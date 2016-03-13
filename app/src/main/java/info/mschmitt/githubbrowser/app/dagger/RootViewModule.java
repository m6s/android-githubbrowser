package info.mschmitt.githubbrowser.app.dagger;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubbrowser.java.LoadingProgressManager;
import info.mschmitt.githubbrowser.ui.AnalyticsService;
import info.mschmitt.githubbrowser.ui.NavigationService;
import info.mschmitt.githubbrowser.ui.fragments.RootViewFragment;
import info.mschmitt.githubbrowser.ui.scopes.RootViewScope;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositoryDetailsViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositoryListViewModel;
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
    public RootViewModel.NavigationService provideRootViewModelNavigationService(
            NavigationService navigationService) {
        return navigationService;
    }

    @Provides
    @RootViewScope
    public RepositorySplitViewModel.NavigationService
    provideRepositorySplitViewModelNavigationService(
            NavigationService navigationService) {
        return navigationService;
    }

    @Provides
    @RootViewScope
    public UsernameViewModel.NavigationService provideUsernameViewModelNavigationService(
            NavigationService navigationService) {
        return navigationService;
    }

    @Provides
    @RootViewScope
    public UsernameViewModel.AnalyticsService provideUserNameViewModelAnalyticsService(
            AnalyticsService analyticsService) {
        return analyticsService;
    }

    @Provides
    @RootViewScope
    public RepositorySplitViewModel.AnalyticsService
    provideRepositorySplitViewModelAnalyticsService(
            AnalyticsService analyticsService) {
        return analyticsService;
    }

    @Provides
    @RootViewScope
    public RepositoryListViewModel.AnalyticsService provideRepositoryListViewModelAnalyticsService(
            AnalyticsService analyticsService) {
        return analyticsService;
    }

    @Provides
    @RootViewScope
    public RepositoryDetailsViewModel.AnalyticsService
    provideRepositoryDetailsViewModelAnalyticsService(
            AnalyticsService analyticsService) {
        return analyticsService;
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
