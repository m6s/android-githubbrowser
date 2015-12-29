package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.app.UsernameViewFragment;
import info.mschmitt.githubapp.modules.UsernameViewModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {UsernameViewModule.class})
public interface UsernameViewComponent extends UsernameViewFragment.Component {
}
