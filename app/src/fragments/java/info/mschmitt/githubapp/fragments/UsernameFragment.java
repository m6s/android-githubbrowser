package info.mschmitt.githubapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.databinding.UsernameViewBinding;
import info.mschmitt.githubapp.modules.UsernameModule;
import info.mschmitt.githubapp.presenters.UsernameViewPresenter;


public class UsernameFragment extends Fragment
        implements Presentable<UsernameViewPresenter>, UsernameViewPresenter.UsernameView {
    private FragmentHost mHost;
    private UsernameViewPresenter mPresenter;

    public static UsernameFragment newInstance() {
        return new UsernameFragment();
    }

    @Override
    public UsernameViewPresenter.ParentPresenter getParentPresenter() {
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
        mHost.getSuperComponent(this).plus(new UsernameModule(this)).inject(this);
        mPresenter.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UsernameViewBinding binding = UsernameViewBinding.inflate(inflater, container, false);
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
    public UsernameViewPresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(UsernameViewPresenter presenter) {
        mPresenter = presenter;
    }

    public interface Component {
        void inject(UsernameFragment fragment);
    }

    public interface SuperComponent {
        Component plus(UsernameModule module);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(UsernameFragment fragment);

        UsernameViewPresenter.ParentPresenter getPresenter();
    }
}
