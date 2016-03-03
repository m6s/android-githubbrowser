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
import info.mschmitt.githubbrowser.domain.AnalyticsService;
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
    private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();
    private final BehaviorSubject<Long> mSelectedRepositorySubject = BehaviorSubject.create(-1l);
    private final BehaviorSubject<LinkedHashMap<Long, Repository>> mRepositoryMapSubject =
            BehaviorSubject.create(new LinkedHashMap<>());
    private final Resources mResources;
    private final RepositoryDownloader mRepositoryDownloader;
    private final AnalyticsService mAnalyticsService;
    private final LoadingProgressManager mLoadingProgressManager;
    private final NavigationHandler mNavigationHandler;
    private String mUsername;
    private CompositeSubscription mSubscriptions;

    @Inject
    public RepositorySplitViewModel(Resources resources, RepositoryDownloader repositoryDownloader,
                                    AnalyticsService analyticsService,
                                    LoadingProgressManager loadingProgressManager,
                                    NavigationHandler navigationHandler) {
        mResources = resources;
        mRepositoryDownloader = repositoryDownloader;
        mAnalyticsService = analyticsService;
        mLoadingProgressManager = loadingProgressManager;
        mNavigationHandler = navigationHandler;
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
        State.restoreInstanceState(this, savedState);
        mUsername = username;
    }

    public void onResume() {
        mSubscriptions = new CompositeSubscription();
        connectModel();
    }

    private void connectModel() {
        mSubscriptions.add(mLoadingProgressManager.getLoadingStateObservable()
                .subscribe(this::onNextLoadingState));
        mSubscriptions.add(mSelectedRepositorySubject.subscribe(this::onNextSelectedRepository));
        Single<LinkedHashMap<Long, Repository>> download =
                mRepositoryDownloader.download(mUsername).observeOn(AndroidSchedulers.mainThread())
                        .doOnUnsubscribe(() -> mLoadingProgressManager.notifyLoadingEnd(this));
        CompositeSubscription subscription = new CompositeSubscription();
        mLoadingProgressManager.notifyLoadingBegin(this, subscription::unsubscribe);
        subscription.add(download.subscribe(mRepositoryMapSubject::onNext, throwable -> {
            mAnalyticsService.logError(throwable);
            mNavigationHandler.showError(throwable, this::connectModel);
        }));
        mSubscriptions.add(subscription);
    }

    private void onNextSelectedRepository(long id) {
        mPropertyChangeRegistry.notifyChange(this, BR.detailsViewActive);
    }

    private void onNextLoadingState(boolean loading) {
        mPropertyChangeRegistry.notifyChange(this, BR.loading);
    }

    public void onSave(Bundle outState) {
        State.saveInstanceState(this, outState);
    }

    public void onPause() {
        mSubscriptions.unsubscribe();
    }

    @Bindable
    public boolean getLoading() {
        return mLoadingProgressManager.isLoading();
    }

    public boolean onAboutOptionsItemSelected() {
        mNavigationHandler.showAboutView();
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

    public interface NavigationHandler {
        void showError(Throwable throwable, Runnable retryHandler);

        void showAboutView();
    }

    private static class State {
        private static final String SELECTED_REPOSITORY = "SELECTED_REPOSITORY";

        private static void saveInstanceState(RepositorySplitViewModel viewModel, Bundle outState) {
            outState.putLong(SELECTED_REPOSITORY, viewModel.mSelectedRepositorySubject.getValue());
        }

        private static void restoreInstanceState(RepositorySplitViewModel viewModel,
                                                 Bundle savedState) {
            if (savedState == null) {
                return;
            }
            viewModel.mSelectedRepositorySubject.onNext(savedState.getLong(SELECTED_REPOSITORY));
        }
    }
}
