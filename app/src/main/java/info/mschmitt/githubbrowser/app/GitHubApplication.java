package info.mschmitt.githubbrowser.app;

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
public class GitHubApplication extends android.app.Application
        implements RootViewFragment.FragmentHost, MainActivity.Application {
    @Inject Component mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerGitHubApplicationComponent.builder().defaultDomainModule(new DefaultDomainModule())
                .defaultNetworkModule(new DefaultNetworkModule())
                .gitHubApplicationModule(new GitHubApplicationModule(this)).build().inject(this);
    }

    @Override
    public RootViewFragment.Component rootViewComponent(RootViewFragment fragment) {
        return mComponent.rootViewComponent(fragment);
    }

    @Override
    public MainActivity.Component mainActivityComponent(MainActivity activity) {
        return mComponent.mainActivityComponent(activity);
    }

    public interface Component {
        RootViewFragment.Component rootViewComponent(RootViewFragment fragment);

        MainActivity.Component mainActivityComponent(MainActivity activity);

        void inject(GitHubApplication application);
    }
}
