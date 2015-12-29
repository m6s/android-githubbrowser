package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;

import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.domain.AnalyticsManager;
import info.mschmitt.githubapp.domain.LoadingProgressManager;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.network.GitHubService;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositorySplitViewModel extends BaseObservable {
    private static final String STATE_DETAILS_VIEW_ACTIVE = "STATE_DETAILS_VIEW_ACTIVE";
    private final AnalyticsManager mAnalyticsManager;
    private final GitHubService mGitHubService;
    private final LoadingProgressManager mLoadingProgressManager;
    private final NavigationHandler mNavigationHandler;
    private String mUsername;
    private CompositeSubscription mSubscriptions;
    private boolean mDetailsViewActive;
    private boolean mLoading;

    @Inject
    public RepositorySplitViewModel(GitHubService gitHubService, AnalyticsManager analyticsManager,
                                    LoadingProgressManager loadingProgressManager,
                                    NavigationHandler navigationHandler) {
        mAnalyticsManager = analyticsManager;
        mGitHubService = gitHubService;
        mLoadingProgressManager = loadingProgressManager;
        mNavigationHandler = navigationHandler;
    }

    public void onCreate(String username, Bundle savedState) {
        mSubscriptions = new CompositeSubscription();
        mUsername = username;
        if (savedState != null) {
            mDetailsViewActive = savedState.getBoolean(STATE_DETAILS_VIEW_ACTIVE); //TODO presenter
        }
        mAnalyticsManager.logScreenView(getClass().getName());
        observe();
    }

    private void observe() {
        setLoading(true);
        Subscription subscription = mGitHubService.getUserRepositories(mUsername)
                .observeOn(AndroidSchedulers.mainThread()).doOnUnsubscribe(() -> {
                    setLoading(false);
                    mLoadingProgressManager.notifyLoading(this, true, null);
                }).subscribe(repositories -> mNavigationHandler
                                .showRepositories(indexById(repositories)),
                        throwable -> mNavigationHandler.showError(throwable, this::observe));
        mSubscriptions.add(subscription);
        setLoading(true);
        mLoadingProgressManager.notifyLoading(this, false, subscription::unsubscribe);
    }

    private static LinkedHashMap<Long, Repository> indexById(List<Repository> repositories) {
//        long seed = System.nanoTime();
//        repositories = new ArrayList<>(repositories);
//        Collections.shuffle(repositories, new Random(seed));
        LinkedHashMap<Long, Repository> map = new LinkedHashMap<>();
        for (Repository repository : repositories) {
            map.put(repository.getId(), repository);
        }
        return map;
    }

    public boolean isDetailsViewActive() {
        return mDetailsViewActive;
    }

    public void onSave(Bundle outState) {
        outState.putBoolean(STATE_DETAILS_VIEW_ACTIVE, mDetailsViewActive);
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
        void showRepositories(LinkedHashMap<Long, Repository> repositoryMap);

        void showError(Throwable throwable, Runnable retryHandler);
    }
}
