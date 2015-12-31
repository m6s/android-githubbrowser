package info.mschmitt.githubapp.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.domain.RepositoryDownloader;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.utils.LoadingProgressManager;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositorySplitViewModel extends BaseObservable {
    private final BehaviorSubject<Repository> mSelectedRepository = BehaviorSubject.create();
    private final RepositoryDownloader mRepositoryDownloader;
    private final AnalyticsService mAnalyticsService;
    private final LoadingProgressManager mLoadingProgressManager;
    private final NavigationHandler mNavigationHandler;
    private final BehaviorSubject<LinkedHashMap<Long, Repository>> mRepositoryMap =
            BehaviorSubject.create();
    private String mUsername;
    private CompositeSubscription mSubscriptions;
    private boolean mLoading;

    @Inject
    public RepositorySplitViewModel(RepositoryDownloader repositoryDownloader,
                                    AnalyticsService analyticsService,
                                    LoadingProgressManager loadingProgressManager,
                                    NavigationHandler navigationHandler) {
        mRepositoryDownloader = repositoryDownloader;
        mAnalyticsService = analyticsService;
        mLoadingProgressManager = loadingProgressManager;
        mNavigationHandler = navigationHandler;
    }

    public Subject<Repository, Repository> getSelectedRepository() {
        return mSelectedRepository;
    }

    public void onCreate(String username, Bundle savedState) {
        mSubscriptions = new CompositeSubscription();
        mUsername = username;
        mSubscriptions.add(mSelectedRepository.subscribe(mNavigationHandler::showRepository));
        load();
    }

    private void load() {
        setLoading(true);
        ConnectableObservable<LinkedHashMap<Long, Repository>> map =
                mRepositoryDownloader.download(mUsername).observeOn(AndroidSchedulers.mainThread())
                        .doOnUnsubscribe(() -> {
                            setLoading(false);
                            mLoadingProgressManager.notifyLoadingEnd(this);
                        }).publish();
        map.subscribe(mRepositoryMap::onNext, throwable -> {
            mAnalyticsService.logError(throwable);
            mNavigationHandler.showError(throwable, this::load);
        });
        map.connect(subscription -> {
            mSubscriptions.add(subscription);
            setLoading(true);
            mLoadingProgressManager.notifyLoadingBegin(this, subscription::unsubscribe);
        });
    }

    public Observable<LinkedHashMap<Long, Repository>> getRepositoryMap() {
        return mRepositoryMap.asObservable();
    }

    public void onSave(Bundle outState) {
    }

    public void onDestroy() {
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

    public interface NavigationHandler {
        void showRepository(Repository repository);

        void showError(Throwable throwable, Runnable retryHandler);

        void showAboutView();
    }
}
