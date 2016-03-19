package info.mschmitt.githubbrowser.app.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubbrowser.ui.fragments.AboutViewDialogFragment;
import info.mschmitt.githubbrowser.ui.scopes.AboutViewScope;

/**
 * @author Matthias Schmitt
 */
@AboutViewScope
@Subcomponent(modules = {AboutViewModule.class})
interface AboutViewComponent extends AboutViewDialogFragment.Component {
}
