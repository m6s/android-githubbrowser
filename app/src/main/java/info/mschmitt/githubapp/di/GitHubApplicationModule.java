package info.mschmitt.githubapp.di;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.domain.RepositoryDownloader;
import info.mschmitt.githubapp.domain.UserDownloader;
import info.mschmitt.githubapp.domain.Validator;
import info.mschmitt.githubapp.network.GitHubService;

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
    @Named("ApplicationContext")
    public Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    @Named("Resources")
    public Resources provideResources() {
        return mApplication.getApplicationContext().getResources();
    }
}
