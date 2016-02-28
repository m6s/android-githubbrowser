package info.mschmitt.githubapp.dagger;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.RepositoryDetailsViewFragment;

/**
 * @author Matthias Schmitt
 */
@RepositoryDetailsViewScope
@Subcomponent(modules = {RepositoryDetailsViewModule.class})
public interface RepositoryDetailsViewComponent extends RepositoryDetailsViewFragment.Component {
}
