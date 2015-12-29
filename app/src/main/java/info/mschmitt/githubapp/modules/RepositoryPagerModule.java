package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.app.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsManager;
import info.mschmitt.githubapp.presenters.RepositoryPagerViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryPagerModule {
    @Provides
    @Singleton
    public RepositoryPagerViewModel providePresenter(AnalyticsManager analyticsManager,
                                                     NavigationManager navigationManager) {
        return new RepositoryPagerViewModel(analyticsManager, navigationManager);
    }
}
