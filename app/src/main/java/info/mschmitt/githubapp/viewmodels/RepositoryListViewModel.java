package info.mschmitt.githubapp.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.di.RepositoryListViewScope;
import info.mschmitt.githubapp.di.qualifiers.RepositoryMapObservable;
import info.mschmitt.githubapp.di.qualifiers.SelectedRepositorySubject;
import info.mschmitt.githubapp.entities.Repository;
import rx.Observable;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@RepositoryListViewScope
public class RepositoryListViewModel extends BaseObservable {
    private static final String ARG_CURRENT_REPOSITORY_ID = "ARG_CURRENT_REPOSITORY_ID";
    private final Map<Long, Integer> mRowIndexes = new HashMap<>();
    private final ObservableList<Repository> mRepositories = new ObservableArrayList<>();
    private final Observable<LinkedHashMap<Long, Repository>> mRepositoryMapObservable;
    private final Subject<Repository, Repository> mSelectedRepositorySubject;
    private final NavigationHandler mNavigationHandler;
    private final AdapterView.OnItemClickListener mOnRepositoryItemClickListener;
    private CompositeSubscription mSubscriptions;
    private long mCurrentRepositoryId;

    @Inject
    public RepositoryListViewModel(@RepositoryMapObservable
                                   Observable<LinkedHashMap<Long, Repository>>
                                               repositoryMapObservable,
                                   @SelectedRepositorySubject
                                   Subject<Repository, Repository> selectedRepositorySubject,
                                   NavigationHandler navigationHandler) {
        mRepositoryMapObservable = repositoryMapObservable;
        mSelectedRepositorySubject = selectedRepositorySubject;
        mNavigationHandler = navigationHandler;
        mOnRepositoryItemClickListener = (ignore1, ignore2, position, ignore3) -> {
            Repository repository = mRepositories.get(position);
            mSelectedRepositorySubject.onNext(repository);
        };
    }

    public ObservableList<Repository> getRepositories() {
        return mRepositories;
    }

    public void onLoad(Bundle savedState) {
        mCurrentRepositoryId =
                savedState != null ? savedState.getLong(ARG_CURRENT_REPOSITORY_ID) : -1;
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
        mSubscriptions.add(mSelectedRepositorySubject.subscribe(this::selectRepository));
    }

    private void setCurrentRepositoryId(long repositoryId) {
        mCurrentRepositoryId = repositoryId;
        notifyPropertyChanged(BR.selection);
    }

    public void onSave(Bundle outState) {
        outState.putLong(ARG_CURRENT_REPOSITORY_ID, mCurrentRepositoryId);
    }

    public AdapterView.OnItemClickListener getOnRepositoryItemClickListener() {
        return mOnRepositoryItemClickListener;
    }

    public void onPause() {
        mSubscriptions.unsubscribe();
    }

    public void selectRepository(Repository repository) {
        setCurrentRepositoryId(repository.id());
    }

    @Bindable
    public Integer getSelection() {
        Integer integer = mRowIndexes.get(mCurrentRepositoryId);
        return integer != null ? integer : -1;
    }

    public interface NavigationHandler {
    }
}
