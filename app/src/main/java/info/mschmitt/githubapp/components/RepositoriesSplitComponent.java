package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.app.RepositorySplitFragment;
import info.mschmitt.githubapp.modules.RepositoryListModule;
import info.mschmitt.githubapp.modules.RepositoryPagerModule;
import info.mschmitt.githubapp.modules.RepositorySplitModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositorySplitModule.class})
public interface RepositoriesSplitComponent extends RepositorySplitFragment.Component {
    @Override
    RepositoryListComponent plus(RepositoryListModule module);

    @Override
    RepositoryPagerComponent plus(RepositoryPagerModule module);
}
