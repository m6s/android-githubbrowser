package info.mschmitt.githubapp.application;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.adapters.RepositoryPagerAdapter;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.dagger.RepositoryPagerViewModule;
import info.mschmitt.githubapp.databinding.RepositoryPagerViewBinding;
import info.mschmitt.githubapp.viewmodels.RepositoryPagerViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class RepositoryPagerViewFragment extends Fragment
        implements RepositoryDetailsViewFragment.FragmentHost {
    private RepositoryPagerViewModel mViewModel;
    private FragmentHost mHost;
    private Component mComponent;
    private RepositoryPagerAdapter mAdapter;

    public static RepositoryPagerViewFragment newInstance() {
        return new RepositoryPagerViewFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost.getComponent().plus(new RepositoryPagerViewModule()).inject(this);
        mViewModel.onLoad(savedInstanceState);
        mAdapter =
                new RepositoryPagerAdapter(getChildFragmentManager(), mViewModel.getRepositories());
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
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        mHost = null;
        super.onDetach();
    }

    @Inject
    public void setViewModel(RepositoryPagerViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public Component getComponent() {
        return mComponent;
    }

    @Inject
    public void setComponent(Component component) {
        mComponent = component;
    }

    public interface Component extends RepositoryDetailsViewFragment.SuperComponent {
        void inject(RepositoryPagerViewFragment fragment);
    }

    public interface SuperComponent {
        Component plus(RepositoryPagerViewModule module);
    }

    public interface FragmentHost {
        SuperComponent getComponent();
    }
}
