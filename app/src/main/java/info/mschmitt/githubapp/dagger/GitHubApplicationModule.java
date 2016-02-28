package info.mschmitt.githubapp.dagger;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.GitHubApplication;
import info.mschmitt.githubapp.application.qualifiers.ApplicationContext;

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
    @ApplicationContext
    public Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    public Resources provideResources(@ApplicationContext Context context) {
        return context.getResources();
    }

    @Provides
    @Singleton
    public GitHubApplication.Component provideComponent(GitHubApplicationComponent component) {
        return component;
    }
}
