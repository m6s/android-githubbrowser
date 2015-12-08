package info.mschmitt.githubapp.presenters;

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
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoriesSplitViewPresenter extends BaseObservable
        implements OnBackPressedListener, RepositoryListViewPresenter.ParentPresenter,
        RepositoryPagerViewPresenter.ParentPresenter {
    private static final String STATE_DETAILS_VIEW_ACTIVE = "STATE_DETAILS_VIEW_ACTIVE";
    private final CompositeSubscription mSubscriptions = new CompositeSubscription();
    private final AnalyticsManager mAnalyticsManager;
    private final GitHubService mGitHubService;
    private final Observable<LinkedHashMap<Long, Repository>> mRepositories;
    private final BehaviorSubject<LinkedHashMap<Long, Repository>> mRepositoriesSubject;
    private final String mUsername;
    private boolean mDetailsViewActive;
    private RepositoriesSplitSceneView mView;
    private boolean mLoading;

    public RepositoriesSplitViewPresenter(String username, RepositoriesSplitSceneView view,
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
        if (savedState != null) {
            mDetailsViewActive = savedState.getBoolean(STATE_DETAILS_VIEW_ACTIVE); //TODO presenter
        }
    }

    private void observe() {
        setLoading(true);
        Subscription subscription = mGitHubService.getUserRepositories(mUsername)
                .observeOn(AndroidSchedulers.mainThread()).doOnUnsubscribe(() -> {
                    setLoading(false);
                    mView.getParentPresenter().onLoading(this, true, null);
                }).subscribe(repositories -> mRepositoriesSubject.onNext(indexById(repositories)),
                        throwable -> mView.getParentPresenter()
                                .onError(RepositoriesSplitViewPresenter.this, throwable,
                                        this::observe));
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
