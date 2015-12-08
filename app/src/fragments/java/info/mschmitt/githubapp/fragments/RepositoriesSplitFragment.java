package info.mschmitt.githubapp.fragments;

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
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.databinding.RepositoriesSplitViewBinding;
import info.mschmitt.githubapp.modules.RepositoriesSplitModule;
import info.mschmitt.githubapp.presenters.RepositoriesSplitViewPresenter;
import info.mschmitt.githubapp.presenters.RepositoryListViewPresenter;
import info.mschmitt.githubapp.presenters.RepositoryPagerViewPresenter;


public class RepositoriesSplitFragment extends Fragment
        implements Presentable<RepositoriesSplitViewPresenter>,
        RepositoriesSplitViewPresenter.RepositoriesSplitSceneView,
        RepositoryListFragment.FragmentHost, RepositoryPagerFragment.FragmentHost {
    private static final String ARG_USERNAME = "arg_username";
    private FragmentHost mHost;
    private RepositoriesSplitViewPresenter mPresenter;
    private RepositoryListFragment mMasterFragment;
    private RepositoryPagerFragment mDetailsFragment;
    private Component mComponent;

    public static RepositoriesSplitFragment newInstance(String username) {
        RepositoriesSplitFragment fragment = new RepositoriesSplitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public RepositoryListViewPresenter getMasterPresenter() {
        return mMasterFragment.getPresenter();
    }

    @Override
    public RepositoryPagerViewPresenter getDetailsPresenter() {
        return mDetailsFragment.getPresenter();
    }

    @Override
    public void showDetailsView() {
        RepositoriesSplitViewBinding binding = getBinding();
        if (!isInSplitMode()) {
            binding.masterView.setVisibility(View.GONE);
        }
        binding.detailsView.setVisibility(View.VISIBLE);
    }

    @Override
    public RepositoriesSplitViewPresenter.ParentPresenter getParentPresenter() {
        return mHost.getPresenter();
    }

    @Override
    public boolean isInSplitMode() {
        return getResources().getBoolean(R.bool.split);
    }

    @Override
    public void hideDetailsView() {
        RepositoriesSplitViewBinding binding = getBinding();
        binding.masterView.setVisibility(View.VISIBLE);
        if (!isInSplitMode()) {
            binding.detailsView.setVisibility(View.GONE);
        }
    }

    private RepositoriesSplitViewBinding getBinding() {
        return DataBindingUtil.findBinding(getView());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = mHost.getSuperComponent(this)
                .plus(new RepositoriesSplitModule(this, getArguments().getString(ARG_USERNAME)));
        mComponent.inject(this);
        mPresenter.onCreate(savedInstanceState);
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
        return binding.contentView;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.onSave(outState);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
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

    @Override
    public RepositoriesSplitViewPresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(RepositoriesSplitViewPresenter presenter) {
        mPresenter = presenter;
    }

    public interface Component
            extends RepositoryListFragment.SuperComponent, RepositoryPagerFragment.SuperComponent {
        void inject(RepositoriesSplitFragment fragment);
    }

    public interface SuperComponent {
        Component plus(RepositoriesSplitModule module);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(RepositoriesSplitFragment fragment);

        RepositoriesSplitViewPresenter.ParentPresenter getPresenter();
    }
}
