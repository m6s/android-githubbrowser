package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.AnalyticsManager;
import info.mschmitt.githubapp.Validator;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.presenters.UsernameViewPresenter;

/**
 * @author Matthias Schmitt
 */
@Module
public class UsernameModule {
    private UsernameViewPresenter.UsernameSceneView mView;

    public UsernameModule(UsernameViewPresenter.UsernameSceneView view) {
        mView = view;
    }

    @Provides
    @Singleton
    public UsernameViewPresenter providePresenter(Validator validator, GitHubService gitHubService,
                                                  AnalyticsManager analyticsManager) {
        return new UsernameViewPresenter(mView, validator, gitHubService, analyticsManager);
    }
}
