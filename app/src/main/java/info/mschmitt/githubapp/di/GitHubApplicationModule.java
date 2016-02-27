package info.mschmitt.githubapp.di;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.di.qualifiers.ApplicationContext;
import info.mschmitt.githubapp.di.qualifiers.Resources;
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
    @GitHubApplicationScope
    public Validator provideValidator() {
        return new Validator();
    }

    @Provides
    @GitHubApplicationScope
    public AnalyticsService provideAnalyticsService(@ApplicationContext Context context) {
        return new AnalyticsService(context);
    }

    @Provides
    @GitHubApplicationScope
    public UserDownloader provideUserDownloader(GitHubService gitHubService) {
        return new UserDownloader(gitHubService);
    }

    @Provides
    @GitHubApplicationScope
    public RepositoryDownloader provideRepositoryDownloader(GitHubService gitHubService) {
        return new RepositoryDownloader(gitHubService);
    }

    @Provides
    @GitHubApplicationScope
    @ApplicationContext
    public Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @GitHubApplicationScope
    @Resources
    public android.content.res.Resources provideResources(@ApplicationContext Context context) {
        return context.getResources();
    }
}
