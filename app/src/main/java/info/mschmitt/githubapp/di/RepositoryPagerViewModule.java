package info.mschmitt.githubapp.di;

import java.util.LinkedHashMap;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.viewmodels.RepositoryPagerViewModel;
import rx.Observable;
import rx.subjects.Subject;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryPagerViewModule {
    @Provides
    @Singleton
    public RepositoryPagerViewModel provideViewModel(@Named("RepositoryMapObservable")
                                                     Observable<LinkedHashMap<Long, Repository>>
                                                                 repositoryMapObservable,
                                                     @Named("SelectedRepositorySubject")
                                                     Subject<Repository, Repository>
                                                             selectedRepositorySubject,
                                                     AnalyticsService analyticsService,
                                                     NavigationManager navigationManager) {
        return new RepositoryPagerViewModel(repositoryMapObservable, selectedRepositorySubject,
                analyticsService, navigationManager);
    }
}
