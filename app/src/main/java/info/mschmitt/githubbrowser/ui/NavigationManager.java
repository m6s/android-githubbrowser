package info.mschmitt.githubbrowser.ui;

import android.support.v4.app.Fragment;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.domain.AnalyticsService;
import info.mschmitt.githubbrowser.ui.fragments.RepositorySplitViewFragment;
import info.mschmitt.githubbrowser.ui.fragments.RootViewFragment;
import info.mschmitt.githubbrowser.ui.scopes.RootViewScope;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositoryDetailsViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositoryListViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositoryPagerViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositorySplitViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.RootViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.UsernameViewModel;

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
        Fragment fragment = mRootViewFragment.getChildFragmentManager()
                .findFragmentById(info.mschmitt.githubbrowser.R.id.contentView);
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
                .replace(info.mschmitt.githubbrowser.R.id.contentView,
                        RepositorySplitViewFragment.newInstance(username))
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
