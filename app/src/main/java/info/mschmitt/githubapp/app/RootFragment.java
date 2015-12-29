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
import info.mschmitt.githubapp.modules.RootModule;
import info.mschmitt.githubapp.presenters.RootViewModel;

/**
 * @author Matthias Schmitt
 */
public class RootFragment extends BugFixFragment
        implements UsernameFragment.FragmentHost, RepositorySplitFragment.FragmentHost {
    private FragmentHost mHost;
    private RootViewModel mPresenter;
    private Component mComponent;
    private NavigationManager mNavigationManager;

    public static RootFragment newInstance() {
        return new RootFragment();
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
        mComponent = application.getSuperComponent(this).plus(new RootModule());
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
                    .add(binding.contentView.getId(), UsernameFragment.newInstance()).commit();
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
    public UsernameFragment.SuperComponent getSuperComponent(UsernameFragment fragment) {
        return mComponent;
    }

    @Override
    public RepositorySplitFragment.SuperComponent getSuperComponent(
            RepositorySplitFragment fragment) {
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

    public interface Component
            extends RepositorySplitFragment.SuperComponent, UsernameFragment.SuperComponent {
        void inject(RootFragment fragment);
    }

    public interface SuperComponent {
        Component plus(RootModule module);
    }

    public interface Application {
        SuperComponent getSuperComponent(RootFragment fragment);
    }

    public interface FragmentHost {
    }
}
