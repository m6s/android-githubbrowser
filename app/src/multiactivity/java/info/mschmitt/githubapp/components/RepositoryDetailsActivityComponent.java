package info.mschmitt.githubapp.components;

import dagger.Subcomponent;
import info.mschmitt.githubapp.RepositoryDetailsActivity;
import info.mschmitt.githubapp.modules.RepositoryDetailsActivityModule;
import info.mschmitt.githubapp.android.scopes.ActivityScope;

/**
 * @author Matthias Schmitt
 */
@ActivityScope
@Subcomponent(modules = {RepositoryDetailsActivityModule.class})
public interface RepositoryDetailsActivityComponent {
    void inject(RepositoryDetailsActivity activity);
}
