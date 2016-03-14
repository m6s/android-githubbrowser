package info.mschmitt.githubbrowser.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.android.BugFixFragment;
import info.mschmitt.githubbrowser.android.FragmentUtils;
import info.mschmitt.githubbrowser.databinding.RootViewBinding;
import info.mschmitt.githubbrowser.ui.viewmodels.RootViewModel;

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
        FragmentUtils.retainAndGetHost(this, FragmentHost.class).rootViewComponent(this)
                .inject(this);
        mViewModel.onLoad(savedInstanceState);
        setHasOptionsMenu(true); // We want to catch the home button
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                return mViewModel.onHomeOptionsItemSelected();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onBackPressed() {
        return mViewModel.onBackPressed();
    }

    @Override
    public RepositorySplitViewFragment.Component repositorySplitViewComponent(
            RepositorySplitViewFragment fragment) {
        return mComponent.repositorySplitViewComponent(fragment);
    }

    @Override
    public UsernameViewFragment.Component usernameViewComponent(UsernameViewFragment fragment) {
        return mComponent.usernameViewComponent(fragment);
    }

    public interface Component {
        RepositorySplitViewFragment.Component repositorySplitViewComponent(
                RepositorySplitViewFragment fragment);

        UsernameViewFragment.Component usernameViewComponent(UsernameViewFragment fragment);

        void inject(RootViewFragment fragment);
    }

    public interface FragmentHost {
        Component rootViewComponent(RootViewFragment fragment);
    }
}
