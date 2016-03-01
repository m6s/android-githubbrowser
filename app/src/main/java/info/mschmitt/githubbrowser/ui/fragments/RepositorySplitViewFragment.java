package info.mschmitt.githubbrowser.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.R;
import info.mschmitt.githubbrowser.android.presentation.FragmentUtils;
import info.mschmitt.githubbrowser.databinding.RepositorySplitViewBinding;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositorySplitViewModel;

/**
 * @author Matthias Schmitt
 */
public class RepositorySplitViewFragment extends Fragment
        implements RepositoryListViewFragment.FragmentHost,
        RepositoryPagerViewFragment.FragmentHost {
    private static final String ARG_USERNAME = "arg_username";
    @Inject Component mComponent;
    @Inject RepositorySplitViewModel mViewModel;

    public static RepositorySplitViewFragment newInstance(String username) {
        RepositorySplitViewFragment fragment = new RepositorySplitViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.getParent(this, FragmentHost.class).inject(this);
        mViewModel.onLoad(getArguments().getString(ARG_USERNAME), savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RepositorySplitViewBinding binding =
                RepositorySplitViewBinding.inflate(inflater, container, false);
        binding.setViewModel(mViewModel);
        if (getChildFragmentManager().findFragmentById(binding.masterView.getId()) == null) {
            getChildFragmentManager().beginTransaction()
                    .add(binding.masterView.getId(), RepositoryListViewFragment.newInstance())
                    .commit();
        }
        if (getChildFragmentManager().findFragmentById(binding.detailsView.getId()) == null) {
            getChildFragmentManager().beginTransaction()
                    .add(binding.detailsView.getId(), RepositoryPagerViewFragment.newInstance())
                    .commit();
        }
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
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
    public void onDestroy() {
        mComponent = null;
        mViewModel = null;
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                return mViewModel.onAboutOptionsItemSelected();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean hideDetailsView() {
        return mViewModel.onHideDetailsView();
    }

    @Override
    public void inject(RepositoryPagerViewFragment fragment) {
        mComponent.inject(fragment);
    }

    @Override
    public void inject(RepositoryListViewFragment fragment) {
        mComponent.inject(fragment);
    }

    public interface Component {
        void inject(RepositoryPagerViewFragment fragment);

        void inject(RepositoryListViewFragment fragment);
    }

    public interface FragmentHost {
        void inject(RepositorySplitViewFragment fragment);
    }
}
