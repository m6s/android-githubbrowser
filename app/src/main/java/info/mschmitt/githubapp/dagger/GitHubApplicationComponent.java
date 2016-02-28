package info.mschmitt.githubapp.dagger;

import dagger.Component;
import info.mschmitt.githubapp.application.GitHubApplication;

/**
 * @author Matthias Schmitt
 */
@GitHubApplicationScope
@Component(modules = {GitHubApplicationModule.class, DefaultDomainModule.class,
        DefaultNetworkModule.class})
public interface GitHubApplicationComponent extends GitHubApplication.Component {
    @Override
    RootViewComponent plus(RootViewModule module);
}
