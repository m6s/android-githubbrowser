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
import info.mschmitt.githubbrowser.databinding.UsernameViewBinding;
import info.mschmitt.githubbrowser.ui.viewmodels.UsernameViewModel;

/**
 * @author Matthias Schmitt
 */
public class UsernameViewFragment extends Fragment {
    @Inject UsernameViewModel mViewModel;

    public static UsernameViewFragment newInstance() {
        return new UsernameViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.getHost(this, FragmentHost.class).usernameViewComponent(this).inject(this);
        mViewModel.onLoad(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UsernameViewBinding binding = UsernameViewBinding.inflate(inflater, container, false);
        binding.setViewModel(mViewModel);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar);
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
        inflater.inflate(R.menu.username, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                return mViewModel.onAboutOptionsItemSelected();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public interface FragmentHost {
        Component usernameViewComponent(UsernameViewFragment fragment);
    }

    public interface Component {
        void inject(UsernameViewFragment fragment);
    }
}
