package info.mschmitt.githubapp.di;

import dagger.Component;
import info.mschmitt.githubapp.application.GitHubApplication;

/**
 * @author Matthias Schmitt
 */
@GitHubApplicationScope
@Component(modules = {GitHubApplicationModule.class, NetworkModule.class})
public interface GitHubApplicationComponent extends GitHubApplication.Component {
    @Override
    RootViewComponent plus(RootViewModule module);
}
