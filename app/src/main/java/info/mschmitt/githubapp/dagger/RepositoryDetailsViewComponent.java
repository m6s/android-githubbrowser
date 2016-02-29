package info.mschmitt.githubapp.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RepositoryDetailsViewFragment;
import info.mschmitt.githubapp.scopes.RepositoryDetailsViewScope;

/**
 * @author Matthias Schmitt
 */
@RepositoryDetailsViewScope
@Subcomponent(modules = {RepositoryDetailsViewModule.class})
interface RepositoryDetailsViewComponent {
    void inject(RepositoryDetailsViewFragment fragment);
}
