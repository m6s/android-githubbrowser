package info.mschmitt.githubapp.application;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.adapters.RepositoryListAdapter;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.databinding.RepositoryListViewBinding;
import info.mschmitt.githubapp.modules.RepositoryListViewModule;
import info.mschmitt.githubapp.viewmodels.RepositoryListViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class RepositoryListViewFragment extends Fragment {
    private RepositoryListViewModel mViewModel;
    private FragmentHost mHost;
    private RepositoryListAdapter mAdapter;
    private NavigationManager mNavigationManager;

    public static RepositoryListViewFragment newInstance() {
        return new RepositoryListViewFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost.getSuperComponent(this).plus(new RepositoryListViewModule()).inject(this);
        mNavigationManager.onCreate(this);
        mViewModel.onCreate(savedInstanceState);
        mAdapter = new RepositoryListAdapter(getActivity(), mViewModel.getRepositories());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RepositoryListViewBinding binding =
                RepositoryListViewBinding.inflate(inflater, container, false);
        binding.setViewModel(mViewModel);
        mAdapter.onCreateView(savedInstanceState);
        binding.setAdapter(mAdapter);
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mViewModel.onSave(outState);
    }

    @Override
    public void onDestroyView() {
        mAdapter.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mViewModel.onDestroy();
        mNavigationManager.onDestroy(this);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        mHost = null;
        super.onDetach();
    }

    @Inject
    public void setNavigationManager(NavigationManager navigationManager) {
        mNavigationManager = navigationManager;
    }

    @Inject
    public void setViewModel(RepositoryListViewModel viewModel) {
        mViewModel = viewModel;
    }

    public interface Component {
        void inject(RepositoryListViewFragment fragment);
    }

    public interface SuperComponent {
        Component plus(RepositoryListViewModule module);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(RepositoryListViewFragment fragment);
    }
}
