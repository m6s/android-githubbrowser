package info.mschmitt.githubapp.presenters.navigation.scenes;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;

import java.util.LinkedHashMap;
import java.util.List;

import info.mschmitt.githubapp.AnalyticsManager;
import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.android.presentation.OnBackPressedListener;
import info.mschmitt.githubapp.android.presentation.OnErrorListener;
import info.mschmitt.githubapp.android.presentation.OnLoadingListener;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.presenters.RepositoriesSplitViewPresenter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoriesSplitSceneViewPresenter extends BaseObservable
        implements OnBackPressedListener, RepositoriesSplitViewPresenter.ParentPresenter {
    private final CompositeSubscription mSubscriptions = new CompositeSubscription();
    private final AnalyticsManager mAnalyticsManager;
    private final GitHubService mGitHubService;
    private final Observable<LinkedHashMap<Long, Repository>> mRepositories;
    private final BehaviorSubject<LinkedHashMap<Long, Repository>> mRepositoriesSubject;
    private final String mUsername;
    private RepositoriesSplitSceneView mView;
    private boolean mLoading;

    public RepositoriesSplitSceneViewPresenter(String username, RepositoriesSplitSceneView view,
                                               GitHubService gitHubService,
                                               AnalyticsManager analyticsManager) {
        mUsername = username;
        mView = view;
        mAnalyticsManager = analyticsManager;
        mGitHubService = gitHubService;
        mRepositoriesSubject = BehaviorSubject.create();
        mRepositories = mRepositoriesSubject.asObservable();
    }

    public void onCreate(Bundle savedState) {
        observe();
        mAnalyticsManager.logScreenView(getClass().getName());
    }

    private void observe() {
        setLoading(true);
        Subscription subscription = mGitHubService.getUserRepositories(mUsername)
                .observeOn(AndroidSchedulers.mainThread()).doOnUnsubscribe(() -> {
                    setLoading(false);
                    mView.getParentPresenter().onLoading(this, true, null);
                }).subscribe(repositories -> mRepositoriesSubject.onNext(indexById(repositories)),
                        throwable -> mView.getParentPresenter()
                                .onError(RepositoriesSplitSceneViewPresenter.this, throwable,
                                        this::observe));
        mSubscriptions.add(subscription);
        setLoading(true);
        mView.getParentPresenter().onLoading(this, false, subscription::unsubscribe);
    }

    private static LinkedHashMap<Long, Repository> indexById(List<Repository> repositories) {
        LinkedHashMap<Long, Repository> map = new LinkedHashMap<>();
        for (Repository repository : repositories) {
            map.put(repository.getId(), repository);
        }
        return map;
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

    public Observable<LinkedHashMap<Long, Repository>> getRepositories() {
        return mRepositories;
    }

    @Override
    public boolean onBackPressed() {
        return mView.getChildPresenter().onBackPressed();
    }

    @Override
    public void onError(Object sender, Throwable throwable, Runnable retryHandler) {
        mView.getParentPresenter().onError(sender, throwable, retryHandler);
    }

    @Override
    public void onLoading(Object sender, boolean complete, Runnable cancelHandler) {
        mView.getParentPresenter().onLoading(sender, complete, cancelHandler);
    }

    public interface RepositoriesSplitSceneView {
        RepositoriesSplitViewPresenter getChildPresenter();

        ParentPresenter getParentPresenter();
    }

    public interface ParentPresenter extends OnLoadingListener, OnErrorListener {
    }
}
