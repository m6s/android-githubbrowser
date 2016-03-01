package info.mschmitt.githubbrowser.app.dagger;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubbrowser.java.LoadingProgressManager;
import info.mschmitt.githubbrowser.ui.NavigationManager;
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
@Module
class RootViewModule {
    private final RootViewFragment mRootViewFragment;

    public RootViewModule(RootViewFragment rootViewFragment) {
        mRootViewFragment = rootViewFragment;
    }

    @Provides
    @RootViewScope
    public RootViewFragment provideRootViewFragment() {
        return mRootViewFragment;
    }

    @Provides
    @RootViewScope
    public RootViewModel.NavigationHandler provideRootViewModelNavigationHandler(
            NavigationManager navigationManager) {
        return navigationManager;
    }

    @Provides
    @RootViewScope
    public RepositoryDetailsViewModel.NavigationHandler
    provideRepositoryDetailsViewModelNavigationHandler(
            NavigationManager navigationManager) {
        return navigationManager;
    }

    @Provides
    @RootViewScope
    public RepositoryListViewModel.NavigationHandler
    provideRepositoryListViewModelNavigationHandler(
            NavigationManager navigationManager) {
        return navigationManager;
    }

    @Provides
    @RootViewScope
    public RepositoryPagerViewModel.NavigationHandler
    provideRepositoryPagerViewModelNavigationHandler(
            NavigationManager navigationManager) {
        return navigationManager;
    }

    @Provides
    @RootViewScope
    public RepositorySplitViewModel.NavigationHandler
    provideRepositorySplitViewModelNavigationHandler(
            NavigationManager navigationManager) {
        return navigationManager;
    }

    @Provides
    @RootViewScope
    public UsernameViewModel.NavigationHandler provideUsernameViewModelNavigationHandler(
            NavigationManager navigationManager) {
        return navigationManager;
    }

    @Provides
    @RootViewScope
    public LoadingProgressManager provideLoadingProgressManager() {
        return new LoadingProgressManager();
    }

    @Provides
    @RootViewScope
    public RootViewFragment.Component provideComponent(RootViewComponent component) {
        return component;
    }
}
