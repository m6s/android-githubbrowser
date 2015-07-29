package info.mschmitt.githubapp.fragments.navigation.scenes;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.android.presentation.ActionBarProvider;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.databinding.EmptyActionBarBinding;
import info.mschmitt.githubapp.databinding.RepositoriesSplitSceneViewBinding;
import info.mschmitt.githubapp.fragments.RepositoriesSplitFragment;
import info.mschmitt.githubapp.modules.RepositoriesSplitSceneModule;
import info.mschmitt.githubapp.presenters.RepositoriesSplitViewPresenter;
import info.mschmitt.githubapp.presenters.navigation.scenes.RepositoriesSplitSceneViewPresenter;


public class RepositoriesSplitSceneFragment extends Fragment
        implements Presentable<RepositoriesSplitSceneViewPresenter>, ActionBarProvider,
        RepositoriesSplitFragment.FragmentHost {
    private static final String ARG_USERNAME = "arg_username";
    private FragmentHost mHost;
    private RepositoriesSplitSceneViewPresenter mPresenter;
    private Toolbar mActionBar;
    private Component mComponent;
    private RepositoriesSplitFragment mRepositoriesSplitFragment;
    private RepositoriesSplitSceneViewPresenter.RepositoriesSplitSceneView
            mRepositoriesSplitSceneView =
            new RepositoriesSplitSceneViewPresenter.RepositoriesSplitSceneView() {
                @Override
                public RepositoriesSplitViewPresenter getChildPresenter() {
                    return mRepositoriesSplitFragment.getPresenter();
                }

                @Override
                public RepositoriesSplitSceneViewPresenter.ParentPresenter getParentPresenter() {
                    return mHost.getPresenter();
                }
            };

    public static RepositoriesSplitSceneFragment newInstance(String username) {
        RepositoriesSplitSceneFragment fragment = new RepositoriesSplitSceneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SuperComponent superComponent = mHost.getSuperComponent(this);
        superComponent.inject(this);
        mComponent =
                superComponent.plus(new RepositoriesSplitSceneModule(mPresenter.getRepositories()));
        mPresenter.onCreate(mRepositoriesSplitSceneView, savedInstanceState);
        mPresenter.setUsername(getArguments().getString(ARG_USERNAME));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EmptyActionBarBinding actionBarBinding = EmptyActionBarBinding.inflate(inflater);
        actionBarBinding.setPresenter(mPresenter);
        mActionBar = (Toolbar) actionBarBinding.getRoot();
        RepositoriesSplitSceneViewBinding binding =
                RepositoriesSplitSceneViewBinding.inflate(inflater, container, false);
        binding.setPresenter(mPresenter);
        mRepositoriesSplitFragment = (RepositoriesSplitFragment) getChildFragmentManager()
                .findFragmentById(binding.contentView.getId());
        if (mRepositoriesSplitFragment == null) {
            mRepositoriesSplitFragment = RepositoriesSplitFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .add(binding.contentView.getId(), mRepositoriesSplitFragment).commit();
        }
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.onSave(outState);
    }

    @Override
    public void onDestroyView() {
        mActionBar = null;
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

    private RepositoriesSplitSceneViewBinding getBinding() {
        return DataBindingUtil.findBinding(getView());
    }

    @Override
    public Toolbar getActionBar() {
        return mActionBar;
    }

    @Override
    public RepositoriesSplitFragment.SuperComponent getSuperComponent(
            RepositoriesSplitFragment fragment) {
        return mComponent;
    }

    @Override
    public RepositoriesSplitSceneViewPresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(RepositoriesSplitSceneViewPresenter presenter) {
        mPresenter = presenter;
    }

    public interface Component extends RepositoriesSplitFragment.SuperComponent {
    }

    public interface SuperComponent {
        Component plus(RepositoriesSplitSceneModule module);

        void inject(RepositoriesSplitSceneFragment fragment);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(RepositoriesSplitSceneFragment fragment);

        RepositoriesSplitSceneViewPresenter.ParentPresenter getPresenter();
    }
}
