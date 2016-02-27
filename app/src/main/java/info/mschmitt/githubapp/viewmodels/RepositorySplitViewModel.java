package info.mschmitt.githubapp.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.di.RepositorySplitViewScope;
import info.mschmitt.githubapp.di.qualifiers.Resources;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.domain.RepositoryDownloader;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.java.LoadingProgressManager;
import info.mschmitt.githubapp.java.RxSingleUtils;
import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@RepositorySplitViewScope
public class RepositorySplitViewModel extends BaseObservable {
    private final BehaviorSubject<Repository> mSelectedRepositorySubject = BehaviorSubject.create();
    private final android.content.res.Resources mResources;
    private final RepositoryDownloader mRepositoryDownloader;
    private final AnalyticsService mAnalyticsService;
    private final LoadingProgressManager mLoadingProgressManager;
    private final NavigationHandler mNavigationHandler;
    private final BehaviorSubject<LinkedHashMap<Long, Repository>> mRepositoryMapSubject =
            BehaviorSubject.create();
    private String mUsername;
    private CompositeSubscription mSubscriptions;
    private boolean mLoading;
    private boolean mDetailsViewActive;

    @Inject
    public RepositorySplitViewModel(@Resources android.content.res.Resources resources,
                                    RepositoryDownloader repositoryDownloader,
                                    AnalyticsService analyticsService,
                                    LoadingProgressManager loadingProgressManager,
                                    NavigationHandler navigationHandler) {
        mResources = resources;
        mRepositoryDownloader = repositoryDownloader;
        mAnalyticsService = analyticsService;
        mLoadingProgressManager = loadingProgressManager;
        mNavigationHandler = navigationHandler;
    }

    public Subject<Repository, Repository> getSelectedRepositorySubject() {
        return mSelectedRepositorySubject;
    }

    public void onLoad(String username, Bundle savedState) {
        State.restoreInstanceState(this, savedState);
        mUsername = username;
    }

    public void onResume() {
        mSubscriptions = new CompositeSubscription();
        ConnectableObservable<Repository> selected =
                mSelectedRepositorySubject.observeOn(AndroidSchedulers.mainThread()).publish();
        selected.subscribe(this::onNextSelectedRepository);
        selected.connect(mSubscriptions::add);
        connectModel();
    }

    private void connectModel() {
        setLoading(true);
        Single<LinkedHashMap<Long, Repository>> download =
                mRepositoryDownloader.download(mUsername).observeOn(AndroidSchedulers.mainThread())
                        .doOnUnsubscribe(() -> {
                            setLoading(false);
                            mLoadingProgressManager.notifyLoadingEnd(this);
                        });
        RxSingleUtils.subscribe(download, mRepositoryMapSubject::onNext, throwable -> {
            mAnalyticsService.logError(throwable);
            mNavigationHandler.showError(throwable, this::connectModel);
        }, subscription -> {
            mSubscriptions.add(subscription);
            setLoading(true);
            mLoadingProgressManager.notifyLoadingBegin(this, subscription::unsubscribe);
        });
    }

    private void onNextSelectedRepository(Repository repository) {
        mNavigationHandler.showRepository(repository);
    }

    public Observable<LinkedHashMap<Long, Repository>> getRepositoryMapObservable() {
        return mRepositoryMapSubject.asObservable();
    }

    public void onSave(Bundle outState) {
        State.saveInstanceState(this, outState);
    }

    public void onPause() {
        mSubscriptions.unsubscribe();
    }

    @Bindable
    public boolean isLoading() {
        return mLoading;
    }

    private void setLoading(boolean loading) {
        mLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    public boolean onAboutOptionsItemSelected() {
        mNavigationHandler.showAboutView();
        return true;
    }

    @Bindable
    public boolean getDetailsViewActive() {
        return mDetailsViewActive;
    }

    private void setDetailsViewActive(boolean active) {
        if (mDetailsViewActive == active) {
            return;
        }
        mDetailsViewActive = active;
        notifyPropertyChanged(BR.detailsViewActive);
    }

    public boolean onHideDetailsView() {
        if (!mDetailsViewActive || mResources.getBoolean(R.bool.split)) {
            return false;
        }
        setDetailsViewActive(false);
        return true;
    }

    public void onShowDetailsView() {
        setDetailsViewActive(true);
    }

    public interface NavigationHandler {
        void showRepository(Repository repository);

        void showError(Throwable throwable, Runnable retryHandler);

        void showAboutView();
    }

    private static class State {
        private static final String DETAILS_VIEW_ACTIVE = "DETAILS_VIEW_ACTIVE";

        private static void saveInstanceState(RepositorySplitViewModel viewModel, Bundle outState) {
            outState.putBoolean(DETAILS_VIEW_ACTIVE, viewModel.mDetailsViewActive);
        }

        private static void restoreInstanceState(RepositorySplitViewModel viewModel,
                                                 Bundle savedState) {
            if (savedState == null) {
                return;
            }
            viewModel.mDetailsViewActive = savedState.getBoolean(DETAILS_VIEW_ACTIVE);
        }
    }
}
