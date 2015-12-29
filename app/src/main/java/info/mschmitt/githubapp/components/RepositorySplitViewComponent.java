package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.app.RepositorySplitViewFragment;
import info.mschmitt.githubapp.modules.RepositoryListViewModule;
import info.mschmitt.githubapp.modules.RepositoryPagerViewModule;
import info.mschmitt.githubapp.modules.RepositorySplitViewModule;

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
