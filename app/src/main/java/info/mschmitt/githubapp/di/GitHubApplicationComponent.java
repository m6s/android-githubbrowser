package info.mschmitt.githubapp.di;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubapp.application.GitHubApplication;

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
