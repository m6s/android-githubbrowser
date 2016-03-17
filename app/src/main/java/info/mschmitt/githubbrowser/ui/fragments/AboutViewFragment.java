package info.mschmitt.githubbrowser.ui.fragments;

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

import info.mschmitt.githubbrowser.R;
import info.mschmitt.githubbrowser.android.FragmentUtils;
import info.mschmitt.githubbrowser.databinding.AboutViewBinding;
import info.mschmitt.githubbrowser.ui.viewmodels.AboutViewModel;

/**
 * @author Matthias Schmitt
 */
public class AboutViewFragment extends Fragment {
    @Inject AboutViewModel mViewModel;

    public static AboutViewFragment newInstance() {
        return new AboutViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.getHost(this, FragmentHost.class).aboutViewComponent(this).inject(this);
        mViewModel.onLoad(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AboutViewBinding binding = AboutViewBinding.inflate(inflater, container, false);
        binding.setViewModel(mViewModel);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
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
        mViewModel = null;
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.about, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public interface FragmentHost {
        Component aboutViewComponent(AboutViewFragment fragment);
    }

    public interface Component {
        void inject(AboutViewFragment fragment);
    }
}
