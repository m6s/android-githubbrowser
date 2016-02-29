package info.mschmitt.githubapp.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RepositoryListViewFragment;
import info.mschmitt.githubapp.application.RepositoryPagerViewFragment;
import info.mschmitt.githubapp.application.RepositorySplitViewFragment;
import info.mschmitt.githubapp.scopes.RepositorySplitViewScope;

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
