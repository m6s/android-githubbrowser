package info.mschmitt.githubapp.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.domain.RepositoryDownloader;
import info.mschmitt.githubapp.domain.UserDownloader;
import info.mschmitt.githubapp.domain.Validator;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.utils.LoadingProgressManager;

/**
 * @author Matthias Schmitt
 */
@Module
public class GitHubApplicationModule {
    private Application mApplication;

    public GitHubApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public Validator provideValidator() {
        return new Validator();
    }

    @Provides
    @Singleton
    public AnalyticsService provideAnalyticsService(@Named("ApplicationContext") Context context) {
        return new AnalyticsService(context);
    }

    @Provides
    @Singleton
    public UserDownloader provideUserDownloader(GitHubService gitHubService) {
        return new UserDownloader(gitHubService);
    }

    @Provides
    @Singleton
    public RepositoryDownloader provideRepositoryDownloader(GitHubService gitHubService) {
        return new RepositoryDownloader(gitHubService);
    }

    @Provides
    @Singleton
    public LoadingProgressManager provideLoadingProgressManager() {
        return new LoadingProgressManager();
    }

    @Provides
    @Singleton
    public NavigationManager provideNavigationManager(AnalyticsService analyticsService) {
        return new NavigationManager(analyticsService);
    }

    @Provides
    @Singleton
    @Named("ApplicationContext")
    public Context provideApplicationContext() {
        return mApplication;
    }
}
