package info.mschmitt.githubbrowser.ui.viewmodels;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.PropertyChangeRegistry;
import android.os.Bundle;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.BR;
import info.mschmitt.githubbrowser.android.databinding.DataBindingObservable;
import info.mschmitt.githubbrowser.domain.AnalyticsService;
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
    private final Map<Long, Integer> mRowIndexes = new HashMap<>();
    private final ObservableList<Repository> mRepositories = new ObservableArrayList<>();
    private final Observable<LinkedHashMap<Long, Repository>> mRepositoryMapObservable;
    private final BehaviorSubject<Long> mSelectedRepositorySubject;
    private final AnalyticsService mAnalyticsService;
    private final NavigationHandler mNavigationHandler;
    private final AdapterView.OnItemClickListener mOnRepositoryItemClickListener;
    private CompositeSubscription mSubscriptions;

    @Inject
    public RepositoryListViewModel(@RepositoryMapObservable
                                   Observable<LinkedHashMap<Long, Repository>>
                                               repositoryMapObservable,
                                   @SelectedRepositorySubject
                                   BehaviorSubject<Long> selectedRepositorySubject,
                                   AnalyticsService analyticsService,
                                   NavigationHandler navigationHandler) {
        mRepositoryMapObservable = repositoryMapObservable;
        mSelectedRepositorySubject = selectedRepositorySubject;
        mAnalyticsService = analyticsService;
        mNavigationHandler = navigationHandler;
        mOnRepositoryItemClickListener = (ignore1, ignore2, position, ignore3) -> {
            Repository repository = mRepositories.get(position);
            mSelectedRepositorySubject.onNext(repository.id());
        };
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
        mAnalyticsService.logScreenView(getClass().getName());
    }

    private void connectModel() {
        mSubscriptions.add(mRepositoryMapObservable.subscribe(this::onNextRepositoryMap));
        mSubscriptions.add(mSelectedRepositorySubject.subscribe(this::onNextSelectedRepository));
    }

    private void onNextRepositoryMap(LinkedHashMap<Long, Repository> repositoryMap) {
        mRowIndexes.clear();
        int i = 0;
        for (long id : repositoryMap.keySet()) {
            mRowIndexes.put(id, i++);
        }
        mRepositories.clear();
        mRepositories.addAll(repositoryMap.values());
        mPropertyChangeRegistry.notifyChange(this, BR.selection);
    }

    public void onSave(Bundle outState) {
    }

    public AdapterView.OnItemClickListener getOnRepositoryItemClickListener() {
        return mOnRepositoryItemClickListener;
    }

    public void onPause() {
        mSubscriptions.unsubscribe();
    }

    public void onNextSelectedRepository(long id) {
        if (id != -1) {
            mPropertyChangeRegistry.notifyChange(this, BR.selection);
        }
    }

    @Bindable
    public int getSelection() {
        Integer integer = mRowIndexes.get(mSelectedRepositorySubject.getValue());
        return integer != null ? integer : -1;
    }

    public interface NavigationHandler {
    }
}
