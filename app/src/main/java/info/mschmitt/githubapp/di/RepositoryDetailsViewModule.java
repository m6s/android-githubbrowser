package info.mschmitt.githubapp.di;

import java.util.LinkedHashMap;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.viewmodels.RepositoryDetailsViewModel;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryDetailsViewModule {
    @Provides
    @Singleton
    public RepositoryDetailsViewModel provideViewModel(
            @Named("RepositoryMap") Observable<LinkedHashMap<Long, Repository>> repositoryMap,
            AnalyticsService analyticsService, NavigationManager navigationManager) {
        return new RepositoryDetailsViewModel(repositoryMap, analyticsService, navigationManager);
    }
}
