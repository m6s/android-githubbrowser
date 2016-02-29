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
    @Inject Component mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerGitHubApplicationComponent.builder().defaultDomainModule(new DefaultDomainModule())
                .defaultNetworkModule(new DefaultNetworkModule())
                .gitHubApplicationModule(new GitHubApplicationModule(this)).build().inject(this);
    }

    @Override
    public void inject(RootViewFragment fragment) {
        mComponent.inject(fragment);
    }

    public interface Component {
        void inject(GitHubApplication gitHubApplication);

        void inject(RootViewFragment fragment);
    }
}
