package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.fragments.MainFragment;
import info.mschmitt.githubapp.modules.MainModule;
import info.mschmitt.githubapp.modules.RepositoriesSplitModule;
import info.mschmitt.githubapp.modules.UsernameModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {MainModule.class})
public interface MainComponent extends MainFragment.Component {
    @Override
    RepositoriesSplitComponent plus(RepositoriesSplitModule module);

    @Override
    UsernameComponent plus(UsernameModule module);
}
