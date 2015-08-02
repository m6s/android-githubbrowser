package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.fragments.RepositoryListFragment;
import info.mschmitt.githubapp.modules.RepositoryListModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositoryListModule.class})
public interface RepositoryListComponent extends RepositoryListFragment.Component {
}
