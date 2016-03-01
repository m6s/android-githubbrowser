package info.mschmitt.githubbrowser.app;

import android.app.Application;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.app.dagger.DaggerGitHubApplicationComponent;
import info.mschmitt.githubbrowser.app.dagger.DefaultDomainModule;
import info.mschmitt.githubbrowser.app.dagger.DefaultNetworkModule;
import info.mschmitt.githubbrowser.app.dagger.GitHubApplicationModule;
import info.mschmitt.githubbrowser.ui.MainActivity;
import info.mschmitt.githubbrowser.ui.fragments.RootViewFragment;

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
