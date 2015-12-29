package info.mschmitt.githubapp.application;

import android.content.Context;
import android.databinding.DataBindingUtil;
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
import info.mschmitt.githubapp.databinding.RepositorySplitViewBinding;
import info.mschmitt.githubapp.di.RepositorySplitViewModule;
import info.mschmitt.githubapp.viewmodels.RepositorySplitViewModel;

public class RepositorySplitViewFragment extends Fragment
        implements RepositoryListViewFragment.FragmentHost,
        RepositoryPagerViewFragment.FragmentHost {
    private static final String ARG_USERNAME = "arg_username";
    private static final String STATE_DETAILS_VIEW_ACTIVE = "STATE_DETAILS_VIEW_ACTIVE";
    private FragmentHost mHost;
    private RepositorySplitViewModel mViewModel;
    private RepositoryListViewFragment mMasterFragment;
    private RepositoryPagerViewFragment mDetailsFragment;
    private Component mComponent;
    private NavigationManager mNavigationManager;
    private boolean mDetailsViewActive;

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
        if (savedInstanceState != null) {
            mDetailsViewActive = savedInstanceState.getBoolean(STATE_DETAILS_VIEW_ACTIVE);
        }
        mComponent = mHost.getSuperComponent(this).plus(new RepositorySplitViewModule());
        mComponent.inject(this);
        mNavigationManager.onCreate(this);
        mViewModel.onCreate(getArguments().getString(ARG_USERNAME), savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RepositorySplitViewBinding binding =
                RepositorySplitViewBinding.inflate(inflater, container, false);
        binding.setViewModel(mViewModel);
        mMasterFragment = (RepositoryListViewFragment) getChildFragmentManager()
                .findFragmentById(binding.masterView.getId());
        if (mMasterFragment == null) {
            mMasterFragment = RepositoryListViewFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .add(binding.masterView.getId(), mMasterFragment).commit();
        }
        mDetailsFragment = (RepositoryPagerViewFragment) getChildFragmentManager()
                .findFragmentById(binding.detailsView.getId());
        if (mDetailsFragment == null) {
            mDetailsFragment = RepositoryPagerViewFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .add(binding.detailsView.getId(), mDetailsFragment).commit();
        }
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        return binding.getRoot();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (getResources().getBoolean(R.bool.split)) {
            getBinding().masterView.setVisibility(View.VISIBLE);
            getBinding().detailsView.setVisibility(View.VISIBLE);
        } else {
            if (mDetailsViewActive) {
                getBinding().masterView.setVisibility(View.GONE);
                getBinding().detailsView.setVisibility(View.VISIBLE);
            } else {
                getBinding().masterView.setVisibility(View.VISIBLE);
                getBinding().detailsView.setVisibility(View.GONE);
            }
        }
    }

    private RepositorySplitViewBinding getBinding() {
        return DataBindingUtil.findBinding(getView());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mViewModel.onSave(outState);
        outState.putBoolean(STATE_DETAILS_VIEW_ACTIVE, mDetailsViewActive);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public RepositoryListViewFragment.SuperComponent getSuperComponent(
            RepositoryListViewFragment fragment) {
        return mComponent;
    }

    @Override
    public RepositoryPagerViewFragment.SuperComponent getSuperComponent(
            RepositoryPagerViewFragment fragment) {
        return mComponent;
    }

    @Inject
    public void setViewModel(RepositorySplitViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Inject
    public void setNavigationManager(NavigationManager navigationManager) {
        mNavigationManager = navigationManager;
    }

    public void showDetailsView() {
        if (mDetailsViewActive) {
            return;
        }
        RepositorySplitViewBinding binding = DataBindingUtil.getBinding(getView());
        assert binding != null;
        if (!isInSplitMode()) {
            binding.masterView.setVisibility(View.GONE);
        }
        binding.detailsView.setVisibility(View.VISIBLE);
        mDetailsViewActive = true;
    }

    private boolean isInSplitMode() {
        return getResources().getBoolean(R.bool.split);
    }

    public boolean hideDetailsView() {
        if (!mDetailsViewActive || isInSplitMode()) {
            return false;
        }
        RepositorySplitViewBinding binding = DataBindingUtil.getBinding(getView());
        assert binding != null;
        binding.masterView.setVisibility(View.VISIBLE);
        if (!isInSplitMode()) {
            binding.detailsView.setVisibility(View.GONE);
        }
        mDetailsViewActive = false;
        return true;
    }

    public interface Component extends RepositoryListViewFragment.SuperComponent,
            RepositoryPagerViewFragment.SuperComponent {
        void inject(RepositorySplitViewFragment fragment);
    }

    public interface SuperComponent {
        Component plus(RepositorySplitViewModule module);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(RepositorySplitViewFragment fragment);
    }
}
