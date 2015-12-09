package info.mschmitt.githubapp;

import android.app.Application;

import info.mschmitt.githubapp.components.ApplicationComponent;
import info.mschmitt.githubapp.components.DaggerApplicationComponent;
import info.mschmitt.githubapp.modules.ApplicationModule;
import info.mschmitt.githubapp.modules.NetworkModule;

/**
 * @author Matthias Schmitt
 */
public class GitHubClientApplication extends Application {
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent.builder().networkModule(new NetworkModule(this))
                .appModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
