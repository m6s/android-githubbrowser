package info.mschmitt.githubbrowser.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.android.FragmentUtils;
import info.mschmitt.githubbrowser.databinding.RepositoryDetailsViewBinding;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositoryDetailsViewModel;

/**
 * @author Matthias Schmitt
 */
public class RepositoryDetailsViewFragment extends Fragment {
    private static final String ARG_REPOSITORY_POSITION = "ARG_REPOSITORY_POSITION";
    @Inject RepositoryDetailsViewModel mViewModel;

    public static RepositoryDetailsViewFragment newInstanceForRepositoryPosition(
            int repositoryPosition) {
        RepositoryDetailsViewFragment fragment = new RepositoryDetailsViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_REPOSITORY_POSITION, repositoryPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.getHost(this, FragmentHost.class).repositoryDetailsViewComponent(this)
                .inject(this);
        mViewModel.onLoadForPosition(getArguments().getInt(ARG_REPOSITORY_POSITION),
                savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RepositoryDetailsViewBinding binding =
                RepositoryDetailsViewBinding.inflate(inflater, container, false);
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
        mViewModel = null;
        super.onDestroy();
    }

    public interface FragmentHost {
        Component repositoryDetailsViewComponent(RepositoryDetailsViewFragment fragment);
    }

    public interface Component {
        void inject(RepositoryDetailsViewFragment fragment);
    }
}
