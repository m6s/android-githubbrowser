package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.presenters.GitHubBrowserPresenter;

/**
 * @author Matthias Schmitt
 */
@Module
public class RootModule {
    @Provides
    @Singleton
    public GitHubBrowserPresenter providePresenter() {
        return new GitHubBrowserPresenter();
    }
}
