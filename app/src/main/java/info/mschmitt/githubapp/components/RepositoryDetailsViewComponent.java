package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.app.RepositoryDetailsViewFragment;
import info.mschmitt.githubapp.modules.RepositoryDetailsViewModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositoryDetailsViewModule.class})
public interface RepositoryDetailsViewComponent extends RepositoryDetailsViewFragment.Component {
}
