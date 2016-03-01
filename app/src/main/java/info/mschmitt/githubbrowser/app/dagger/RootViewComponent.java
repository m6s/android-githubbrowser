package info.mschmitt.githubbrowser.app.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubbrowser.ui.fragments.RepositorySplitViewFragment;
import info.mschmitt.githubbrowser.ui.fragments.RootViewFragment;
import info.mschmitt.githubbrowser.ui.fragments.UsernameViewFragment;
import info.mschmitt.githubbrowser.ui.scopes.RootViewScope;

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
