package info.mschmitt.githubbrowser.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.android.FragmentUtils;
import info.mschmitt.githubbrowser.databinding.RepositoryPagerViewBinding;
import info.mschmitt.githubbrowser.ui.adapters.RepositoryPagerAdapter;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositoryPagerViewModel;

/**
 * @author Matthias Schmitt
 */
public class RepositoryPagerViewFragment extends Fragment
        implements RepositoryDetailsViewFragment.FragmentHost {
    @Inject Component mComponent;
    @Inject RepositoryPagerViewModel mViewModel;
    @Inject RepositoryPagerAdapter mAdapter;

    public static RepositoryPagerViewFragment newInstance() {
        return new RepositoryPagerViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.getHost(this, FragmentHost.class).repositoryPagerViewComponent(this)
                .inject(this);
        mViewModel.onLoad(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RepositoryPagerViewBinding binding =
                RepositoryPagerViewBinding.inflate(inflater, container, false);
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
        mComponent = null;
        mViewModel = null;
        mAdapter = null;
        super.onDestroy();
    }

    @Override
    public RepositoryDetailsViewFragment.Component repositoryDetailsViewComponent(
            RepositoryDetailsViewFragment fragment) {
        return mComponent.repositoryDetailsViewComponent(fragment);
    }

    public interface Component {
        RepositoryDetailsViewFragment.Component repositoryDetailsViewComponent(
                RepositoryDetailsViewFragment fragment);

        void inject(RepositoryPagerViewFragment fragment);
    }

    public interface FragmentHost {
        Component repositoryPagerViewComponent(RepositoryPagerViewFragment fragment);
    }
}
