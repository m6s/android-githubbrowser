package info.mschmitt.githubapp.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RepositoryListViewFragment;

/**
 * @author Matthias Schmitt
 */
@RepositoryListViewScope
@Subcomponent(modules = {RepositoryListViewModule.class})
public interface RepositoryListViewComponent extends RepositoryListViewFragment.Component {
}
