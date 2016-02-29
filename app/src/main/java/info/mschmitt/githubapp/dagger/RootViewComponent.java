package info.mschmitt.githubapp.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RepositorySplitViewFragment;
import info.mschmitt.githubapp.application.RootViewFragment;
import info.mschmitt.githubapp.application.UsernameViewFragment;
import info.mschmitt.githubapp.scopes.RootViewScope;

/**
 * @author Matthias Schmitt
 */
@RootViewScope
@Subcomponent(modules = {RootViewModule.class})
abstract class RootViewComponent implements RootViewFragment.Component {
    @Override
    public void inject(RepositorySplitViewFragment fragment) {
        repositorySplitViewComponent().inject(fragment);
    }

    @Override
    public void inject(UsernameViewFragment fragment) {
        usernameViewComponent().inject(fragment);
    }

    abstract UsernameViewComponent usernameViewComponent();

    abstract RepositorySplitViewComponent repositorySplitViewComponent();

    public abstract void inject(RootViewFragment fragment);
}
