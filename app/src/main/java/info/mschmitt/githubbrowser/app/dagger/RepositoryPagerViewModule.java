package info.mschmitt.githubbrowser.app.dagger;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubbrowser.ui.fragments.RepositoryPagerViewFragment;
import info.mschmitt.githubbrowser.ui.scopes.RepositoryPagerViewScope;

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
