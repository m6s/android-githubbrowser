package info.mschmitt.githubbrowser.app.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubbrowser.ui.fragments.UsernameViewFragment;
import info.mschmitt.githubbrowser.ui.scopes.UsernameViewScope;

/**
 * @author Matthias Schmitt
 */
@UsernameViewScope
@Subcomponent(modules = {UsernameViewModule.class})
interface UsernameViewComponent extends UsernameViewFragment.Component {
}
