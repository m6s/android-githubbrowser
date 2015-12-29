package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.app.LoadingProgressManager;
import info.mschmitt.githubapp.app.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.presenters.RepositorySplitViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositorySplitViewModule {
    @Provides
    @Singleton
    public RepositorySplitViewModel providePresenter(GitHubService gitHubService,
                                                     AnalyticsService analyticsService,
                                                     LoadingProgressManager loadingProgressManager,
                                                     NavigationManager navigationManager) {
        return new RepositorySplitViewModel(gitHubService, analyticsService, loadingProgressManager,
                navigationManager);
    }
}
