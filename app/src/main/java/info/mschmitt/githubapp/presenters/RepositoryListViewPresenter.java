package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.adapters.RepositoryListAdapter;
import info.mschmitt.githubapp.android.presentation.OnBackPressedListener;
import info.mschmitt.githubapp.entities.Repository;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoryListViewPresenter extends BaseObservable implements OnBackPressedListener {
    private static final String ARG_CURRENT_REPOSITORY_ID = "ARG_CURRENT_REPOSITORY_ID";
    private final CompositeSubscription mSubscriptions = new CompositeSubscription();
    private final Observable<LinkedHashMap<Long, Repository>> mRepositories;
    private final RepositoryListView mView;
    private final AdapterView.OnItemClickListener mOnRepositoryItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View listView, int position,
                                        long id) {
                    Repository repository = mView.getAdapter().getItem(position);
                    mView.getParentPresenter()
                            .onRepositorySelected(RepositoryListViewPresenter.this, repository);
                }
            };
    private final Map<Long, Integer> mRowIndexes = new HashMap<>();
    private long mCurrentRepositoryId;

    public RepositoryListViewPresenter(RepositoryListView view,
                                       Observable<LinkedHashMap<Long, Repository>> repositories) {
        mView = view;
        mRepositories = repositories;
    }

    public void onCreate(Bundle savedState) {
        long lastRepositoryId =
                savedState != null ? savedState.getLong(ARG_CURRENT_REPOSITORY_ID) : -1;
        mSubscriptions.add(mRepositories.subscribe((repositories) -> {
            mRowIndexes.clear();
            int i = 0;
            for (long id : repositories.keySet()) {
                mRowIndexes.put(id, i++);
            }
            mView.getAdapter().clear();
            mView.getAdapter().addAll(repositories.values());
            mView.getAdapter().notifyDataSetChanged();
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

    public RepositoryListAdapter getAdapter() {
        return mView.getAdapter();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public interface RepositoryListView {
        RepositoryListAdapter getAdapter();

        ParentPresenter getParentPresenter();
    }

    public interface ParentPresenter {
        void onRepositorySelected(Object sender, Repository repository);
    }
}
