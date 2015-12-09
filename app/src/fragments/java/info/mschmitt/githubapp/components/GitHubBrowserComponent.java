package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.modules.GitHubBrowserModule;
import info.mschmitt.githubapp.modules.RepositoriesSplitModule;
import info.mschmitt.githubapp.modules.UsernameModule;
import info.mschmitt.githubapp.ui.GitHubBrowserFragment;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {GitHubBrowserModule.class})
public interface GitHubBrowserComponent extends GitHubBrowserFragment.Component {
    @Override
    RepositoriesSplitComponent plus(RepositoriesSplitModule module);

    @Override
    UsernameComponent plus(UsernameModule module);
}
