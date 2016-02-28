package info.mschmitt.githubapp.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RepositoryPagerViewFragment;

/**
 * @author Matthias Schmitt
 */
@RepositoryPagerViewScope
@Subcomponent(modules = {RepositoryPagerViewModule.class})
public interface RepositoryPagerViewComponent extends RepositoryPagerViewFragment.Component {
    @Override
    RepositoryDetailsViewComponent plus(RepositoryDetailsViewModule module);
}
