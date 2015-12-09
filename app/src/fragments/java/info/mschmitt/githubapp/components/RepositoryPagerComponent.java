package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.app.RepositoryPagerFragment;
import info.mschmitt.githubapp.modules.RepositoryDetailsModule;
import info.mschmitt.githubapp.modules.RepositoryPagerModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositoryPagerModule.class})
public interface RepositoryPagerComponent extends RepositoryPagerFragment.Component {
    @Override
    RepositoryDetailsComponent plus(RepositoryDetailsModule module);
}
