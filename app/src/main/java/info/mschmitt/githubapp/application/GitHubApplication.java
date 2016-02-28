package info.mschmitt.githubapp.application;

import android.app.Application;

import javax.inject.Inject;

import info.mschmitt.githubapp.dagger.DaggerGitHubApplicationComponent;
import info.mschmitt.githubapp.dagger.DefaultDomainModule;
import info.mschmitt.githubapp.dagger.DefaultNetworkModule;
import info.mschmitt.githubapp.dagger.GitHubApplicationModule;

/**
 * @author Matthias Schmitt
 */
public class GitHubApplication extends Application
        implements RootViewFragment.Application, MainActivity.Application {
    private Component mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerGitHubApplicationComponent.builder().defaultDomainModule(new DefaultDomainModule())
                .defaultNetworkModule(new DefaultNetworkModule())
                .gitHubApplicationModule(new GitHubApplicationModule(this)).build().inject(this);
    }

    @Override
    public Component getComponent() {
        return mComponent;
    }

    @Inject
    public void setComponent(Component component) {
        mComponent = component;
    }

    public interface Component extends RootViewFragment.SuperComponent {
        void inject(GitHubApplication gitHubApplication);
    }
}
