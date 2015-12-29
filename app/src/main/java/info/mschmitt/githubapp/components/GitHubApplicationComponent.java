package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubapp.app.GitHubApplication;
import info.mschmitt.githubapp.modules.GitHubApplicationModule;
import info.mschmitt.githubapp.modules.MainActivityModule;
import info.mschmitt.githubapp.modules.NetworkModule;
import info.mschmitt.githubapp.modules.RootViewModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Component(modules = {GitHubApplicationModule.class, NetworkModule.class})
public interface GitHubApplicationComponent extends GitHubApplication.Component {
    @Override
    RootViewComponent plus(RootViewModule module);

    @Override
    MainActivityComponent plus(MainActivityModule module);
}
