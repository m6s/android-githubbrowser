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
import info.mschmitt.githubapp.presenters.RepositoryListViewPresenter;
import info.mschmitt.githubapp.presenters.RepositoryPagerViewPresenter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoriesSplitSceneViewPresenter extends BaseObservable
        implements OnBackPressedListener, RepositoryListViewPresenter.ParentPresenter,
        RepositoryPagerViewPresenter.ParentPresenter {
    public static final String STATE_DETAILS_VIEW_ACTIVE = "STATE_DETAILS_VIEW_ACTIVE";
    private final CompositeSubscription mSubscriptions = new CompositeSubscription();
    private final AnalyticsManager mAnalyticsManager;
    private final GitHubService mGitHubService;
    private final Observable<List<Repository>> mRepositories;
    private final PublishSubject<List<Repository>> mRepositoriesSubject;
    private RepositoriesSplitSceneView mView;
    private boolean mDetailsViewActive;
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

    public boolean isDetailsViewActive() {
        return mDetailsViewActive;
    }

    public void setDetailsViewActive(boolean detailsViewActive) {
        mDetailsViewActive = detailsViewActive;
    }

    public void onCreate(RepositoriesSplitSceneView view, Bundle savedState) {
        mView = view;
        mAnalyticsManager.logScreenView(getClass().getName());
        if (savedState != null) {
            mDetailsViewActive = savedState.getBoolean(STATE_DETAILS_VIEW_ACTIVE); //TODO presenter
        }
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
    public void onRepositorySelected(Object sender, Repository repository) {
        if (sender == mView.getMasterPresenter()) {
            if (!mDetailsViewActive) {
                mView.showDetailsView();
                mDetailsViewActive = true;
            }
            mView.getDetailsPresenter().selectRepository(repository);
        } else if (sender == mView.getDetailsPresenter()) {
            mView.getMasterPresenter().selectRepository(repository);
        } else {
            throw new AssertionError();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mDetailsViewActive && mView.getDetailsPresenter().onBackPressed()) {
            return true;
        }
        if (mDetailsViewActive && !mView.isInSplitMode()) {
            mView.hideDetailsView();
            return true;
        }
        return mView.getMasterPresenter().onBackPressed();
    }

    public interface RepositoriesSplitSceneView {
        RepositoryListViewPresenter getMasterPresenter();

        RepositoryPagerViewPresenter getDetailsPresenter();

        void showDetailsView();

        ParentPresenter getParentPresenter();

        boolean isInSplitMode();

        void hideDetailsView();
    }

    public interface ParentPresenter extends OnLoadingListener, OnErrorListener {
    }
}
