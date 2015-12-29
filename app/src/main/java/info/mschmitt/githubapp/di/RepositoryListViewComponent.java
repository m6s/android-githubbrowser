package info.mschmitt.githubapp.di;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RepositoryListViewFragment;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositoryListViewModule.class})
public interface RepositoryListViewComponent extends RepositoryListViewFragment.Component {
}
