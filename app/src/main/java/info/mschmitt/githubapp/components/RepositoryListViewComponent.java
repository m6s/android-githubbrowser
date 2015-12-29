package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.app.RepositoryListViewFragment;
import info.mschmitt.githubapp.modules.RepositoryListViewModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositoryListViewModule.class})
public interface RepositoryListViewComponent extends RepositoryListViewFragment.Component {
}
