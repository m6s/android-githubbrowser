package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.app.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.viewmodels.RepositoryPagerViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryPagerViewModule {
    @Provides
    @Singleton
    public RepositoryPagerViewModel provideViewModel(AnalyticsService analyticsService,
                                                     NavigationManager navigationManager) {
        return new RepositoryPagerViewModel(analyticsService, navigationManager);
    }
}
