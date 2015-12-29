package info.mschmitt.githubapp.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.android.presentation.BugFixFragment;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.databinding.MainViewBinding;
import info.mschmitt.githubapp.modules.RootViewModule;
import info.mschmitt.githubapp.presenters.RootViewModel;

/**
 * @author Matthias Schmitt
 */
public class RootViewFragment extends BugFixFragment
        implements UsernameViewFragment.FragmentHost, RepositorySplitViewFragment.FragmentHost {
    private FragmentHost mHost;
    private RootViewModel mPresenter;
    private Component mComponent;
    private NavigationManager mNavigationManager;

    public static RootViewFragment newInstance() {
        return new RootViewFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Application application = (Application) getActivity().getApplication();
        mComponent = application.getSuperComponent(this).plus(new RootViewModule());
        mComponent.inject(this);
        mNavigationManager.onCreate(this);
        mPresenter.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainViewBinding binding = MainViewBinding.inflate(inflater, container, false);
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .add(binding.contentView.getId(), UsernameViewFragment.newInstance()).commit();
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
        mNavigationManager.onDestroy(this);
        mComponent = null;
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        mHost = null;
        super.onDetach();
    }

    @Override
    public UsernameViewFragment.SuperComponent getSuperComponent(UsernameViewFragment fragment) {
        return mComponent;
    }

    @Override
    public RepositorySplitViewFragment.SuperComponent getSuperComponent(
            RepositorySplitViewFragment fragment) {
        return mComponent;
    }

    @Inject
    public void setPresenter(RootViewModel presenter) {
        mPresenter = presenter;
    }

    @Inject
    public void setNavigationManager(NavigationManager navigationManager) {
        mNavigationManager = navigationManager;
    }

    public interface Component extends RepositorySplitViewFragment.SuperComponent,
            UsernameViewFragment.SuperComponent {
        void inject(RootViewFragment fragment);
    }

    public interface SuperComponent {
        Component plus(RootViewModule module);
    }

    public interface Application {
        SuperComponent getSuperComponent(RootViewFragment fragment);
    }

    public interface FragmentHost {
    }
}
