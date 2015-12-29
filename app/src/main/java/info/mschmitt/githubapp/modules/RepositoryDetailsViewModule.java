package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.app.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.viewmodels.RepositoryDetailsViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryDetailsViewModule {
    @Provides
    @Singleton
    public RepositoryDetailsViewModel provideViewModel(AnalyticsService analyticsService,
                                                       NavigationManager navigationManager) {
        return new RepositoryDetailsViewModel(analyticsService, navigationManager);
    }
}
