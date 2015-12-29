package info.mschmitt.githubapp.modules;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.app.NavigationManager;
import info.mschmitt.githubapp.domain.LoadingProgressManager;

/**
 * @author Matthias Schmitt
 */
@Module
public class ApplicationModule {
    private Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return mApplication;
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
