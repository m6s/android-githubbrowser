package info.mschmitt.githubbrowser.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
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
public class AboutViewDialogFragment extends DialogFragment {
    @Inject AboutViewModel mViewModel;

    public static AboutViewDialogFragment newInstance() {
        return new AboutViewDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.getHost(this, FragmentHost.class).aboutViewComponent(this).inject(this);
        mViewModel.onLoad(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.about_view_title);
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mViewModel.onSave(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AboutViewBinding binding = AboutViewBinding.inflate(inflater, container, false);
        binding.setViewModel(mViewModel);
        binding.textView.setMovementMethod(new ScrollingMovementMethod());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.onResume();
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
        Component aboutViewComponent(AboutViewDialogFragment fragment);
    }

    public interface Component {
        void inject(AboutViewDialogFragment fragment);
    }
}
