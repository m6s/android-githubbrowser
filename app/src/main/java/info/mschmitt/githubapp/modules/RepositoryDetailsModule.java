package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.app.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsManager;
import info.mschmitt.githubapp.presenters.RepositoryDetailsViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryDetailsModule {
    @Provides
    @Singleton
    public RepositoryDetailsViewModel providePresenter(AnalyticsManager analyticsManager,
                                                       NavigationManager navigationManager) {
        return new RepositoryDetailsViewModel(analyticsManager, navigationManager);
    }
}
