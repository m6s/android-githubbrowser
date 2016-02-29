package info.mschmitt.githubapp.dagger;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.RepositoryPagerViewFragment;
import info.mschmitt.githubapp.scopes.RepositoryPagerViewScope;

/**
 * @author Matthias Schmitt
 */
@Module
class RepositoryPagerViewModule {
    @Provides
    @RepositoryPagerViewScope
    public RepositoryPagerViewFragment.Component provideComponent(
            RepositoryPagerViewComponent component) {
        return component;
    }
}
