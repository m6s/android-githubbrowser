package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.app.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.presenters.RepositoryPagerViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryPagerViewModule {
    @Provides
    @Singleton
    public RepositoryPagerViewModel providePresenter(AnalyticsService analyticsService,
                                                     NavigationManager navigationManager) {
        return new RepositoryPagerViewModel(analyticsService, navigationManager);
    }
}
