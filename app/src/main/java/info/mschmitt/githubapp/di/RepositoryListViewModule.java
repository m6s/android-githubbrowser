package info.mschmitt.githubapp.di;

import java.util.LinkedHashMap;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.application.NavigationManager;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.viewmodels.RepositoryListViewModel;
import rx.Observable;
import rx.subjects.Subject;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryListViewModule {
    @Provides
    @Singleton
    public RepositoryListViewModel provideViewModel(
            @Named("RepositoryMap") Observable<LinkedHashMap<Long, Repository>> repositoryMap,
            @Named("SelectedRepository") Subject<Repository, Repository> selectedRepository,
            NavigationManager navigationManager) {
        return new RepositoryListViewModel(repositoryMap, selectedRepository, navigationManager);
    }
}
