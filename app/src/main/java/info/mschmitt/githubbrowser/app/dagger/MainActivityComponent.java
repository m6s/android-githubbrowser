package info.mschmitt.githubbrowser.app.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubbrowser.ui.MainActivity;
import info.mschmitt.githubbrowser.ui.scopes.MainActivityScope;

/**
 * @author Matthias Schmitt
 */
@MainActivityScope
@Subcomponent(modules = {MainActivityModule.class})
abstract class MainActivityComponent implements MainActivity.Component {
}
