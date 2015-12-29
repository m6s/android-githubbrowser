package info.mschmitt.githubapp.application;

import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.utils.AlertDialogs;
import info.mschmitt.githubapp.utils.LoadingProgressManager;
import info.mschmitt.githubapp.viewmodels.RepositoryDetailsViewModel;
import info.mschmitt.githubapp.viewmodels.RepositoryListViewModel;
import info.mschmitt.githubapp.viewmodels.RepositoryPagerViewModel;
import info.mschmitt.githubapp.viewmodels.RepositorySplitViewModel;
import info.mschmitt.githubapp.viewmodels.RootViewModel;
import info.mschmitt.githubapp.viewmodels.UsernameViewModel;

/**
 * @author Matthias Schmitt
 */
public class NavigationManager
        implements UsernameViewModel.NavigationHandler, RepositorySplitViewModel.NavigationHandler,
        RepositoryListViewModel.NavigationHandler, RepositoryPagerViewModel.NavigationHandler,
        RepositoryDetailsViewModel.NavigationHandler, RootViewModel.NavigationHandler {
    private final LoadingProgressManager mLoadingProgressManager;
    private RepositorySplitViewFragment mRepositorySplitViewFragment;
    private RootViewFragment mRootViewFragment;
    private UsernameViewFragment mUsernameViewFragment;
    private RepositoryPagerViewFragment mRepositoryPagerViewFragment;
    private RepositoryListViewFragment mRepositoryListViewFragment;
    private RepositoryDetailsViewFragment mRepositoryDetailsViewFragment;
    private MainActivity mMainActivity;

    public NavigationManager(LoadingProgressManager loadingProgressManager) {
        mLoadingProgressManager = loadingProgressManager;
    }

    @Override
    public void showRepository(Repository repository) {
        mRepositorySplitViewFragment.showDetailsView();
    }

    public boolean onBackPressed() {
        if (mLoadingProgressManager.cancelAllTasks(true)) {
            return true;
        }
        if (mRepositorySplitViewFragment != null &&
                mRepositorySplitViewFragment.hideDetailsView()) {
            return true;
        }
        return mRootViewFragment.getChildFragmentManager().popBackStackImmediate();
    }

    public void onHomeOrUpPressed() {
        mRepositorySplitViewFragment.hideDetailsView();
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
