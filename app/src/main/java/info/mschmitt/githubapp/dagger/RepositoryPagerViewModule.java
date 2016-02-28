package info.mschmitt.githubapp.dagger;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.RepositoryPagerViewFragment;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryPagerViewModule {
    @Provides
    @RepositoryPagerViewScope
    public RepositoryPagerViewFragment.Component provideComponent(
            RepositoryPagerViewComponent component) {
        return component;
    }
}
