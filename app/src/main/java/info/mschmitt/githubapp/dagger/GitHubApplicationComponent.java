package info.mschmitt.githubapp.dagger;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubapp.application.GitHubApplication;
import info.mschmitt.githubapp.application.RootViewFragment;

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

    abstract RootViewComponent rootViewComponent(RootViewModule module);
}
