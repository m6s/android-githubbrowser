package info.mschmitt.githubapp.fragments.navigation.scenes;

import android.app.Activity;
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
import info.mschmitt.githubapp.databinding.UsernameSceneActionBarBinding;
import info.mschmitt.githubapp.databinding.UsernameSceneViewBinding;
import info.mschmitt.githubapp.fragments.UsernameFragment;
import info.mschmitt.githubapp.modules.navigation.scenes.UsernameSceneModule;
import info.mschmitt.githubapp.presenters.navigation.scenes.UsernameSceneViewPresenter;


public class UsernameSceneFragment extends Fragment
        implements Presentable<UsernameSceneViewPresenter>,
        UsernameSceneViewPresenter.UsernameSceneView, ActionBarProvider,
        UsernameFragment.FragmentHost {
    private FragmentHost mHost;
    private UsernameSceneViewPresenter mPresenter;
    private Toolbar mActionBar;
    private Component mComponent;

    public static UsernameSceneFragment newInstance() {
        return new UsernameSceneFragment();
    }

    @Override
    public UsernameSceneViewPresenter.ParentPresenter getParentPresenter() {
        return mHost.getPresenter();
    }

    @Override
    public void showRepositories(Object sender, String username) {
        getFragmentManager().beginTransaction()
                .replace(FragmentUtils.getContainerViewId(UsernameSceneFragment.this),
                        RepositoriesSplitSceneFragment.newInstance(username)).addToBackStack(null)
                .commit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = mHost.getSuperComponent(this).plus(new UsernameSceneModule(this));
        mComponent.inject(this);
        mPresenter.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UsernameSceneActionBarBinding actionBarBinding =
                UsernameSceneActionBarBinding.inflate(inflater);
        actionBarBinding.setPresenter(mPresenter);
        mActionBar = (Toolbar) actionBarBinding.getRoot();
        UsernameSceneViewBinding binding =
                UsernameSceneViewBinding.inflate(inflater, container, false);
        binding.setPresenter(mPresenter);
        if (getChildFragmentManager().findFragmentById(binding.contentView.getId()) == null) {
            getChildFragmentManager().beginTransaction()
                    .add(binding.contentView.getId(), UsernameFragment.newInstance()).commit();
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

    @Override
    public Toolbar getActionBar() {
        return mActionBar;
    }

    @Override
    public UsernameFragment.SuperComponent getSuperComponent(UsernameFragment fragment) {
        return mComponent;
    }

    @Override
    public UsernameSceneViewPresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(UsernameSceneViewPresenter presenter) {
        mPresenter = presenter;
    }

    public interface Component extends UsernameFragment.SuperComponent {
        void inject(UsernameSceneFragment fragment);
    }

    public interface SuperComponent {
        Component plus(UsernameSceneModule module);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(UsernameSceneFragment fragment);

        UsernameSceneViewPresenter.ParentPresenter getPresenter();
    }
}
