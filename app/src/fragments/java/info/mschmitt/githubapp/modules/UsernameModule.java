package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.domain.AnalyticsManager;
import info.mschmitt.githubapp.domain.Validator;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.presenters.UsernamePresenter;

/**
 * @author Matthias Schmitt
 */
@Module
public class UsernameModule {
    @Provides
    @Singleton
    public UsernamePresenter providePresenter(Validator validator, GitHubService gitHubService,
                                              AnalyticsManager analyticsManager) {
        return new UsernamePresenter(validator, gitHubService, analyticsManager);
    }
}
