package info.mschmitt.githubapp.dagger;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import dagger.Module;
import dagger.Provides;
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
    @GitHubApplicationScope
    @ApplicationContext
    public Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @GitHubApplicationScope
    public Resources provideResources(@ApplicationContext Context context) {
        return context.getResources();
    }
}
