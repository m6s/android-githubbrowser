package info.mschmitt.githubapp.components;

import dagger.Subcomponent;
import info.mschmitt.githubapp.android.scopes.FragmentScope;
import info.mschmitt.githubapp.fragments.navigation.scenes.UsernameSceneFragment;
import info.mschmitt.githubapp.modules.UsernameSceneModule;

/**
 * @author Matthias Schmitt
 */
@FragmentScope
@Subcomponent(modules = {UsernameSceneModule.class})
public interface UsernameSceneComponent extends UsernameSceneFragment.Component {
}
