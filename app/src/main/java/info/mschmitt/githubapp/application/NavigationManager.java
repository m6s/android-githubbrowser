package info.mschmitt.githubapp.application;

import android.support.v4.app.Fragment;

import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.utils.AlertDialogs;
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
    private final AnalyticsService mAnalyticsService;
    private MainActivity mMainActivity;

    public NavigationManager(AnalyticsService analyticsService) {
        mAnalyticsService = analyticsService;
    }

    @Override
    public void showRepository(Repository repository) {
        findRepositorySplitViewFragment().showDetailsView();
    }

    private RepositorySplitViewFragment findRepositorySplitViewFragment() {
        Fragment fragment =
                getRootViewFragment().getChildFragmentManager().findFragmentById(R.id.contentView);
        return fragment instanceof RepositorySplitViewFragment ?
                (RepositorySplitViewFragment) fragment : null;
    }

    private RootViewFragment getRootViewFragment() {
        return (RootViewFragment) mMainActivity.getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
    }

    public boolean goBack() {
        boolean handled = false;
        RepositorySplitViewFragment repositorySplitViewFragment = findRepositorySplitViewFragment();
        if (repositorySplitViewFragment != null) {
            handled = repositorySplitViewFragment.hideDetailsView();
        }
        if (!handled) {
            handled = getRootViewFragment().getChildFragmentManager().popBackStackImmediate();
        }
        return handled;
    }

    public void goUp() {
        RepositorySplitViewFragment repositorySplitViewFragment = findRepositorySplitViewFragment();
        if (repositorySplitViewFragment == null || !repositorySplitViewFragment.hideDetailsView()) {
            getRootViewFragment().getChildFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void showRepositorySplitView(String username) {
        mAnalyticsService.logScreenView(RepositorySplitViewFragment.class.getName());
        getRootViewFragment().getChildFragmentManager().beginTransaction()
                .replace(R.id.contentView, RepositorySplitViewFragment.newInstance(username))
                .addToBackStack(null).commit();
    }

    @Override
    public void showError(Throwable throwable, Runnable retryHandler) {
        AlertDialogs.showErrorDialog(mMainActivity, throwable, retryHandler);
    }

    @Override
    public void showAboutView() {
    }

    public void onMainActivityDestroyed() {
        mMainActivity = null;
    }

    public void onMainActivityCreated(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }
}
