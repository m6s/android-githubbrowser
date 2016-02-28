package info.mschmitt.githubapp.dagger;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.NavigationManager;
import info.mschmitt.githubapp.application.RootViewFragment;
import info.mschmitt.githubapp.java.LoadingProgressManager;
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
public class RootViewModule {
    private final RootViewFragment mRootViewFragment;

    public RootViewModule(RootViewFragment rootViewFragment) {
        mRootViewFragment = rootViewFragment;
    }

    @Provides
    public RootViewFragment provideRootViewFragment() {
        return mRootViewFragment;
    }

    @Provides
    public RootViewModel.NavigationHandler provideRootViewModelNavigationHandler(
            NavigationManager navigationManager) {
        return navigationManager;
    }

    @Provides
    public RepositoryDetailsViewModel.NavigationHandler
    provideRepositoryDetailsViewModelNavigationHandler(
            NavigationManager navigationManager) {
        return navigationManager;
    }

    @Provides
    public RepositoryListViewModel.NavigationHandler
    provideRepositoryListViewModelNavigationHandler(
            NavigationManager navigationManager) {
        return navigationManager;
    }

    @Provides
    public RepositoryPagerViewModel.NavigationHandler
    provideRepositoryPagerViewModelNavigationHandler(
            NavigationManager navigationManager) {
        return navigationManager;
    }

    @Provides
    public RepositorySplitViewModel.NavigationHandler
    provideRepositorySplitViewModelNavigationHandler(
            NavigationManager navigationManager) {
        return navigationManager;
    }

    @Provides
    public UsernameViewModel.NavigationHandler provideUsernameViewModelNavigationHandler(
            NavigationManager navigationManager) {
        return navigationManager;
    }

    @Provides
    @RootViewScope
    public LoadingProgressManager provideLoadingProgressManager() {
        return new LoadingProgressManager();
    }
}
