package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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
    private final CompositeSubscription mSubscriptions = new CompositeSubscription();
    private final Observable<List<Repository>> mRepositories;
    private RepositoryListView mView;
    private final AdapterView.OnItemClickListener mOnRepositoryItemClickListener =
            (parent, view, position, id) -> {
                Repository repository = mView.getAdapter().getItem(position);
                mView.getParentPresenter()
                        .onRepositorySelected(RepositoryListViewPresenter.this, repository);
            };
    private Map<Repository, Integer> mRepositoryIndexes = new HashMap<>();
    private int mSelection;

    @Inject
    public RepositoryListViewPresenter(Observable<List<Repository>> repositories) {
        mRepositories = repositories;
    }

    public void onCreate(RepositoryListView view, Bundle savedState) {
        mView = view;
        mSubscriptions.add(mRepositories.subscribe((repositories) -> {
            mView.getAdapter().clear();
            mView.getAdapter().addAll(repositories);
            mRepositoryIndexes.clear();
            int i = 0;
            for (Repository repository : repositories) {
                mRepositoryIndexes.put(repository, i++);
            }
            mView.getAdapter().notifyDataSetChanged();
        }));
    }

    public void onSave(Bundle outState) {

    }

    public AdapterView.OnItemClickListener getOnRepositoryItemClickListener() {
        return mOnRepositoryItemClickListener;
    }

    public void onDestroy() {
        mSubscriptions.unsubscribe();
    }

    public void selectRepository(Repository repository) {
        setSelection(mRepositoryIndexes.get(repository));
    }

    @Bindable
    public Integer getSelection() {
        return mSelection;
    }

    public void setSelection(Integer selection) {
        mSelection = selection;
        notifyPropertyChanged(BR.selection);
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
