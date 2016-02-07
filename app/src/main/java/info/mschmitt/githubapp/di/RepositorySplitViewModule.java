package info.mschmitt.githubapp.di;

import android.content.res.Resources;

import java.util.LinkedHashMap;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.NavigationManager;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.domain.RepositoryDownloader;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.utils.LoadingProgressManager;
import info.mschmitt.githubapp.viewmodels.RepositorySplitViewModel;
import rx.Observable;
import rx.subjects.Subject;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositorySplitViewModule {
    @Provides
    @Singleton
    public RepositorySplitViewModel provideViewModel(@Named("Resources") Resources resources,
                                                     RepositoryDownloader repositoryDownloader,
                                                     AnalyticsService analyticsService,
                                                     LoadingProgressManager loadingProgressManager,
                                                     NavigationManager navigationManager) {
        return new RepositorySplitViewModel(resources, repositoryDownloader, analyticsService,
                loadingProgressManager, navigationManager);
    }

    @Provides
    @Singleton
    @Named("RepositoryMapObservable")
    public Observable<LinkedHashMap<Long, Repository>> provideRepositoryMapObservable(
            RepositorySplitViewModel viewModel) {
        return viewModel.getRepositoryMapObservable();
    }

    @Provides
    @Singleton
    @Named("SelectedRepositorySubject")
    public Subject<Repository, Repository> provideSelectedRepositorySubject(
            RepositorySplitViewModel viewModel) {
        return viewModel.getSelectedRepositorySubject();
    }
}
