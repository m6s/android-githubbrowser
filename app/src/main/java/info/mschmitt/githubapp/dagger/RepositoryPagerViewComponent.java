package info.mschmitt.githubapp.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RepositoryDetailsViewFragment;
import info.mschmitt.githubapp.application.RepositoryPagerViewFragment;
import info.mschmitt.githubapp.scopes.RepositoryPagerViewScope;

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
