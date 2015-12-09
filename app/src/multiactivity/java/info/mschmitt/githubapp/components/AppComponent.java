package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubapp.modules.GitHubBrowserApplicationModule;
import info.mschmitt.githubapp.modules.NetworkModule;
import info.mschmitt.githubapp.modules.RepositoryDetailsActivityModule;
import info.mschmitt.githubapp.modules.RepositoryListActivityModule;
import info.mschmitt.githubapp.modules.UsernameActivityModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Component(modules = {GitHubBrowserApplicationModule.class, NetworkModule.class})
public interface AppComponent {
    UsernameActivityComponent plus(UsernameActivityModule module);

    RepositoryDetailsActivityComponent plus(RepositoryDetailsActivityModule module);

    RepositoryListActivityComponent plus(RepositoryListActivityModule module);
}
