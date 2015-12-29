package info.mschmitt.githubapp.di;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RootViewFragment;

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
