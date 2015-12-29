package info.mschmitt.githubapp.app;

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
import info.mschmitt.githubapp.databinding.RepositoriesSplitViewBinding;
import info.mschmitt.githubapp.modules.RepositorySplitModule;
import info.mschmitt.githubapp.presenters.RepositorySplitViewModel;


public class RepositorySplitFragment extends Fragment
        implements RepositoryListFragment.FragmentHost,
        RepositoryPagerFragment.FragmentHost {
    private static final String ARG_USERNAME = "arg_username";
    private FragmentHost mHost;
    private RepositorySplitViewModel mPresenter;
    private RepositoryListFragment mMasterFragment;
    private RepositoryPagerFragment mDetailsFragment;
    private Component mComponent;
    private NavigationManager mNavigationManager;

    public static RepositorySplitFragment newInstance(String username) {
        RepositorySplitFragment fragment = new RepositorySplitFragment();
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
        mComponent = mHost.getSuperComponent(this).plus(new RepositorySplitModule());
        mComponent.inject(this);
        mNavigationManager.onCreate(this);
        mPresenter.onCreate(getArguments().getString(ARG_USERNAME), savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RepositoriesSplitViewBinding binding =
                RepositoriesSplitViewBinding.inflate(inflater, container, false);
        binding.setPresenter(mPresenter);
        mMasterFragment = (RepositoryListFragment) getChildFragmentManager()
                .findFragmentById(binding.masterView.getId());
        if (mMasterFragment == null) {
            mMasterFragment = RepositoryListFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .add(binding.masterView.getId(), mMasterFragment).commit();
        }
        mDetailsFragment = (RepositoryPagerFragment) getChildFragmentManager()
                .findFragmentById(binding.detailsView.getId());
        if (mDetailsFragment == null) {
            mDetailsFragment = RepositoryPagerFragment.newInstance();
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
            if (mPresenter.isDetailsViewActive()) {
                getBinding().masterView.setVisibility(View.GONE);
                getBinding().detailsView.setVisibility(View.VISIBLE);
            } else {
                getBinding().masterView.setVisibility(View.VISIBLE);
                getBinding().detailsView.setVisibility(View.GONE);
            }
        }
    }

    private RepositoriesSplitViewBinding getBinding() {
        return DataBindingUtil.findBinding(getView());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.onSave(outState);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
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
    public RepositoryListFragment.SuperComponent getSuperComponent(
            RepositoryListFragment fragment) {
        return mComponent;
    }

    @Override
    public RepositoryPagerFragment.SuperComponent getSuperComponent(
            RepositoryPagerFragment fragment) {
        return mComponent;
    }

    @Inject
    public void setPresenter(RepositorySplitViewModel presenter) {
        mPresenter = presenter;
    }

    @Inject
    public void setNavigationManager(NavigationManager navigationManager) {
        mNavigationManager = navigationManager;
    }

    public interface Component
            extends RepositoryListFragment.SuperComponent, RepositoryPagerFragment.SuperComponent {
        void inject(RepositorySplitFragment fragment);
    }

    public interface SuperComponent {
        Component plus(RepositorySplitModule module);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(RepositorySplitFragment fragment);
    }
}
