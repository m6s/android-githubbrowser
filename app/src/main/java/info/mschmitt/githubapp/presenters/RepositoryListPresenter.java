package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.android.presentation.OnBackPressedListener;
import info.mschmitt.githubapp.entities.Repository;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoryListPresenter extends BaseObservable implements OnBackPressedListener {
    private static final String ARG_CURRENT_REPOSITORY_ID = "ARG_CURRENT_REPOSITORY_ID";
    private final CompositeSubscription mSubscriptions = new CompositeSubscription();
    private final Observable<LinkedHashMap<Long, Repository>> mRepositoryMapObservable;
    private final RepositoryListView mView;
    private final Map<Long, Integer> mRowIndexes = new HashMap<>();
    private final ObservableList<Repository> mRepositories = new ObservableArrayList<>();
    private final AdapterView.OnItemClickListener mOnRepositoryItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View listView, int position,
                                        long id) {
                    Repository repository = mRepositories.get(position);
                    mView.getParentPresenter()
                            .onRepositorySelected(RepositoryListPresenter.this, repository);
                }
            };
    private long mCurrentRepositoryId;
    public RepositoryListPresenter(RepositoryListView view,
                                   Observable<LinkedHashMap<Long, Repository>>
                                           repositoryMapObservable) {
        mView = view;
        mRepositoryMapObservable = repositoryMapObservable;
    }

    public ObservableList<Repository> getRepositories() {
        return mRepositories;
    }

    public void onCreate(Bundle savedState) {
        long lastRepositoryId =
                savedState != null ? savedState.getLong(ARG_CURRENT_REPOSITORY_ID) : -1;
        mSubscriptions.add(mRepositoryMapObservable.subscribe((repositoryMap) -> {
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

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public interface RepositoryListView {
        ParentPresenter getParentPresenter();
    }

    public interface ParentPresenter {
        void onRepositorySelected(Object sender, Repository repository);
    }
}
