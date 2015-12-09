package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;

import java.util.LinkedHashMap;
import java.util.List;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.android.presentation.OnBackPressedListener;
import info.mschmitt.githubapp.android.presentation.OnErrorListener;
import info.mschmitt.githubapp.android.presentation.OnLoadingListener;
import info.mschmitt.githubapp.domain.AnalyticsManager;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.network.GitHubService;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositorySplitPresenter extends BaseObservable
        implements OnBackPressedListener, RepositoryListPresenter.ParentPresenter,
        RepositoryPagerPresenter.ParentPresenter {
    private static final String STATE_DETAILS_VIEW_ACTIVE = "STATE_DETAILS_VIEW_ACTIVE";
    private final AnalyticsManager mAnalyticsManager;
    private final GitHubService mGitHubService;
    private final Observable<LinkedHashMap<Long, Repository>> mRepositories;
    private final BehaviorSubject<LinkedHashMap<Long, Repository>> mRepositoriesSubject;
    private String mUsername;
    private CompositeSubscription mSubscriptions;
    private boolean mDetailsViewActive;
    private RepositoriesSplitView mView;
    private boolean mLoading;

    public RepositorySplitPresenter(GitHubService gitHubService,
                                    AnalyticsManager analyticsManager) {
        mAnalyticsManager = analyticsManager;
        mGitHubService = gitHubService;
        mRepositoriesSubject = BehaviorSubject.create();
        mRepositories = mRepositoriesSubject.asObservable();
    }

    public void onCreate(RepositoriesSplitView view, String username, Bundle savedState) {
        mSubscriptions = new CompositeSubscription();
        mUsername = username;
        mView = view;
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
                    mView.getParentPresenter().onLoading(this, true, null);
                }).subscribe(repositories -> mRepositoriesSubject.onNext(indexById(repositories)),
                        throwable -> mView.getParentPresenter()
                                .onError(RepositorySplitPresenter.this, throwable, this::observe));
        mSubscriptions.add(subscription);
        setLoading(true);
        mView.getParentPresenter().onLoading(this, false, subscription::unsubscribe);
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
        mView = null;
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
        if (mDetailsViewActive && mView.getDetailsPresenter().onBackPressed()) {
            return true;
        }
        if (mDetailsViewActive && !mView.isInSplitMode()) {
            mView.hideDetailsView();
            mDetailsViewActive = false;
            return true;
        }
        return mView.getMasterPresenter().onBackPressed();
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

    public interface RepositoriesSplitView {
        RepositoryListPresenter getMasterPresenter();

        RepositoryPagerPresenter getDetailsPresenter();

        void showDetailsView();

        ParentPresenter getParentPresenter();

        boolean isInSplitMode();

        void hideDetailsView();
    }

    public interface ParentPresenter extends OnLoadingListener, OnErrorListener {
    }
}
