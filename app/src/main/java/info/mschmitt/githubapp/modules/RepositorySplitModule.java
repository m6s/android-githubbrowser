package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.app.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsManager;
import info.mschmitt.githubapp.domain.LoadingProgressManager;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.presenters.RepositorySplitViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositorySplitModule {
    @Provides
    @Singleton
    public RepositorySplitViewModel providePresenter(GitHubService gitHubService,
                                                     AnalyticsManager analyticsManager,
                                                     LoadingProgressManager loadingProgressManager,
                                                     NavigationManager navigationManager) {
        return new RepositorySplitViewModel(gitHubService, analyticsManager, loadingProgressManager,
                navigationManager);
    }
}
