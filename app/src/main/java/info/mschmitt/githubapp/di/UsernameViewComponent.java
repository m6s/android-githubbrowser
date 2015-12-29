package info.mschmitt.githubapp.di;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.UsernameViewFragment;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {UsernameViewModule.class})
public interface UsernameViewComponent extends UsernameViewFragment.Component {
}
