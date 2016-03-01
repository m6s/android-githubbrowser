package info.mschmitt.githubbrowser.app.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubbrowser.ui.fragments.RepositoryListViewFragment;
import info.mschmitt.githubbrowser.ui.fragments.RepositoryPagerViewFragment;
import info.mschmitt.githubbrowser.ui.fragments.RepositorySplitViewFragment;
import info.mschmitt.githubbrowser.ui.scopes.RepositorySplitViewScope;

/**
 * @author Matthias Schmitt
 */
@RepositorySplitViewScope
@Subcomponent(modules = {RepositorySplitViewModule.class})
abstract class RepositorySplitViewComponent implements RepositorySplitViewFragment.Component {
    @Override
    public void inject(RepositoryPagerViewFragment fragment) {
        repositoryPagerViewComponent().inject(fragment);
    }

    @Override
    public void inject(RepositoryListViewFragment fragment) {
        repositoryListViewComponent().inject(fragment);
    }

    abstract RepositoryListViewComponent repositoryListViewComponent();

    abstract RepositoryPagerViewComponent repositoryPagerViewComponent();

    public abstract void inject(RepositorySplitViewFragment fragment);
}
