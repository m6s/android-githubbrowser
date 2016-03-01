package info.mschmitt.githubbrowser.app.dagger;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubbrowser.app.GitHubApplication;
import info.mschmitt.githubbrowser.ui.fragments.RootViewFragment;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Component(modules = {GitHubApplicationModule.class, DefaultDomainModule.class,
        DefaultNetworkModule.class})
public abstract class GitHubApplicationComponent implements GitHubApplication.Component {
    @Override
    public void inject(RootViewFragment fragment) {
        rootViewComponent(fragment).inject(fragment);
    }

    RootViewComponent rootViewComponent(RootViewFragment fragment) {
        return rootViewComponent(new RootViewModule(fragment));
    }

    abstract RootViewComponent rootViewComponent(RootViewModule module);
}
