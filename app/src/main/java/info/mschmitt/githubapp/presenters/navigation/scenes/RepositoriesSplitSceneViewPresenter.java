package info.mschmitt.githubapp.presenters.navigation.scenes;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;

import java.util.List;

import javax.inject.Inject;

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
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoriesSplitSceneViewPresenter extends BaseObservable
        implements OnBackPressedListener, RepositoriesSplitViewPresenter.ParentPresenter {
    private final CompositeSubscription mSubscriptions = new CompositeSubscription();
    private final AnalyticsManager mAnalyticsManager;
    private final GitHubService mGitHubService;
    private final Observable<List<Repository>> mRepositories;
    private final PublishSubject<List<Repository>> mRepositoriesSubject;
    private RepositoriesSplitSceneView mView;
    private String mUsername;
    private boolean mLoading;

    @Inject
    public RepositoriesSplitSceneViewPresenter(GitHubService gitHubService,
                                               AnalyticsManager analyticsManager) {
        mAnalyticsManager = analyticsManager;
        mGitHubService = gitHubService;
        mRepositoriesSubject = PublishSubject.create();
        mRepositories = mRepositoriesSubject.asObservable();
    }

    public void onCreate(RepositoriesSplitSceneView view, Bundle savedState) {
        mView = view;
        mAnalyticsManager.logScreenView(getClass().getName());
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

    @Bindable
    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
        notifyPropertyChanged(BR.username);
        setLoading(true);
        Subscription subscription = mGitHubService.getUserRepositories(username)
                .observeOn(AndroidSchedulers.mainThread()).doOnUnsubscribe(() -> {
                    setLoading(false);
                    mView.getParentPresenter().onLoading(this, true, null);
                }).subscribe(mRepositoriesSubject::onNext, throwable -> {
                    mView.getParentPresenter()
                            .onError(RepositoriesSplitSceneViewPresenter.this, throwable,
                                    () -> setUsername(username));
                });
        mSubscriptions.add(subscription);
        setLoading(true);
        mView.getParentPresenter().onLoading(this, false, subscription::unsubscribe);
    }

    public Observable<List<Repository>> getRepositories() {
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
