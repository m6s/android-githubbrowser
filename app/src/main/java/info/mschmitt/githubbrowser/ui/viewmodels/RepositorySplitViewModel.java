package info.mschmitt.githubbrowser.ui.viewmodels;

import android.content.res.Resources;
import android.databinding.Bindable;
import android.databinding.PropertyChangeRegistry;
import android.os.Bundle;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.BR;
import info.mschmitt.githubbrowser.R;
import info.mschmitt.githubbrowser.android.databinding.DataBindingObservable;
import info.mschmitt.githubbrowser.app.qualifiers.ApplicationResources;
import info.mschmitt.githubbrowser.domain.RepositoryDownloader;
import info.mschmitt.githubbrowser.entities.Repository;
import info.mschmitt.githubbrowser.java.LoadingProgressManager;
import info.mschmitt.githubbrowser.ui.scopes.RepositorySplitViewScope;
import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@RepositorySplitViewScope
public class RepositorySplitViewModel implements DataBindingObservable {
    private static final LinkedHashMap<Long, Repository> EMPTY_REPOSITORY_MAP =
            new LinkedHashMap<>();
    private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();
    private final BehaviorSubject<Long> mSelectedRepositorySubject = BehaviorSubject.create(-1l);
    private final BehaviorSubject<LinkedHashMap<Long, Repository>> mRepositoryMapSubject =
            BehaviorSubject.create(EMPTY_REPOSITORY_MAP);
    private final Resources mResources;
    private final RepositoryDownloader mRepositoryDownloader;
    private final AnalyticsService mAnalyticsService;
    private final LoadingProgressManager mLoadingProgressManager;
    private final NavigationService mNavigationService;
    private String mUsername;
    private CompositeSubscription mSubscriptions;

    @Inject
    public RepositorySplitViewModel(@ApplicationResources Resources resources,
                                    RepositoryDownloader repositoryDownloader,
                                    AnalyticsService analyticsService,
                                    LoadingProgressManager loadingProgressManager,
                                    NavigationService NavigationService) {
        mResources = resources;
        mRepositoryDownloader = repositoryDownloader;
        mAnalyticsService = analyticsService;
        mLoadingProgressManager = loadingProgressManager;
        mNavigationService = NavigationService;
    }

    public BehaviorSubject<Long> getSelectedRepositorySubject() {
        return mSelectedRepositorySubject;
    }

    public Observable<LinkedHashMap<Long, Repository>> getRepositoryMapObservable() {
        return mRepositoryMapSubject.asObservable();
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mPropertyChangeRegistry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mPropertyChangeRegistry.remove(callback);
    }

    public void onLoad(String username, Bundle savedState) {
        InstanceStateUtils.load(this, savedState);
        mUsername = username;
    }

    public void onResume() {
        mSubscriptions = new CompositeSubscription();
        connectModel();
        if (mRepositoryMapSubject.getValue() == EMPTY_REPOSITORY_MAP) {
            initializeRepositoryMap();
        }
    }

    private void connectModel() {
        connectLoadingState();
    }

    private void initializeRepositoryMap() {
        Single<LinkedHashMap<Long, Repository>> download =
                mRepositoryDownloader.download(mUsername).observeOn(AndroidSchedulers.mainThread())
                        .doOnUnsubscribe(() -> mLoadingProgressManager.notifyLoadingEnd(this));
        CompositeSubscription subscription = new CompositeSubscription();
        mLoadingProgressManager.notifyLoadingBegin(this, subscription::unsubscribe);
        subscription.add(download.subscribe(mRepositoryMapSubject::onNext, throwable -> {
            mAnalyticsService.logError(throwable);
            mNavigationService.showError(throwable, this::initializeRepositoryMap);
        }));
        mSubscriptions.add(subscription);
    }

    private void connectLoadingState() {
        mSubscriptions.add(mLoadingProgressManager.getLoadingStateObservable()
                .subscribe(this::onNextLoadingState));
        mSubscriptions.add(mSelectedRepositorySubject.subscribe(this::onNextSelectedRepository));
    }

    private void onNextSelectedRepository(long id) {
        mPropertyChangeRegistry.notifyChange(this, BR.detailsViewActive);
    }

    private void onNextLoadingState(boolean loading) {
        mPropertyChangeRegistry.notifyChange(this, BR.loading);
    }

    public void onSave(Bundle outState) {
        InstanceStateUtils.save(this, outState);
    }

    public void onPause() {
        mSubscriptions.unsubscribe();
    }

    @Bindable
    public boolean getLoading() {
        return mLoadingProgressManager.isLoading();
    }

    public boolean onAboutOptionsItemSelected() {
        mNavigationService.showAboutView();
        return true;
    }

    public boolean onHideDetailsView() {
        if (!getDetailsViewActive() || mResources.getBoolean(R.bool.split)) {
            return false;
        }
        mSelectedRepositorySubject.onNext(-1l);
        return true;
    }

    @Bindable
    public boolean getDetailsViewActive() {
        return mSelectedRepositorySubject.getValue() != -1l;
    }

    public interface NavigationService {
        void showError(Throwable throwable, Runnable retryHandler);

        void showAboutView();
    }

    public interface AnalyticsService {
        void logError(Throwable throwable);
    }

    private static class InstanceStateUtils {
        private static final String SELECTED_REPOSITORY = "SELECTED_REPOSITORY";

        private static void save(RepositorySplitViewModel viewModel, Bundle outState) {
            outState.putLong(SELECTED_REPOSITORY, viewModel.mSelectedRepositorySubject.getValue());
        }

        private static void load(RepositorySplitViewModel viewModel, Bundle savedState) {
            if (savedState == null) {
                return;
            }
            viewModel.mSelectedRepositorySubject.onNext(savedState.getLong(SELECTED_REPOSITORY));
        }
    }
}
