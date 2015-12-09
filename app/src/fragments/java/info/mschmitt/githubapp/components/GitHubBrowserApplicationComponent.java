package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubapp.modules.GitHubBrowserApplicationModule;
import info.mschmitt.githubapp.modules.GitHubBrowserModule;
import info.mschmitt.githubapp.modules.NetworkModule;
import info.mschmitt.githubapp.ui.GitHubBrowserFragment;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Component(modules = {GitHubBrowserApplicationModule.class, NetworkModule.class})
public interface GitHubBrowserApplicationComponent extends GitHubBrowserFragment.SuperComponent {
    @Override
    GitHubBrowserComponent plus(GitHubBrowserModule module);
}
