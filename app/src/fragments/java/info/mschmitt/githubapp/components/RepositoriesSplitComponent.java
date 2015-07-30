package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.fragments.RepositoriesSplitFragment;
import info.mschmitt.githubapp.modules.navigation.RepositoriesSplitModule;
import info.mschmitt.githubapp.modules.navigation.RepositoryListModule;
import info.mschmitt.githubapp.modules.navigation.RepositoryPagerModule;

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
