package info.mschmitt.githubapp.di;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RepositorySplitViewFragment;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositorySplitViewModule.class})
public interface RepositorySplitViewComponent extends RepositorySplitViewFragment.Component {
    @Override
    RepositoryListViewComponent plus(RepositoryListViewModule module);

    @Override
    RepositoryPagerViewComponent plus(RepositoryPagerViewModule module);
}
