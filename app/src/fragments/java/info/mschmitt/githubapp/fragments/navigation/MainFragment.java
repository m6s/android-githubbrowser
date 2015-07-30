package info.mschmitt.githubapp.fragments.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.AlertDialogs;
import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.android.presentation.ActionBarProvider;
import info.mschmitt.githubapp.android.presentation.BugFixFragment;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.databinding.MainViewBinding;
import info.mschmitt.githubapp.fragments.navigation.scenes.RepositoriesSplitSceneFragment;
import info.mschmitt.githubapp.fragments.navigation.scenes.UsernameSceneFragment;
import info.mschmitt.githubapp.modules.navigation.MainModule;
import info.mschmitt.githubapp.presenters.navigation.MainViewPresenter;

/**
 * @author Matthias Schmitt
 */
public class MainFragment extends BugFixFragment
        implements Presentable<MainViewPresenter>, MainViewPresenter.MainView,
        UsernameSceneFragment.FragmentHost, RepositoriesSplitSceneFragment.FragmentHost {
    private FragmentHost mHost;
    private MainViewPresenter mPresenter;
    private Component mComponent;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public MainViewPresenter.ParentPresenter getParentPresenter() {
        return mHost.getPresenter();
    }

    @Override
    public Object getChildPresenter() {
        Presentable fragment =
                (Presentable) getChildFragmentManager().findFragmentById(R.id.contentView);
        return fragment.getPresenter();
    }

    @Override
    public boolean tryShowPreviousChildView() {
        return getChildFragmentManager().popBackStackImmediate();
    }

    @Override
    public void showErrorDialog(Throwable throwable, Runnable retryHandler) {
        AlertDialogs.showErrorDialog(getActivity(), throwable, retryHandler);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Application application = (Application) getActivity().getApplication();
        mComponent = application.getSuperComponent(this).plus(new MainModule(this));
        mComponent.inject(this);
        mPresenter.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainViewBinding binding = MainViewBinding.inflate(inflater, container, false);
        binding.contentView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                Fragment fragment =
                        getChildFragmentManager().findFragmentById(binding.contentView.getId());
                Toolbar actionBar = null;
                if (fragment instanceof ActionBarProvider) {
                    actionBar = ((ActionBarProvider) fragment).getActionBar();
                    binding.actionBarView.addView(actionBar);
                } else {
                    binding.actionBarView.removeAllViews();
                }
                mHost.setSupportActionBar(actionBar);
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                binding.actionBarView.removeAllViews();
            }
        });
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .add(binding.contentView.getId(), UsernameSceneFragment.newInstance()).commit();
        }
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
        mComponent = null;
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        mHost = null;
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public UsernameSceneFragment.SuperComponent getSuperComponent(UsernameSceneFragment fragment) {
        return mComponent;
    }

    @Override
    public RepositoriesSplitSceneFragment.SuperComponent getSuperComponent(
            RepositoriesSplitSceneFragment fragment) {
        return mComponent;
    }

    @Override
    public MainViewPresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(MainViewPresenter presenter) {
        mPresenter = presenter;
    }

    public interface Component extends RepositoriesSplitSceneFragment.SuperComponent,
            UsernameSceneFragment.SuperComponent {
        void inject(MainFragment fragment);
    }

    public interface SuperComponent {
        Component plus(MainModule module);
    }

    public interface Application {
        SuperComponent getSuperComponent(MainFragment fragment);
    }

    public interface FragmentHost {
        void setSupportActionBar(Toolbar actionBar);

        MainViewPresenter.ParentPresenter getPresenter();
    }
}
