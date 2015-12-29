package info.mschmitt.githubapp.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.domain.UserDownloader;
import info.mschmitt.githubapp.domain.Validator;
import info.mschmitt.githubapp.utils.LoadingProgressManager;
import info.mschmitt.githubapp.viewmodels.UsernameViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
public class UsernameViewModule {
    @Provides
    @Singleton
    public UsernameViewModel provideViewModel(Validator validator, UserDownloader userDownloader,
                                              AnalyticsService analyticsService,
                                              LoadingProgressManager loadingProgressManager,
                                              NavigationManager navigationManager) {
        return new UsernameViewModel(validator, userDownloader, analyticsService,
                loadingProgressManager, navigationManager);
    }
}
