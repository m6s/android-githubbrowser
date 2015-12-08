package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.fragments.RepositoriesSplitSceneFragment;
import info.mschmitt.githubapp.modules.RepositoriesSplitSceneModule;
import info.mschmitt.githubapp.modules.RepositoryListModule;
import info.mschmitt.githubapp.modules.RepositoryPagerModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositoriesSplitSceneModule.class})
public interface RepositoriesSplitComponent extends RepositoriesSplitSceneFragment.Component {
    @Override
    RepositoryListComponent plus(RepositoryListModule module);

    @Override
    RepositoryPagerComponent plus(RepositoryPagerModule module);
}
