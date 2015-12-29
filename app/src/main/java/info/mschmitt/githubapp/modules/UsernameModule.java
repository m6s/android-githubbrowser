package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.app.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsManager;
import info.mschmitt.githubapp.domain.LoadingProgressManager;
import info.mschmitt.githubapp.domain.Validator;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.presenters.UsernameViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class UsernameModule {
    @Provides
    @Singleton
    public UsernameViewModel providePresenter(Validator validator, GitHubService gitHubService,
                                              AnalyticsManager analyticsManager,
                                              LoadingProgressManager loadingProgressManager,
                                              NavigationManager navigationManager) {
        return new UsernameViewModel(validator, gitHubService, analyticsManager,
                loadingProgressManager, navigationManager);
    }
}
