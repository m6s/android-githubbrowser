package info.mschmitt.githubapp.fragments;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.databinding.RepositoriesSplitViewBinding;
import info.mschmitt.githubapp.presenters.RepositoriesSplitViewPresenter;
import info.mschmitt.githubapp.presenters.RepositoryListViewPresenter;
import info.mschmitt.githubapp.presenters.RepositoryPagerViewPresenter;


public class RepositoriesSplitFragment extends Fragment
        implements Presentable<RepositoriesSplitViewPresenter>, RepositoryListFragment.FragmentHost,
        RepositoryPagerFragment.FragmentHost {
    private FragmentHost mHost;
    private RepositoriesSplitViewPresenter mPresenter;
    private RepositoryListFragment mMasterFragment;
    private RepositoryPagerFragment mDetailsFragment;
    private RepositoriesSplitViewPresenter.RepositoriesSplitView mRepositoriesSplitView =
            new RepositoriesSplitViewPresenter.RepositoriesSplitView() {
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
            };

    public static RepositoriesSplitFragment newInstance() {
        return new RepositoriesSplitFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost.getSuperComponent(this).inject(this);
        SuperComponent superComponent = mHost.getSuperComponent(this);
        superComponent.inject(this);
        mPresenter.onCreate(mRepositoriesSplitView, savedInstanceState);
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

    private RepositoriesSplitViewBinding getBinding() {
        return DataBindingUtil.findBinding(getView());
    }

    @Override
    public RepositoryListFragment.SuperComponent getSuperComponent(
            RepositoryListFragment fragment) {
        return mHost.getSuperComponent(this);
    }

    @Override
    public RepositoryPagerFragment.SuperComponent getSuperComponent(
            RepositoryPagerFragment fragment) {
        return mHost.getSuperComponent(this);
    }

    @Override
    public RepositoriesSplitViewPresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(RepositoriesSplitViewPresenter presenter) {
        mPresenter = presenter;
    }

    public interface SuperComponent
            extends RepositoryListFragment.SuperComponent, RepositoryPagerFragment.SuperComponent {
        void inject(RepositoriesSplitFragment fragment);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(RepositoriesSplitFragment fragment);

        RepositoriesSplitViewPresenter.ParentPresenter getPresenter();
    }
}
