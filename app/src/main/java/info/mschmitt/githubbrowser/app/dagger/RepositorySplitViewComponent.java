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
        repositoryPagerViewComponent(fragment).inject(fragment);
    }

    @Override
    public void inject(RepositoryListViewFragment fragment) {
        repositoryListViewComponent().inject(fragment);
    }

    abstract RepositoryListViewComponent repositoryListViewComponent();

    private RepositoryPagerViewComponent repositoryPagerViewComponent(
            RepositoryPagerViewFragment fragment) {
        return repositoryPagerViewComponent(new RepositoryPagerViewModule(fragment));
    }

    abstract RepositoryPagerViewComponent repositoryPagerViewComponent(
            RepositoryPagerViewModule module);

    public abstract void inject(RepositorySplitViewFragment fragment);
}
