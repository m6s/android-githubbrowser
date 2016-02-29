package info.mschmitt.githubapp.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RepositorySplitViewFragment;

/**
 * @author Matthias Schmitt
 */
@RepositorySplitViewScope
@Subcomponent(modules = {RepositorySplitViewModule.class})
public interface RepositorySplitViewComponent extends RepositorySplitViewFragment.Component {
    @Override
    RepositoryListViewComponent repositoryListViewComponent();

    @Override
    RepositoryPagerViewComponent repositoryPagerViewComponent();
}
