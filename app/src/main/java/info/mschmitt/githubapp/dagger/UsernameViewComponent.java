package info.mschmitt.githubapp.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.UsernameViewFragment;
import info.mschmitt.githubapp.scopes.UsernameViewScope;

/**
 * @author Matthias Schmitt
 */
@UsernameViewScope
@Subcomponent(modules = {UsernameViewModule.class})
interface UsernameViewComponent {
    void inject(UsernameViewFragment fragment);
}
