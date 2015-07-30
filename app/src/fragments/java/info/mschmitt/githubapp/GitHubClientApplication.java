package info.mschmitt.githubapp;

import android.app.Application;

import info.mschmitt.githubapp.fragments.navigation.MainFragment;
import info.mschmitt.githubapp.modules.AppModule;
import info.mschmitt.githubapp.modules.NetworkModule;

/**
 * @author Matthias Schmitt
 */
public class GitHubClientApplication extends Application implements MainFragment.Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder().networkModule(new NetworkModule(this))
                .appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    public MainFragment.SuperComponent getSuperComponent(MainFragment fragment) {
        return mAppComponent;
    }
}
