package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.components.RepositoriesSplitSceneComponent;
import info.mschmitt.githubapp.components.UsernameSceneComponent;
import info.mschmitt.githubapp.fragments.MainFragment;
import info.mschmitt.githubapp.modules.MainModule;
import info.mschmitt.githubapp.modules.RepositoriesSplitSceneModule;
import info.mschmitt.githubapp.modules.UsernameSceneModule;

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
