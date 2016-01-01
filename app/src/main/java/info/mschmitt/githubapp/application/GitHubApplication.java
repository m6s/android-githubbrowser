package info.mschmitt.githubapp.application;

import android.app.Application;

import info.mschmitt.githubapp.di.DaggerGitHubApplicationComponent;
import info.mschmitt.githubapp.di.GitHubApplicationModule;
import info.mschmitt.githubapp.di.NetworkModule;

/**
 * @author Matthias Schmitt
 */
public class GitHubApplication extends Application
        implements RootViewFragment.Application, MainActivity.Application {
    private Component mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent =
                DaggerGitHubApplicationComponent.builder().networkModule(new NetworkModule(this))
                        .gitHubApplicationModule(new GitHubApplicationModule(this)).build();
        mApplicationComponent.inject(this);
    }

    @Override
    public Component getComponent() {
        return mApplicationComponent;
    }

    public interface Component extends RootViewFragment.SuperComponent {
        void inject(GitHubApplication gitHubApplication);
    }
}
