package info.mschmitt.githubapp.app;

import android.databinding.DataBindingUtil;
import android.view.View;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.databinding.RepositoriesSplitViewBinding;
import info.mschmitt.githubapp.domain.LoadingProgressManager;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.presenters.RepositoryDetailsViewModel;
import info.mschmitt.githubapp.presenters.RepositoryListViewModel;
import info.mschmitt.githubapp.presenters.RepositoryPagerViewModel;
import info.mschmitt.githubapp.presenters.RepositorySplitViewModel;
import info.mschmitt.githubapp.presenters.RootViewModel;
import info.mschmitt.githubapp.presenters.UsernameViewModel;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Matthias Schmitt
 */
public class NavigationManager
        implements UsernameViewModel.NavigationHandler, RepositorySplitViewModel.NavigationHandler,
        RepositoryListViewModel.NavigationHandler, RepositoryPagerViewModel.NavigationHandler,
        RepositoryDetailsViewModel.NavigationHandler, RootViewModel.NavigationHandler {
    private final LoadingProgressManager mLoadingProgressManager;
    private final BehaviorSubject<Repository> mSelectedRepository = BehaviorSubject.create();
    private final BehaviorSubject<LinkedHashMap<Long, Repository>> mRepositoryMap =
            BehaviorSubject.create();
    private RepositorySplitFragment mRepositorySplitFragment;
    private RootFragment mRootFragment;
    private UsernameFragment mUsernameFragment;
    private RepositoryPagerFragment mRepositoryPagerFragment;
    private RepositoryListFragment mRepositoryListFragment;
    private RepositoryDetailsFragment mRepositoryDetailsFragment;
    private FragmentActivity mFragmentActivity;
    private boolean mDetailsViewActive;

    @Inject
    public NavigationManager(LoadingProgressManager loadingProgressManager) {
        mLoadingProgressManager = loadingProgressManager;
    }

    @Override
    public void showRepositories(LinkedHashMap<Long, Repository> repositoryMap) {
        mRepositoryMap.onNext(repositoryMap);
    }

    @Override
    public void showRepository(Repository repository) {
        mSelectedRepository.onNext(repository);
        if (mDetailsViewActive) {
            return;
        }
        RepositoriesSplitViewBinding binding =
                DataBindingUtil.getBinding(mRepositorySplitFragment.getView());
        assert binding != null;
        if (!isInSplitMode()) {
            binding.masterView.setVisibility(View.GONE);
        }
        binding.detailsView.setVisibility(View.VISIBLE);
        mDetailsViewActive = true;
    }

    @Override
    public Observable<LinkedHashMap<Long, Repository>> getRepositoryMap() {
        return mRepositoryMap.asObservable();
    }

    @Override
    public Observable<Repository> getRepository() {
        return mSelectedRepository.asObservable();
    }

    private boolean isInSplitMode() {
        return mRepositorySplitFragment.getResources().getBoolean(R.bool.split);
    }

    public boolean onBackPressed() {
        if (mLoadingProgressManager.cancelAllTasks(true)) {
            return true;
        }
        if (mDetailsViewActive && !isInSplitMode()) {
            hideRepositoryDetailsView();
            return true;
        }
        return mRootFragment.getChildFragmentManager().popBackStackImmediate();
    }

    private void hideRepositoryDetailsView() {
        RepositoriesSplitViewBinding binding =
                DataBindingUtil.getBinding(mRepositorySplitFragment.getView());
        assert binding != null;
        binding.masterView.setVisibility(View.VISIBLE);
        if (!isInSplitMode()) {
            binding.detailsView.setVisibility(View.GONE);
        }
        mDetailsViewActive = false;
    }

    public void onHomeOrUpPressed() {
        if (mDetailsViewActive) {
            hideRepositoryDetailsView();
        }
    }


    @Override
    public void showRepositorySplitView(String username) {
        mRootFragment.getChildFragmentManager().beginTransaction()
                .replace(R.id.contentView, RepositorySplitFragment.newInstance(username))
                .addToBackStack(null).commit();
    }

    @Override
    public void showError(Throwable throwable, Runnable retryHandler) {
        AlertDialogs.showErrorDialog(mFragmentActivity, throwable, retryHandler);
    }

    public void onCreate(RepositorySplitFragment repositorySplitFragment) {
        mRepositorySplitFragment = repositorySplitFragment;
    }

    public void onDestroy(RepositorySplitFragment repositorySplitFragment) {
        mRepositorySplitFragment = null;
    }

    public void onCreate(RootFragment rootFragment) {
        mRootFragment = rootFragment;
    }

    public void onDestroy(RootFragment rootFragment) {
        mRootFragment = null;
    }

    public void onCreate(UsernameFragment usernameFragment) {
        mUsernameFragment = usernameFragment;
    }

    public void onDestroy(UsernameFragment usernameFragment) {
        mUsernameFragment = null;
    }

    public void onCreate(RepositoryPagerFragment repositoryPagerFragment) {
        mRepositoryPagerFragment = repositoryPagerFragment;
    }

    public void onDestroy(RepositoryPagerFragment repositoryPagerFragment) {
        mRepositoryPagerFragment = null;
    }

    public void onCreate(RepositoryListFragment repositoryListFragment) {
        mRepositoryListFragment = repositoryListFragment;
    }


    public void onDestroy(RepositoryListFragment repositoryListFragment) {
        mRepositoryListFragment = null;
    }

    public void onCreate(RepositoryDetailsFragment repositoryDetailsFragment) {
        mRepositoryDetailsFragment = repositoryDetailsFragment;
    }

    public void onDestroy(RepositoryDetailsFragment repositoryDetailsFragment) {
        mRepositoryDetailsFragment = null;
    }

    public void onDestroy(FragmentActivity fragmentActivity) {
        mFragmentActivity = null;
    }

    public void onCreate(FragmentActivity fragmentActivity) {
        mFragmentActivity = fragmentActivity;
    }
}
