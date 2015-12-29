package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RootViewFragment;
import info.mschmitt.githubapp.modules.RepositorySplitViewModule;
import info.mschmitt.githubapp.modules.RootViewModule;
import info.mschmitt.githubapp.modules.UsernameViewModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RootViewModule.class})
public interface RootViewComponent extends RootViewFragment.Component {
    @Override
    RepositorySplitViewComponent plus(RepositorySplitViewModule module);

    @Override
    UsernameViewComponent plus(UsernameViewModule module);
}
