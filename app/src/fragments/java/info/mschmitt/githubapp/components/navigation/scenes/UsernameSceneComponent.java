package info.mschmitt.githubapp.components.navigation.scenes;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.components.UsernameComponent;
import info.mschmitt.githubapp.fragments.navigation.scenes.UsernameSceneFragment;
import info.mschmitt.githubapp.modules.navigation.UsernameModule;
import info.mschmitt.githubapp.modules.navigation.scenes.UsernameSceneModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {UsernameSceneModule.class})
public interface UsernameSceneComponent extends UsernameSceneFragment.Component {
    @Override
    UsernameComponent plus(UsernameModule module);
}
