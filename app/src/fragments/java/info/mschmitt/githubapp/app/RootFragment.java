package info.mschmitt.githubapp.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.android.presentation.BugFixFragment;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.databinding.MainViewBinding;
import info.mschmitt.githubapp.modules.RootModule;
import info.mschmitt.githubapp.presenters.GitHubBrowserPresenter;

/**
 * @author Matthias Schmitt
 */
public class RootFragment extends BugFixFragment
        implements Presentable<GitHubBrowserPresenter>, GitHubBrowserPresenter.GitHubBrowserView,
        UsernameFragment.FragmentHost, RepositoriesSplitFragment.FragmentHost {
    private FragmentHost mHost;
    private GitHubBrowserPresenter mPresenter;
    private Component mComponent;

    public static RootFragment newInstance() {
        return new RootFragment();
    }

    @Override
    public GitHubBrowserPresenter.ParentPresenter getParentPresenter() {
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
        mPresenter.onCreate(this, savedInstanceState);
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
    public RepositoriesSplitFragment.SuperComponent getSuperComponent(
            RepositoriesSplitFragment fragment) {
        return mComponent;
    }

    @Override
    public GitHubBrowserPresenter getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(GitHubBrowserPresenter presenter) {
        mPresenter = presenter;
    }

    public interface Component
            extends RepositoriesSplitFragment.SuperComponent, UsernameFragment.SuperComponent {
        void inject(RootFragment fragment);
    }

    public interface SuperComponent {
        Component plus(RootModule module);
    }

    public interface Application {
        SuperComponent getSuperComponent(RootFragment fragment);
    }

    public interface FragmentHost {
        GitHubBrowserPresenter.ParentPresenter getPresenter();
    }
}
