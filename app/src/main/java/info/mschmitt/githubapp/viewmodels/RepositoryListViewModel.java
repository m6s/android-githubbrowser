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

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.entities.Repository;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoryListViewModel extends BaseObservable {
    private static final String ARG_CURRENT_REPOSITORY_ID = "ARG_CURRENT_REPOSITORY_ID";
    private final Map<Long, Integer> mRowIndexes = new HashMap<>();
    private final ObservableList<Repository> mRepositories = new ObservableArrayList<>();
    private final NavigationHandler mNavigationHandler;
    private final AdapterView.OnItemClickListener mOnRepositoryItemClickListener;
    private CompositeSubscription mSubscriptions;
    private long mCurrentRepositoryId;

    public RepositoryListViewModel(NavigationHandler navigationHandler) {
        mNavigationHandler = navigationHandler;
        mOnRepositoryItemClickListener = (ignore1, ignore2, position, ignore3) -> {
            Repository repository = mRepositories.get(position);
            mNavigationHandler.showRepository(repository);
        };
    }

    public ObservableList<Repository> getRepositories() {
        return mRepositories;
    }

    public void onCreate(Bundle savedState) {
        mSubscriptions = new CompositeSubscription();
        long lastRepositoryId =
                savedState != null ? savedState.getLong(ARG_CURRENT_REPOSITORY_ID) : -1;
        mSubscriptions.add(mNavigationHandler.getRepositoryMap().subscribe((repositoryMap) -> {
            mRowIndexes.clear();
            int i = 0;
            for (long id : repositoryMap.keySet()) {
                mRowIndexes.put(id, i++);
            }
            mRepositories.clear();
            mRepositories.addAll(repositoryMap.values());
            if (lastRepositoryId != -1) {
                setCurrentRepositoryId(lastRepositoryId);
            }
        }));
        mSubscriptions.add(mNavigationHandler.getRepository().subscribe(this::selectRepository));
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

    public void onDestroy() {
        mSubscriptions.unsubscribe();
    }

    public void selectRepository(Repository repository) {
        setCurrentRepositoryId(repository.getId());
    }

    @Bindable
    public Integer getSelection() {
        Integer integer = mRowIndexes.get(mCurrentRepositoryId);
        return integer != null ? integer : -1;
    }

    public interface NavigationHandler {
        void showRepository(Repository repository);

        Observable<LinkedHashMap<Long, Repository>> getRepositoryMap();

        Observable<Repository> getRepository();
    }
}
