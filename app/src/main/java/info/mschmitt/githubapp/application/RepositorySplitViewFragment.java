package info.mschmitt.githubapp.application;

import android.content.Context;
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

import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.dagger.RepositorySplitViewModule;
import info.mschmitt.githubapp.databinding.RepositorySplitViewBinding;
import info.mschmitt.githubapp.viewmodels.RepositorySplitViewModel;

public class RepositorySplitViewFragment extends Fragment
        implements RepositoryListViewFragment.FragmentHost,
        RepositoryPagerViewFragment.FragmentHost {
    private static final String ARG_USERNAME = "arg_username";
    private FragmentHost mHost;
    private RepositorySplitViewModel mViewModel;
    private Component mComponent;

    public static RepositorySplitViewFragment newInstance(String username) {
        RepositorySplitViewFragment fragment = new RepositorySplitViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = mHost.getComponent().plus(new RepositorySplitViewModule());
        mComponent.inject(this);
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
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        mHost = null;
        super.onDetach();
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

    @Override
    public Component getComponent() {
        return mComponent;
    }

    @Inject
    public void setViewModel(RepositorySplitViewModel viewModel) {
        mViewModel = viewModel;
    }

    public void showDetailsView() {
        mViewModel.onShowDetailsView();
    }

    public boolean hideDetailsView() {
        return mViewModel.onHideDetailsView();
    }

    public interface Component extends RepositoryListViewFragment.SuperComponent,
            RepositoryPagerViewFragment.SuperComponent {
        void inject(RepositorySplitViewFragment fragment);
    }

    public interface SuperComponent {
        Component plus(RepositorySplitViewModule module);
    }

    public interface FragmentHost {
        SuperComponent getComponent();
    }
}
