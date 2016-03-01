package info.mschmitt.githubbrowser.app.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubbrowser.ui.fragments.RepositoryDetailsViewFragment;
import info.mschmitt.githubbrowser.ui.fragments.RepositoryPagerViewFragment;
import info.mschmitt.githubbrowser.ui.scopes.RepositoryPagerViewScope;

/**
 * @author Matthias Schmitt
 */
@RepositoryPagerViewScope
@Subcomponent(modules = {RepositoryPagerViewModule.class})
abstract class RepositoryPagerViewComponent implements RepositoryPagerViewFragment.Component {
    @Override
    public void inject(RepositoryDetailsViewFragment fragment) {
        repositoryDetailsViewComponent().inject(fragment);
    }

    abstract RepositoryDetailsViewComponent repositoryDetailsViewComponent();

    abstract void inject(RepositoryPagerViewFragment fragment);
}
