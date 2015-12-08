package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.components.UsernameComponent;
import info.mschmitt.githubapp.fragments.UsernameSceneFragment;
import info.mschmitt.githubapp.modules.UsernameModule;
import info.mschmitt.githubapp.modules.UsernameSceneModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {UsernameSceneModule.class})
public interface UsernameSceneComponent extends UsernameSceneFragment.Component {
    @Override
    UsernameComponent plus(UsernameModule module);
}
