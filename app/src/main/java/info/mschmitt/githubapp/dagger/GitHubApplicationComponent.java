package info.mschmitt.githubapp.dagger;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubapp.application.GitHubApplication;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Component(modules = {GitHubApplicationModule.class, DefaultDomainModule.class,
        DefaultNetworkModule.class})
public interface GitHubApplicationComponent extends GitHubApplication.Component {
    @Override
    RootViewComponent plus(RootViewModule module);
}
