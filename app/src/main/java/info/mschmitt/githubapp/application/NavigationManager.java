package info.mschmitt.githubapp.application;

import android.support.v4.app.Fragment;

import javax.inject.Inject;

import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.ghdomain.AnalyticsService;
import info.mschmitt.githubapp.scopes.RootViewScope;
import info.mschmitt.githubapp.viewmodels.RepositoryDetailsViewModel;
import info.mschmitt.githubapp.viewmodels.RepositoryListViewModel;
import info.mschmitt.githubapp.viewmodels.RepositoryPagerViewModel;
import info.mschmitt.githubapp.viewmodels.RepositorySplitViewModel;
import info.mschmitt.githubapp.viewmodels.RootViewModel;
import info.mschmitt.githubapp.viewmodels.UsernameViewModel;

/**
 * @author Matthias Schmitt
 */
@RootViewScope
public class NavigationManager
        implements UsernameViewModel.NavigationHandler, RepositorySplitViewModel.NavigationHandler,
        RepositoryListViewModel.NavigationHandler, RepositoryPagerViewModel.NavigationHandler,
        RepositoryDetailsViewModel.NavigationHandler, RootViewModel.NavigationHandler {
    private final AnalyticsService mAnalyticsService;
    private final Fragment mRootViewFragment;

    @Inject
    public NavigationManager(RootViewFragment rootViewFragment, AnalyticsService analyticsService) {
        mRootViewFragment = rootViewFragment;
        mAnalyticsService = analyticsService;
    }

    public boolean goBack() {
        boolean handled = false;
        RepositorySplitViewFragment repositorySplitViewFragment = findRepositorySplitViewFragment();
        if (repositorySplitViewFragment != null) {
            handled = repositorySplitViewFragment.hideDetailsView();
        }
        if (!handled) {
            handled = mRootViewFragment.getChildFragmentManager().popBackStackImmediate();
        }
        return handled;
    }

    private RepositorySplitViewFragment findRepositorySplitViewFragment() {
        Fragment fragment =
                mRootViewFragment.getChildFragmentManager().findFragmentById(R.id.contentView);
        return fragment instanceof RepositorySplitViewFragment ?
                (RepositorySplitViewFragment) fragment : null;
    }

    public void goUp() {
        RepositorySplitViewFragment repositorySplitViewFragment = findRepositorySplitViewFragment();
        if (repositorySplitViewFragment == null || !repositorySplitViewFragment.hideDetailsView()) {
            mRootViewFragment.getChildFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void showRepositorySplitView(String username) {
        mAnalyticsService.logScreenView(RepositorySplitViewFragment.class.getName());
        mRootViewFragment.getChildFragmentManager().beginTransaction()
                .replace(R.id.contentView, RepositorySplitViewFragment.newInstance(username))
                .addToBackStack(null).commit();
    }

    @Override
    public void showError(Throwable throwable, Runnable retryHandler) {
        AlertDialogs.showErrorDialog(mRootViewFragment.getContext(), throwable, retryHandler);
    }

    @Override
    public void showAboutView() {
    }
}
