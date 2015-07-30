package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.fragments.UsernameFragment;
import info.mschmitt.githubapp.modules.navigation.UsernameModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {UsernameModule.class})
public interface UsernameComponent extends UsernameFragment.Component {
}
