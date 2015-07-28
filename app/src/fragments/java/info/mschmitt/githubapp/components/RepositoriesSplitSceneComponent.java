package info.mschmitt.githubapp.components;

import dagger.Subcomponent;
import info.mschmitt.githubapp.android.scopes.FragmentScope;
import info.mschmitt.githubapp.fragments.navigation.scenes.RepositoriesSplitSceneFragment;
import info.mschmitt.githubapp.modules.RepositoriesSplitSceneModule;

/**
 * @author Matthias Schmitt
 */
@FragmentScope
@Subcomponent(modules = {RepositoriesSplitSceneModule.class})
public interface RepositoriesSplitSceneComponent extends RepositoriesSplitSceneFragment.Component {
}
