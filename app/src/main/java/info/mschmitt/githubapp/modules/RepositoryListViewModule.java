package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.NavigationManager;
import info.mschmitt.githubapp.viewmodels.RepositoryListViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryListViewModule {
    @Provides
    @Singleton
    public RepositoryListViewModel provideViewModel(NavigationManager navigationManager) {
        return new RepositoryListViewModel(navigationManager);
    }
}
