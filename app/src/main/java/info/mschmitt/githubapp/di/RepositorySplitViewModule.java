package info.mschmitt.githubapp.di;

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
    public RepositorySplitViewModel provideViewModel(RepositoryDownloader repositoryDownloader,
                                                     AnalyticsService analyticsService,
                                                     LoadingProgressManager loadingProgressManager,
                                                     NavigationManager navigationManager) {
        return new RepositorySplitViewModel(repositoryDownloader, analyticsService,
                loadingProgressManager, navigationManager);
    }

    @Provides
    @Singleton
    @Named("RepositoryMap")
    public Observable<LinkedHashMap<Long, Repository>> provideRepositoryMap(
            RepositorySplitViewModel viewModel) {
        return viewModel.getRepositoryMap();
    }

    @Provides
    @Singleton
    @Named("SelectedRepository")
    public Subject<Repository, Repository> provideSelectedRepository(
            RepositorySplitViewModel viewModel) {
        return viewModel.getSelectedRepository();
    }
}
