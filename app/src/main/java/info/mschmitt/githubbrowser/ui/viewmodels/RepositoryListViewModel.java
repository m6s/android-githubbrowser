package info.mschmitt.githubbrowser.ui.viewmodels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.PropertyChangeRegistry;
import android.os.Bundle;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.android.databinding.DataBindingObservable;
import info.mschmitt.githubbrowser.entities.Repository;
import info.mschmitt.githubbrowser.ui.scopes.RepositoryListViewScope;
import info.mschmitt.githubbrowser.ui.viewmodels.qualifiers.RepositoryMapObservable;
import info.mschmitt.githubbrowser.ui.viewmodels.qualifiers.SelectedRepositorySubject;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@RepositoryListViewScope
public class RepositoryListViewModel implements DataBindingObservable {
    private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();
    private final ObservableList<Repository> mRepositories = new ObservableArrayList<>();
    private final Observable<LinkedHashMap<Long, Repository>> mRepositoryMapObservable;
    private final BehaviorSubject<Long> mSelectedRepositorySubject;
    private final AnalyticsService mAnalyticsService;
    private CompositeSubscription mSubscriptions;

    @Inject
    public RepositoryListViewModel(@RepositoryMapObservable
                                   Observable<LinkedHashMap<Long, Repository>>
                                               repositoryMapObservable,
                                   @SelectedRepositorySubject
                                   BehaviorSubject<Long> selectedRepositorySubject,
                                   AnalyticsService analyticsService) {
        mRepositoryMapObservable = repositoryMapObservable;
        mSelectedRepositorySubject = selectedRepositorySubject;
        mAnalyticsService = analyticsService;
    }

    public void onClick(Repository repository) {
        mSelectedRepositorySubject.onNext(repository.id());
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mPropertyChangeRegistry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mPropertyChangeRegistry.remove(callback);
    }

    public ObservableList<Repository> getRepositories() {
        return mRepositories;
    }

    public void onLoad(Bundle savedState) {
    }

    public void onResume() {
        mSubscriptions = new CompositeSubscription();
        connectModel();
        mAnalyticsService.logRepositoryListShown();
    }

    private void connectModel() {
        mSubscriptions.add(mRepositoryMapObservable.subscribe(this::onNextRepositoryMap));
    }

    private void onNextRepositoryMap(LinkedHashMap<Long, Repository> repositoryMap) {
        mRepositories.clear();
        mRepositories.addAll(repositoryMap.values());
    }

    public void onSave(Bundle outState) {
    }

    public void onPause() {
        mSubscriptions.unsubscribe();
    }

    public interface AnalyticsService {
        void logRepositoryListShown();
    }
}
