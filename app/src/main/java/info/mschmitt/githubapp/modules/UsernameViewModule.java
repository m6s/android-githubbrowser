package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.app.LoadingProgressManager;
import info.mschmitt.githubapp.app.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.domain.Validator;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.presenters.UsernameViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class UsernameViewModule {
    @Provides
    @Singleton
    public UsernameViewModel providePresenter(Validator validator, GitHubService gitHubService,
                                              AnalyticsService analyticsService,
                                              LoadingProgressManager loadingProgressManager,
                                              NavigationManager navigationManager) {
        return new UsernameViewModel(validator, gitHubService, analyticsService,
                loadingProgressManager, navigationManager);
    }
}
