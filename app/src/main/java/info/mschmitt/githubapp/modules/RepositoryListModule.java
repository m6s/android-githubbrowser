package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.app.NavigationManager;
import info.mschmitt.githubapp.presenters.RepositoryListViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryListModule {
    @Provides
    @Singleton
    public RepositoryListViewModel providePresenter(NavigationManager navigationManager) {
        return new RepositoryListViewModel(navigationManager);
    }
}
