package info.mschmitt.githubapp.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.UsernameViewFragment;

/**
 * @author Matthias Schmitt
 */
@UsernameViewScope
@Subcomponent(modules = {UsernameViewModule.class})
public interface UsernameViewComponent extends UsernameViewFragment.Component {
}
