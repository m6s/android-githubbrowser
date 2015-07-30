package info.mschmitt.githubapp.components.navigation.scenes;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.components.RepositoriesSplitComponent;
import info.mschmitt.githubapp.fragments.navigation.scenes.RepositoriesSplitSceneFragment;
import info.mschmitt.githubapp.modules.navigation.RepositoriesSplitModule;
import info.mschmitt.githubapp.modules.navigation.scenes.RepositoriesSplitSceneModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositoriesSplitSceneModule.class})
public interface RepositoriesSplitSceneComponent extends RepositoriesSplitSceneFragment.Component {
    @Override
    RepositoriesSplitComponent plus(RepositoriesSplitModule module);
}
