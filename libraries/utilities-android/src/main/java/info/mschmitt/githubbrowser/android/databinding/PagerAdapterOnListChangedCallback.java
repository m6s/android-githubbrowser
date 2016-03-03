package info.mschmitt.githubbrowser.android.databinding;

import android.databinding.ObservableList;
import android.support.v4.view.PagerAdapter;

/**
 * @author Matthias Schmitt
 */
public class PagerAdapterOnListChangedCallback<T>
        extends ObservableList.OnListChangedCallback<ObservableList<T>> {
    private final PagerAdapter mAdapter;

    public PagerAdapterOnListChangedCallback(PagerAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onChanged(ObservableList<T> sender) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition,
                                 int itemCount) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
        mAdapter.notifyDataSetChanged();
    }
}
