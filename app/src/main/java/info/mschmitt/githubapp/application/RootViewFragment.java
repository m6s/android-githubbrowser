package info.mschmitt.githubapp.application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.android.presentation.BugFixFragment;
import info.mschmitt.githubapp.databinding.RootViewBinding;
import info.mschmitt.githubapp.viewmodels.RootViewModel;

/**
 * @author Matthias Schmitt
 */
public class RootViewFragment extends BugFixFragment
        implements UsernameViewFragment.FragmentHost, RepositorySplitViewFragment.FragmentHost {
    @Inject RootViewModel mViewModel;
    @Inject Component mComponent;

    public static RootViewFragment newInstance() {
        return new RootViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((Application) getActivity().getApplication()).inject(this);
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
        mViewModel = null;
        super.onDestroy();
    }

    public boolean onBackPressed() {
        return mViewModel.onBackPressed();
    }

    @Override
    public void inject(RepositorySplitViewFragment fragment) {
        mComponent.inject(fragment);
    }

    @Override
    public void inject(UsernameViewFragment fragment) {
        mComponent.inject(fragment);
    }

    public interface Component {
        void inject(RepositorySplitViewFragment fragment);

        void inject(UsernameViewFragment fragment);
    }

    public interface Application {
        void inject(RootViewFragment fragment);
    }

    public interface FragmentHost {
    }
}
