package info.mschmitt.githubapp.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.adapters.RepositoryListAdapter;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.databinding.RepositoryListViewBinding;
import info.mschmitt.githubapp.modules.RepositoryListModule;
import info.mschmitt.githubapp.presenters.RepositoryListPresenter;

/**
 * A placeholder fragment containing a simple view.
 */
public class RepositoryListFragment extends Fragment
        implements Presentable<RepositoryListPresenter>,
        RepositoryListPresenter.RepositoryListView {
    private RepositoryListPresenter mPresenter;
    private FragmentHost mHost;
    private RepositoryListAdapter mAdapter;

    public static RepositoryListFragment newInstance() {
        return new RepositoryListFragment();
    }

    @Override
    public RepositoryListPresenter.ParentPresenter getParentPresenter() {
        return mHost.getPresenter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost.getSuperComponent(this).plus(new RepositoryListModule()).inject(this);
        mPresenter.onCreate(this, savedInstanceState);
        mAdapter = new RepositoryListAdapter(getActivity(), mPresenter.getRepositories());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RepositoryListViewBinding binding =
                RepositoryListViewBinding.inflate(inflater, container, false);
        binding.setPresenter(mPresenter);
        mAdapter.onCreateView(savedInstanceState);
        binding.setAdapter(mAdapter);
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.onSave(outState);
    }

    @Override
    public void onDestroyView() {
        mAdapter.onDestroyView();
        super.onDestroyView();
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
    public RepositoryListPresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(RepositoryListPresenter presenter) {
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

        RepositoryListPresenter.ParentPresenter getPresenter();
    }
}
