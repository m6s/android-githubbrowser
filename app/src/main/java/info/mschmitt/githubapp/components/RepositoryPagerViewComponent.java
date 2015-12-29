package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RepositoryPagerViewFragment;
import info.mschmitt.githubapp.modules.RepositoryDetailsViewModule;
import info.mschmitt.githubapp.modules.RepositoryPagerViewModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositoryPagerViewModule.class})
public interface RepositoryPagerViewComponent extends RepositoryPagerViewFragment.Component {
    @Override
    RepositoryDetailsViewComponent plus(RepositoryDetailsViewModule module);
}
