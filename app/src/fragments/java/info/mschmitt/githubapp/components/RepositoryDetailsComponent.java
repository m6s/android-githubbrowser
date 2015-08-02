package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.fragments.RepositoryDetailsFragment;
import info.mschmitt.githubapp.modules.RepositoryDetailsModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {RepositoryDetailsModule.class})
public interface RepositoryDetailsComponent extends RepositoryDetailsFragment.Component {
}
