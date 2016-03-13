package info.mschmitt.githubbrowser.ui.adapters;

import android.databinding.Bindable;
import android.databinding.PropertyChangeRegistry;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.BR;
import info.mschmitt.githubbrowser.android.databinding.DataBindingObservable;
import info.mschmitt.githubbrowser.android.databinding.RecyclerViewAdapterOnListChangedCallback;
import info.mschmitt.githubbrowser.databinding.RepositoryListItemViewBinding;
import info.mschmitt.githubbrowser.entities.Repository;
import info.mschmitt.githubbrowser.ui.scopes.RepositoryListViewScope;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositoryListViewModel;

/**
 * TODO Highlight selected item http://stackoverflow.com/q/27194044/2317680
 *
 * @author Matthias Schmitt
 */
@RepositoryListViewScope
public class RepositoryRecyclerViewAdapter
        extends RecyclerView.Adapter<RepositoryRecyclerViewAdapter.ItemViewHolder> {
    private final RecyclerViewAdapterOnListChangedCallback<Repository> mCallback =
            new RecyclerViewAdapterOnListChangedCallback<>(this);
    private final RepositoryListViewModel mViewModel;

    @Inject
    public RepositoryRecyclerViewAdapter(RepositoryListViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder viewHolder = ItemViewHolder.inflateView(parent);
        viewHolder.setOnRepositoryClickListener(mViewModel::onClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Repository repository = mViewModel.getRepositories().get(position);
        holder.setRepository(repository);
    }

    @Override
    public int getItemCount() {
        return mViewModel.getRepositories().size();
    }

    public void onCreateView(Bundle savedInstanceState) {
        mViewModel.getRepositories().addOnListChangedCallback(mCallback);
    }

    public void onDestroyView() {
        mViewModel.getRepositories().removeOnListChangedCallback(mCallback);
    }

    public interface OnRepositoryClickListener {
        void onRepositoryClick(Repository repository);
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    public static class ItemViewHolder extends ViewHolder implements DataBindingObservable {
        private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();
        private OnRepositoryClickListener mOnRepositoryClickListener;
        private Repository mRepository;

        public ItemViewHolder(RepositoryListItemViewBinding binding) {
            super(binding.getRoot());
            binding.setViewHolder(this);
        }

        public static ItemViewHolder inflateView(ViewGroup parent) {
            RepositoryListItemViewBinding binding = RepositoryListItemViewBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ItemViewHolder(binding);
        }

        public void setOnRepositoryClickListener(OnRepositoryClickListener listener) {
            mOnRepositoryClickListener = listener;
        }

        @Bindable
        public String getRepositoryName() {
            return mRepository.name();
        }

        public void setRepository(Repository repository) {
            mRepository = repository;
            mPropertyChangeRegistry.notifyChange(this, BR.repositoryName);
        }

        public void onClick(View view) {
            mOnRepositoryClickListener.onRepositoryClick(mRepository);
        }

        @Override
        public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
            mPropertyChangeRegistry.add(callback);
        }

        @Override
        public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
            mPropertyChangeRegistry.remove(callback);
        }
    }
}
