package info.mschmitt.githubapp.viewmodels;

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

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.android.presentation.DataBindingObservable;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.scopes.RepositoryListViewScope;
import info.mschmitt.githubapp.viewmodels.qualifiers.RepositoryMapObservable;
import info.mschmitt.githubapp.viewmodels.qualifiers.SelectedRepositorySubject;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@RepositoryListViewScope
public class RepositoryListViewModel implements DataBindingObservable {
    private static final String STATE_CURRENT_REPOSITORY_ID = "STATE_CURRENT_REPOSITORY_ID";
    private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();
    private final Map<Long, Integer> mRowIndexes = new HashMap<>();
    private final ObservableList<Repository> mRepositories = new ObservableArrayList<>();
    private final Observable<LinkedHashMap<Long, Repository>> mRepositoryMapObservable;
    private final BehaviorSubject<Long> mSelectedRepositoryIdSubject;
    private final NavigationHandler mNavigationHandler;
    private final AdapterView.OnItemClickListener mOnRepositoryItemClickListener;
    private CompositeSubscription mSubscriptions;
    private long mCurrentRepositoryId;

    @Inject
    public RepositoryListViewModel(@RepositoryMapObservable
                                   Observable<LinkedHashMap<Long, Repository>>
                                               repositoryMapObservable,
                                   @SelectedRepositorySubject
                                   BehaviorSubject<Long> selectedRepositoryIdSubject,
                                   NavigationHandler navigationHandler) {
        mRepositoryMapObservable = repositoryMapObservable;
        mSelectedRepositoryIdSubject = selectedRepositoryIdSubject;
        mNavigationHandler = navigationHandler;
        mOnRepositoryItemClickListener = (ignore1, ignore2, position, ignore3) -> {
            Repository repository = mRepositories.get(position);
            mSelectedRepositoryIdSubject.onNext(repository.id());
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
        mCurrentRepositoryId =
                savedState != null ? savedState.getLong(STATE_CURRENT_REPOSITORY_ID) : -1;
    }

    public void onResume() {
        mSubscriptions = new CompositeSubscription();
        mSubscriptions.add(mRepositoryMapObservable.subscribe((repositoryMap) -> {
            mRowIndexes.clear();
            int i = 0;
            for (long id : repositoryMap.keySet()) {
                mRowIndexes.put(id, i++);
            }
            mRepositories.clear();
            mRepositories.addAll(repositoryMap.values());
            if (mCurrentRepositoryId != -1) {
                setCurrentRepositoryId(mCurrentRepositoryId);
            }
        }));
        mSubscriptions.add(mSelectedRepositoryIdSubject.subscribe(this::onNextRepositorySelected));
    }

    private void setCurrentRepositoryId(long repositoryId) {
        mCurrentRepositoryId = repositoryId;
        mPropertyChangeRegistry.notifyChange(this, BR.selection);
    }

    public void onSave(Bundle outState) {
        outState.putLong(STATE_CURRENT_REPOSITORY_ID, mCurrentRepositoryId);
    }

    public AdapterView.OnItemClickListener getOnRepositoryItemClickListener() {
        return mOnRepositoryItemClickListener;
    }

    public void onPause() {
        mSubscriptions.unsubscribe();
    }

    public void onNextRepositorySelected(long id) {
        if (id != -1) {
            setCurrentRepositoryId(id);
        }
    }

    @Bindable
    public Integer getSelection() {
        Integer integer = mRowIndexes.get(mCurrentRepositoryId);
        return integer != null ? integer : -1;
    }

    public interface NavigationHandler {
    }
}
