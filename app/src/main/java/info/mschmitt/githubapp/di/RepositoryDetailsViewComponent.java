package info.mschmitt.githubapp.di;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RepositoryDetailsViewFragment;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositoryDetailsViewModule.class})
public interface RepositoryDetailsViewComponent extends RepositoryDetailsViewFragment.Component {
}
