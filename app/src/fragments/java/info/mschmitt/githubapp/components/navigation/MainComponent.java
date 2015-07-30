package info.mschmitt.githubapp.components.navigation;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.components.navigation.scenes.RepositoriesSplitSceneComponent;
import info.mschmitt.githubapp.components.navigation.scenes.UsernameSceneComponent;
import info.mschmitt.githubapp.fragments.navigation.MainFragment;
import info.mschmitt.githubapp.modules.navigation.MainModule;
import info.mschmitt.githubapp.modules.navigation.scenes.RepositoriesSplitSceneModule;
import info.mschmitt.githubapp.modules.navigation.scenes.UsernameSceneModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {MainModule.class})
public interface MainComponent extends MainFragment.Component {
    @Override
    RepositoriesSplitSceneComponent plus(RepositoriesSplitSceneModule module);

    @Override
    UsernameSceneComponent plus(UsernameSceneModule module);
}
