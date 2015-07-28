package info.mschmitt.githubapp.components;

import dagger.Subcomponent;
import info.mschmitt.githubapp.RepositoryListActivity;
import info.mschmitt.githubapp.modules.RepositoryListActivityModule;
import info.mschmitt.githubapp.android.scopes.ActivityScope;

/**
 * @author Matthias Schmitt
 */
@ActivityScope
@Subcomponent(modules = {RepositoryListActivityModule.class})
public interface RepositoryListActivityComponent {
    void inject(RepositoryListActivity activity);
}
