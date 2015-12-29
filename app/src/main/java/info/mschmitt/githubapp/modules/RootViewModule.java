package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.app.LoadingProgressManager;
import info.mschmitt.githubapp.app.NavigationManager;
import info.mschmitt.githubapp.presenters.RootViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class RootViewModule {
    @Provides
    @Singleton
    public RootViewModel providePresenter(LoadingProgressManager loadingProgressManager,
                                          NavigationManager navigationManager) {
        return new RootViewModel(loadingProgressManager, navigationManager);
    }
}
