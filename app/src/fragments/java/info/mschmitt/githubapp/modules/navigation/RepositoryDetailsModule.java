package info.mschmitt.githubapp.modules.navigation;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.AnalyticsManager;
import info.mschmitt.githubapp.presenters.RepositoryDetailsViewPresenter;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryDetailsModule {
    private RepositoryDetailsViewPresenter.RepositoryDetailsView mView;

    public RepositoryDetailsModule(RepositoryDetailsViewPresenter.RepositoryDetailsView view) {
        mView = view;
    }

    @Provides
    @Singleton
    public RepositoryDetailsViewPresenter providePresenter(AnalyticsManager analyticsManager) {
        return new RepositoryDetailsViewPresenter(mView, analyticsManager);
    }
}
