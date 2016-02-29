package info.mschmitt.githubapp.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RepositoryListViewFragment;
import info.mschmitt.githubapp.scopes.RepositoryListViewScope;

/**
 * @author Matthias Schmitt
 */
@RepositoryListViewScope
@Subcomponent(modules = {RepositoryListViewModule.class})
interface RepositoryListViewComponent {
    void inject(RepositoryListViewFragment fragment);
}
