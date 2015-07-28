package info.mschmitt.githubapp.components;

import dagger.Subcomponent;
import info.mschmitt.githubapp.android.scopes.RetainedFragmentScope;
import info.mschmitt.githubapp.fragments.navigation.MainFragment;
import info.mschmitt.githubapp.modules.MainModule;
import info.mschmitt.githubapp.modules.RepositoriesSplitSceneModule;
import info.mschmitt.githubapp.modules.UsernameSceneModule;

/**
 * @author Matthias Schmitt
 */
@RetainedFragmentScope
@Subcomponent(modules = {MainModule.class})
public interface MainComponent extends MainFragment.Component {
    @Override
    RepositoriesSplitSceneComponent plus(RepositoriesSplitSceneModule module);

    @Override
    UsernameSceneComponent plus(UsernameSceneModule module);
}
