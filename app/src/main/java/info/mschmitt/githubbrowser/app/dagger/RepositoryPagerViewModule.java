package info.mschmitt.githubbrowser.app.dagger;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubbrowser.ui.adapters.RepositoryPagerAdapter;
import info.mschmitt.githubbrowser.ui.fragments.RepositoryPagerViewFragment;
import info.mschmitt.githubbrowser.ui.scopes.RepositoryPagerViewScope;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositoryPagerViewModel;

/**
 * @author Matthias Schmitt
 */
@Module
class RepositoryPagerViewModule {
    private final RepositoryPagerViewFragment mFragment;

    public RepositoryPagerViewModule(RepositoryPagerViewFragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @RepositoryPagerViewScope
    public RepositoryPagerAdapter provideAdapter(RepositoryPagerViewModel viewModel) {
        return new RepositoryPagerAdapter(mFragment.getChildFragmentManager(),
                viewModel.getRepositories());
    }

    @Provides
    @RepositoryPagerViewScope
    public RepositoryPagerViewFragment.Component provideComponent(
            RepositoryPagerViewComponent component) {
        return component;
    }
}
