package info.mschmitt.githubapp.app;

import android.databinding.DataBindingUtil;
import android.view.View;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.databinding.RepositorySplitViewBinding;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.viewmodels.RepositoryDetailsViewModel;
import info.mschmitt.githubapp.viewmodels.RepositoryListViewModel;
import info.mschmitt.githubapp.viewmodels.RepositoryPagerViewModel;
import info.mschmitt.githubapp.viewmodels.RepositorySplitViewModel;
import info.mschmitt.githubapp.viewmodels.RootViewModel;
import info.mschmitt.githubapp.viewmodels.UsernameViewModel;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Matthias Schmitt
 */
public class NavigationManager
        implements UsernameViewModel.NavigationHandler, RepositorySplitViewModel.NavigationHandler,
        RepositoryListViewModel.NavigationHandler, RepositoryPagerViewModel.NavigationHandler,
        RepositoryDetailsViewModel.NavigationHandler, RootViewModel.NavigationHandler {
    private final LoadingProgressManager mLoadingProgressManager;
    private final BehaviorSubject<Repository> mSelectedRepository = BehaviorSubject.create();
    private final BehaviorSubject<LinkedHashMap<Long, Repository>> mRepositoryMap =
            BehaviorSubject.create();
    private RepositorySplitViewFragment mRepositorySplitViewFragment;
    private RootViewFragment mRootViewFragment;
    private UsernameViewFragment mUsernameViewFragment;
    private RepositoryPagerViewFragment mRepositoryPagerViewFragment;
    private RepositoryListViewFragment mRepositoryListViewFragment;
    private RepositoryDetailsViewFragment mRepositoryDetailsViewFragment;
    private MainActivity mMainActivity;
    private boolean mDetailsViewActive;

    @Inject
    public NavigationManager(LoadingProgressManager loadingProgressManager) {
        mLoadingProgressManager = loadingProgressManager;
    }

    @Override
    public void showRepositories(LinkedHashMap<Long, Repository> repositoryMap) {
        mRepositoryMap.onNext(repositoryMap);
    }

    @Override
    public void showRepository(Repository repository) {
        mSelectedRepository.onNext(repository);
        if (mDetailsViewActive) {
            return;
        }
        RepositorySplitViewBinding binding =
                DataBindingUtil.getBinding(mRepositorySplitViewFragment.getView());
        assert binding != null;
        if (!isInSplitMode()) {
            binding.masterView.setVisibility(View.GONE);
        }
        binding.detailsView.setVisibility(View.VISIBLE);
        mDetailsViewActive = true;
    }

    @Override
    public Observable<LinkedHashMap<Long, Repository>> getRepositoryMap() {
        return mRepositoryMap.asObservable();
    }

    @Override
    public Observable<Repository> getRepository() {
        return mSelectedRepository.asObservable();
    }

    private boolean isInSplitMode() {
        return mRepositorySplitViewFragment.getResources().getBoolean(R.bool.split);
    }

    public boolean onBackPressed() {
        if (mLoadingProgressManager.cancelAllTasks(true)) {
            return true;
        }
        if (mDetailsViewActive && !isInSplitMode()) {
            hideRepositoryDetailsView();
            return true;
        }
        return mRootViewFragment.getChildFragmentManager().popBackStackImmediate();
    }

    private void hideRepositoryDetailsView() {
        RepositorySplitViewBinding binding =
                DataBindingUtil.getBinding(mRepositorySplitViewFragment.getView());
        assert binding != null;
        binding.masterView.setVisibility(View.VISIBLE);
        if (!isInSplitMode()) {
            binding.detailsView.setVisibility(View.GONE);
        }
        mDetailsViewActive = false;
    }

    public void onHomeOrUpPressed() {
        if (mDetailsViewActive) {
            hideRepositoryDetailsView();
        }
    }


    @Override
    public void showRepositorySplitView(String username) {
        mRootViewFragment.getChildFragmentManager().beginTransaction()
                .replace(R.id.contentView, RepositorySplitViewFragment.newInstance(username))
                .addToBackStack(null).commit();
    }

    @Override
    public void showError(Throwable throwable, Runnable retryHandler) {
        AlertDialogs.showErrorDialog(mMainActivity, throwable, retryHandler);
    }

    public void onCreate(RepositorySplitViewFragment repositorySplitViewFragment) {
        mRepositorySplitViewFragment = repositorySplitViewFragment;
    }

    public void onDestroy(RepositorySplitViewFragment repositorySplitViewFragment) {
        mRepositorySplitViewFragment = null;
    }

    public void onCreate(RootViewFragment rootViewFragment) {
        mRootViewFragment = rootViewFragment;
    }

    public void onDestroy(RootViewFragment rootViewFragment) {
        mRootViewFragment = null;
    }

    public void onCreate(UsernameViewFragment usernameViewFragment) {
        mUsernameViewFragment = usernameViewFragment;
    }

    public void onDestroy(UsernameViewFragment usernameViewFragment) {
        mUsernameViewFragment = null;
    }

    public void onCreate(RepositoryPagerViewFragment repositoryPagerViewFragment) {
        mRepositoryPagerViewFragment = repositoryPagerViewFragment;
    }

    public void onDestroy(RepositoryPagerViewFragment repositoryPagerViewFragment) {
        mRepositoryPagerViewFragment = null;
    }

    public void onCreate(RepositoryListViewFragment repositoryListViewFragment) {
        mRepositoryListViewFragment = repositoryListViewFragment;
    }


    public void onDestroy(RepositoryListViewFragment repositoryListViewFragment) {
        mRepositoryListViewFragment = null;
    }

    public void onCreate(RepositoryDetailsViewFragment repositoryDetailsViewFragment) {
        mRepositoryDetailsViewFragment = repositoryDetailsViewFragment;
    }

    public void onDestroy(RepositoryDetailsViewFragment repositoryDetailsViewFragment) {
        mRepositoryDetailsViewFragment = null;
    }

    public void onDestroy(MainActivity mainActivity) {
        mMainActivity = null;
    }

    public void onCreate(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }
}
