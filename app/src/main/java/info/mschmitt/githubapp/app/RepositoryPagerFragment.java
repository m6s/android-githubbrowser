package info.mschmitt.githubapp.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.adapters.RepositoryPagerAdapter;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.databinding.RepositoryPagerViewBinding;
import info.mschmitt.githubapp.modules.RepositoryPagerModule;
import info.mschmitt.githubapp.presenters.RepositoryPagerPresenter;

/**
 * A placeholder fragment containing a simple view.
 */
public class RepositoryPagerFragment extends Fragment
        implements Presentable<RepositoryPagerPresenter>,
        RepositoryPagerPresenter.RepositoryPagerView, RepositoryDetailsFragment.FragmentHost {
    private RepositoryPagerPresenter mPresenter;
    private FragmentHost mHost;
    private Component mComponent;
    private RepositoryPagerAdapter mAdapter;

    public static RepositoryPagerFragment newInstance() {
        return new RepositoryPagerFragment();
    }

    @Override
    public RepositoryPagerPresenter.ParentPresenter getParentPresenter() {
        return mHost.getPresenter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = mHost.getSuperComponent(this).plus(new RepositoryPagerModule());
        mComponent.inject(this);
        mPresenter.onCreate(this, savedInstanceState);
        mAdapter =
                new RepositoryPagerAdapter(getChildFragmentManager(), mPresenter.getRepositories());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RepositoryPagerViewBinding binding =
                RepositoryPagerViewBinding.inflate(inflater, container, false);
        binding.setPresenter(mPresenter);
        mAdapter.onCreateView(savedInstanceState);
        binding.setAdapter(mAdapter);
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.onSave(outState);
    }

    @Override
    public void onDestroyView() {
        mAdapter.onDestroyView();
        super.onDestroyView();
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
    public RepositoryPagerPresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(RepositoryPagerPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RepositoryDetailsFragment.SuperComponent getSuperComponent(
            RepositoryDetailsFragment fragment) {
        return mComponent;
    }


    public interface Component extends RepositoryDetailsFragment.SuperComponent {
        void inject(RepositoryPagerFragment fragment);
    }

    public interface SuperComponent {
        Component plus(RepositoryPagerModule module);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(RepositoryPagerFragment fragment);

        RepositoryPagerPresenter.ParentPresenter getPresenter();
    }
}
