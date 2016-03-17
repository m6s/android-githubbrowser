package info.mschmitt.githubbrowser.app.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubbrowser.ui.fragments.AboutViewFragment;
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
    public RepositorySplitViewComponent repositorySplitViewComponent(
            RepositorySplitViewFragment fragment) {
        return repositorySplitViewComponent(new RepositorySplitViewModule());
    }

    @Override
    public UsernameViewComponent usernameViewComponent(UsernameViewFragment fragment) {
        return usernameViewComponent(new UsernameViewModule());
    }

    @Override
    public AboutViewFragment.Component repositorySplitViewComponent(AboutViewFragment fragment) {
        return repositorySplitViewComponent(new AboutViewModule());
    }

    abstract AboutViewComponent repositorySplitViewComponent(AboutViewModule aboutViewModule);

    abstract UsernameViewComponent usernameViewComponent(UsernameViewModule module);

    abstract RepositorySplitViewComponent repositorySplitViewComponent(
            RepositorySplitViewModule module);
}
