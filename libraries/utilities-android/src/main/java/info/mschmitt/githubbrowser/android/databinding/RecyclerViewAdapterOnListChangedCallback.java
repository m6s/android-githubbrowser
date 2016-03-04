package info.mschmitt.githubbrowser.android.databinding;

import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;

/**
 * @author Matthias Schmitt
 */
public class RecyclerViewAdapterOnListChangedCallback<T>
        extends ObservableList.OnListChangedCallback<ObservableList<T>> {
    private final RecyclerView.Adapter<? extends RecyclerView.ViewHolder> mAdapter;

    public RecyclerViewAdapterOnListChangedCallback(
            RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onChanged(ObservableList<T> sender) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition,
                                 int itemCount) {
        if (itemCount == 1) {
            mAdapter.notifyItemMoved(fromPosition, toPosition);
        } else {
            onChanged(sender);
        }
    }

    @Override
    public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }
}
