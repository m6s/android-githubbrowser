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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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
        mAnalyticsService.logScreenView(getClass().getName());
        mSubscriptions.add(mSelectedRepository.subscribe(mNavigationHandler::showRepository));
        load();
    }

    private void load() {
        setLoading(true);
        Subscription subscription =
                mRepositoryDownloader.download(mUsername).observeOn(AndroidSchedulers.mainThread())
                        .doOnUnsubscribe(() -> {
                            setLoading(false);
                            mLoadingProgressManager.notifyLoading(this, true, null);
                        }).subscribe(mRepositoryMap::onNext,
                        throwable -> mNavigationHandler.showError(throwable, this::load));
        mSubscriptions.add(subscription);
        setLoading(true);
        mLoadingProgressManager.notifyLoading(this, false, subscription::unsubscribe);
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

    public interface NavigationHandler {
        void showRepository(Repository repository);

        void showError(Throwable throwable, Runnable retryHandler);
    }
}
