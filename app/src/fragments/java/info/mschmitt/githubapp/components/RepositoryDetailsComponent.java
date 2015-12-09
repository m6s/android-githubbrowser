package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.modules.RepositoryDetailsModule;
import info.mschmitt.githubapp.ui.RepositoryDetailsFragment;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositoryDetailsModule.class})
public interface RepositoryDetailsComponent extends RepositoryDetailsFragment.Component {
}
