package info.mschmitt.githubapp.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RootViewFragment;

/**
 * @author Matthias Schmitt
 */
@RootViewScope
@Subcomponent(modules = {RootViewModule.class})
public interface RootViewComponent extends RootViewFragment.Component {
    @Override
    RepositorySplitViewComponent plus(RepositorySplitViewModule module);

    @Override
    UsernameViewComponent plus(UsernameViewModule module);
}
