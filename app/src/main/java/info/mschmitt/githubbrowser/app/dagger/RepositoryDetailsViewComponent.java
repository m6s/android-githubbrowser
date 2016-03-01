package info.mschmitt.githubbrowser.app.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubbrowser.ui.fragments.RepositoryDetailsViewFragment;
import info.mschmitt.githubbrowser.ui.scopes.RepositoryDetailsViewScope;

/**
 * @author Matthias Schmitt
 */
@RepositoryDetailsViewScope
@Subcomponent(modules = {RepositoryDetailsViewModule.class})
interface RepositoryDetailsViewComponent {
    void inject(RepositoryDetailsViewFragment fragment);
}
