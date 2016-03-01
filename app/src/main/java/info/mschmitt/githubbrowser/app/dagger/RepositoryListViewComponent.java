package info.mschmitt.githubbrowser.app.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubbrowser.ui.fragments.RepositoryListViewFragment;
import info.mschmitt.githubbrowser.ui.scopes.RepositoryListViewScope;

/**
 * @author Matthias Schmitt
 */
@RepositoryListViewScope
@Subcomponent(modules = {RepositoryListViewModule.class})
interface RepositoryListViewComponent {
    void inject(RepositoryListViewFragment fragment);
}
