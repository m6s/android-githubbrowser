package info.mschmitt.githubapp.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.NavigationManager;
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
    public LoadingProgressManager provideLoadingProgressManager() {
        return new LoadingProgressManager();
    }

    @Provides
    @Singleton
    public NavigationManager provideNavigationManager(
            LoadingProgressManager loadingProgressManager) {
        return new NavigationManager(loadingProgressManager);
    }

    @Provides
    @Singleton
    @Named("ApplicationContext")
    public Context provideApplicationContext() {
        return mApplication;
    }
}
