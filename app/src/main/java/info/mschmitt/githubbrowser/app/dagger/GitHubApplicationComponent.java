package info.mschmitt.githubbrowser.app.dagger;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubbrowser.app.GitHubApplication;
import info.mschmitt.githubbrowser.ui.MainActivity;
import info.mschmitt.githubbrowser.ui.fragments.RootViewFragment;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Component(modules = {GitHubApplicationModule.class, DefaultDomainModule.class,
        DefaultNetworkModule.class})
public abstract class GitHubApplicationComponent implements GitHubApplication.Component {
    @Override
    public RootViewComponent rootViewComponent(RootViewFragment fragment) {
        return rootViewComponent(new RootViewModule(fragment));
    }

    @Override
    public MainActivityComponent mainActivityComponent(MainActivity activity) {
        return mainActivityComponent(new MainActivityModule(activity));
    }

    abstract MainActivityComponent mainActivityComponent(MainActivityModule module);

    abstract RootViewComponent rootViewComponent(RootViewModule module);
}
