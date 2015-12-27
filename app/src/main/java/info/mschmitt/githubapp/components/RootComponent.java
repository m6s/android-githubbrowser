package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.app.RootFragment;
import info.mschmitt.githubapp.modules.RepositoriesSplitModule;
import info.mschmitt.githubapp.modules.RootModule;
import info.mschmitt.githubapp.modules.UsernameModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RootModule.class})
public interface RootComponent extends RootFragment.Component {
    @Override
    RepositoriesSplitComponent plus(RepositoriesSplitModule module);

    @Override
    UsernameComponent plus(UsernameModule module);
}
