package info.mschmitt.githubapp.application;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.databinding.RepositoryDetailsViewBinding;
import info.mschmitt.githubapp.viewmodels.RepositoryDetailsViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class RepositoryDetailsViewFragment extends Fragment {
    private static final String ARG_REPOSITORY_POSITION = "ARG_REPOSITORY_POSITION";
    private RepositoryDetailsViewModel mViewModel;
    private FragmentHost mHost;

    public static RepositoryDetailsViewFragment newInstanceForRepositoryPosition(
            int repositoryPosition) {
        RepositoryDetailsViewFragment fragment = new RepositoryDetailsViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_REPOSITORY_POSITION, repositoryPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost.getComponent().repositoryDetailsViewComponent().inject(this);
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
    public void onDetach() {
        mHost = null;
        super.onDetach();
    }

    @Inject
    public void setViewModel(RepositoryDetailsViewModel viewModel) {
        mViewModel = viewModel;
    }

    public interface Component {
        void inject(RepositoryDetailsViewFragment fragment);
    }

    public interface SuperComponent {
        Component repositoryDetailsViewComponent();
    }

    public interface FragmentHost {
        SuperComponent getComponent();
    }
}
