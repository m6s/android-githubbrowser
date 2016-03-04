package info.mschmitt.githubbrowser.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.android.InjectionUtils;
import info.mschmitt.githubbrowser.android.design.DividerItemDecoration;
import info.mschmitt.githubbrowser.databinding.RepositoryListViewBinding;
import info.mschmitt.githubbrowser.ui.adapters.RepositoryRecyclerViewAdapter;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositoryListViewModel;

/**
 * @author Matthias Schmitt
 */
public class RepositoryListViewFragment extends Fragment {
    @Inject RepositoryListViewModel mViewModel;
    private RepositoryRecyclerViewAdapter mAdapter;

    public static RepositoryListViewFragment newInstance() {
        return new RepositoryListViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectionUtils.getInjector(this, Injector.class).inject(this);
        mViewModel.onLoad(savedInstanceState);
        mAdapter = new RepositoryRecyclerViewAdapter(mViewModel.getRepositories(),
                mViewModel::onClick);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RepositoryListViewBinding binding =
                RepositoryListViewBinding.inflate(inflater, container, false);
        binding.repositoriesView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.repositoriesView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        binding.setViewModel(mViewModel);
        mAdapter.onCreateView(savedInstanceState);
        binding.setAdapter(mAdapter);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mViewModel.onSave(outState);
    }

    @Override
    public void onPause() {
        mViewModel.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        mAdapter.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mViewModel = null;
        mAdapter = null;
        super.onDestroy();
    }

    public interface Injector {
        void inject(RepositoryListViewFragment fragment);
    }
}
