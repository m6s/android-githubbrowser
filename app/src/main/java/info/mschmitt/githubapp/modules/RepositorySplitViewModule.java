package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.utils.LoadingProgressManager;
import info.mschmitt.githubapp.viewmodels.RepositorySplitViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositorySplitViewModule {
    @Provides
    @Singleton
    public RepositorySplitViewModel provideViewModel(GitHubService gitHubService,
                                                     AnalyticsService analyticsService,
                                                     LoadingProgressManager loadingProgressManager,
                                                     NavigationManager navigationManager) {
        return new RepositorySplitViewModel(gitHubService, analyticsService, loadingProgressManager,
                navigationManager);
    }
}
