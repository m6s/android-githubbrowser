package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.app.RepositoriesSplitFragment;
import info.mschmitt.githubapp.modules.RepositoriesSplitModule;
import info.mschmitt.githubapp.modules.RepositoryListModule;
import info.mschmitt.githubapp.modules.RepositoryPagerModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositoriesSplitModule.class})
public interface RepositoriesSplitComponent extends RepositoriesSplitFragment.Component {
    @Override
    RepositoryListComponent plus(RepositoryListModule module);

    @Override
    RepositoryPagerComponent plus(RepositoryPagerModule module);
}
