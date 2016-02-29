package info.mschmitt.githubapp.application;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.android.presentation.BugFixFragment;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.databinding.RootViewBinding;
import info.mschmitt.githubapp.viewmodels.RootViewModel;

/**
 * @author Matthias Schmitt
 */
public class RootViewFragment extends BugFixFragment
        implements UsernameViewFragment.FragmentHost, RepositorySplitViewFragment.FragmentHost {
    private FragmentHost mHost;
    private RootViewModel mViewModel;
    private Component mComponent;

    public static RootViewFragment newInstance() {
        return new RootViewFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((Application) getActivity().getApplication()).getComponent().rootViewComponent(this)
                .inject(this);
        mViewModel.onLoad(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RootViewBinding binding = RootViewBinding.inflate(inflater, container, false);
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .add(binding.contentView.getId(), UsernameViewFragment.newInstance()).commit();
        }
        binding.setViewModel(mViewModel);
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
    public Component getComponent() {
        return mComponent;
    }

    @Inject
    public void setComponent(Component component) {
        mComponent = component;
    }

    @Inject
    public void setViewModel(RootViewModel viewModel) {
        mViewModel = viewModel;
    }

    public boolean onBackPressed() {
        return mViewModel.onBackPressed();
    }

    public interface Component extends RepositorySplitViewFragment.SuperComponent,
            UsernameViewFragment.SuperComponent {
        void inject(RootViewFragment fragment);
    }

    public interface SuperComponent {
        Component rootViewComponent(RootViewFragment fragment);
    }

    public interface Application {
        SuperComponent getComponent();
    }

    public interface FragmentHost {
    }
}
