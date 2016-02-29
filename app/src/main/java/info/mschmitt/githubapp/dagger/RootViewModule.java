package info.mschmitt.githubapp.dagger;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.NavigationManager;
import info.mschmitt.githubapp.application.RootViewFragment;
import info.mschmitt.githubapp.java.LoadingProgressManager;
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
