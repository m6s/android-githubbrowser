package info.mschmitt.githubapp.di;

import java.util.LinkedHashMap;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.di.qualifiers.RepositoryMapObservable;
import info.mschmitt.githubapp.di.qualifiers.SelectedRepositorySubject;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.viewmodels.RepositorySplitViewModel;
import rx.Observable;
import rx.subjects.Subject;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositorySplitViewModule {
    @Provides
    @RepositoryMapObservable
    public Observable<LinkedHashMap<Long, Repository>> provideRepositoryMapObservable(
            RepositorySplitViewModel viewModel) {
        return viewModel.getRepositoryMapObservable();
    }

    @Provides
    @SelectedRepositorySubject
    public Subject<Repository, Repository> provideSelectedRepositorySubject(
            RepositorySplitViewModel viewModel) {
        return viewModel.getSelectedRepositorySubject();
    }
}
