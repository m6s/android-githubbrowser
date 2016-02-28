package info.mschmitt.githubapp.application;

import android.app.Application;

import info.mschmitt.githubapp.dagger.DaggerGitHubApplicationComponent;
import info.mschmitt.githubapp.dagger.DefaultDomainModule;
import info.mschmitt.githubapp.dagger.DefaultNetworkModule;
import info.mschmitt.githubapp.dagger.GitHubApplicationModule;

/**
 * @author Matthias Schmitt
 */
public class GitHubApplication extends Application
        implements RootViewFragment.Application, MainActivity.Application {
    private Component mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerGitHubApplicationComponent.builder()
                .defaultDomainModule(new DefaultDomainModule())
                .defaultNetworkModule(new DefaultNetworkModule())
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
