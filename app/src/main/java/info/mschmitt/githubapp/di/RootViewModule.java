package info.mschmitt.githubapp.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.NavigationManager;
import info.mschmitt.githubapp.application.RootViewFragment;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.utils.LoadingProgressManager;
import info.mschmitt.githubapp.viewmodels.RootViewModel;

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
    @Singleton
    public RootViewModel provideViewModel(LoadingProgressManager loadingProgressManager,
                                          NavigationManager navigationManager) {
        return new RootViewModel(loadingProgressManager, navigationManager);
    }

    @Provides
    @Singleton
    public NavigationManager provideNavigationManager(AnalyticsService analyticsService) {
        return new NavigationManager(mRootViewFragment, analyticsService);
    }

    @Provides
    @Singleton
    public LoadingProgressManager provideLoadingProgressManager() {
        return new LoadingProgressManager();
    }

}
