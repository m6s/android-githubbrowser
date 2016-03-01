package info.mschmitt.githubbrowser.app.dagger;

import java.util.LinkedHashMap;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubbrowser.ui.fragments.RepositorySplitViewFragment;
import info.mschmitt.githubbrowser.ui.scopes.RepositorySplitViewScope;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositorySplitViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.qualifiers.RepositoryMapObservable;
import info.mschmitt.githubbrowser.ui.viewmodels.qualifiers.SelectedRepositorySubject;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Matthias Schmitt
 */
@Module
class RepositorySplitViewModule {
    @Provides
    @RepositorySplitViewScope
    @RepositoryMapObservable
    public Observable<LinkedHashMap<Long, info.mschmitt.githubbrowser.entities.Repository>>
    provideRepositoryMapObservable(
            RepositorySplitViewModel viewModel) {
        return viewModel.getRepositoryMapObservable();
    }

    @Provides
    @RepositorySplitViewScope
    @SelectedRepositorySubject
    public BehaviorSubject<Long> provideSelectedRepositorySubject(
            RepositorySplitViewModel viewModel) {
        return viewModel.getSelectedRepositorySubject();
    }

    @Provides
    @RepositorySplitViewScope
    public RepositorySplitViewFragment.Component provideComponent(
            RepositorySplitViewComponent component) {
        return component;
    }
}
