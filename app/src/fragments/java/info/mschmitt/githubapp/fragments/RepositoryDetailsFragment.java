package info.mschmitt.githubapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.android.presentation.OnBackPressedListener;
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.databinding.RepositoryDetailsViewBinding;
import info.mschmitt.githubapp.modules.navigation.RepositoryDetailsModule;
import info.mschmitt.githubapp.presenters.RepositoryDetailsViewPresenter;

/**
 * A placeholder fragment containing a simple view.
 */
public class RepositoryDetailsFragment extends Fragment
        implements Presentable<RepositoryDetailsViewPresenter>,
        RepositoryDetailsViewPresenter.RepositoryDetailsView, OnBackPressedListener {
    private static final String ARG_REPOSITORY_POSITION = "ARG_REPOSITORY_POSITION";
    private RepositoryDetailsViewPresenter mPresenter;
    private FragmentHost mHost;

    public static RepositoryDetailsFragment newInstanceForRepositoryPosition(
            int repositoryPosition) {
        RepositoryDetailsFragment fragment = new RepositoryDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_REPOSITORY_POSITION, repositoryPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public RepositoryDetailsViewPresenter.ParentPresenter getParentPresenter() {
        return mHost.getPresenter();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost.getSuperComponent(this).plus(new RepositoryDetailsModule(this,
                getArguments().getInt(ARG_REPOSITORY_POSITION))).inject(this);
        mPresenter.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RepositoryDetailsViewBinding binding =
                RepositoryDetailsViewBinding.inflate(inflater, container, false);
        binding.setPresenter(mPresenter);
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.onSave(outState);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        mHost = null;
        super.onDetach();
    }

    @Override
    public RepositoryDetailsViewPresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(RepositoryDetailsViewPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public interface Component {
        void inject(RepositoryDetailsFragment fragment);
    }

    public interface SuperComponent {
        Component plus(RepositoryDetailsModule module);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(RepositoryDetailsFragment fragment);

        RepositoryDetailsViewPresenter.ParentPresenter getPresenter();
    }
}
