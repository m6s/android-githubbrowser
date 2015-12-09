package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.modules.RepositoryListModule;
import info.mschmitt.githubapp.ui.RepositoryListFragment;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositoryListModule.class})
public interface RepositoryListComponent extends RepositoryListFragment.Component {
}
