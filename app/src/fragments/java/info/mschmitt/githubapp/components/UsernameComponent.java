package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.modules.UsernameModule;
import info.mschmitt.githubapp.ui.UsernameFragment;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {UsernameModule.class})
public interface UsernameComponent extends UsernameFragment.Component {
}
