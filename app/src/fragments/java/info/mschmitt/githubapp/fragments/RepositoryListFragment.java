package info.mschmitt.githubapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import info.mschmitt.githubapp.adapters.RepositoryListAdapter;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.databinding.RepositoryListViewBinding;
import info.mschmitt.githubapp.modules.navigation.RepositoryListModule;
import info.mschmitt.githubapp.presenters.RepositoryListViewPresenter;

/**
 * A placeholder fragment containing a simple view.
 */
public class RepositoryListFragment extends Fragment
        implements Presentable<RepositoryListViewPresenter>,
        RepositoryListViewPresenter.RepositoryListView {
    private RepositoryListViewPresenter mPresenter;
    private FragmentHost mHost;
    private RepositoryListAdapter mAdapter;

    public static RepositoryListFragment newInstance() {
        return new RepositoryListFragment();
    }

    @Override
    public RepositoryListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public RepositoryListViewPresenter.ParentPresenter getParentPresenter() {
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
        mHost.getSuperComponent(this).plus(new RepositoryListModule(this)).inject(this);
        mAdapter = new RepositoryListAdapter(getActivity(), new ArrayList<>());
        mPresenter.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RepositoryListViewBinding binding =
                RepositoryListViewBinding.inflate(inflater, container, false);
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
    public RepositoryListViewPresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(RepositoryListViewPresenter presenter) {
        mPresenter = presenter;
    }

    public interface Component {
        void inject(RepositoryListFragment fragment);
    }

    public interface SuperComponent {
        Component plus(RepositoryListModule module);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(RepositoryListFragment fragment);

        RepositoryListViewPresenter.ParentPresenter getPresenter();
    }
}
