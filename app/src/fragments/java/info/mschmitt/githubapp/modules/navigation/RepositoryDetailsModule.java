package info.mschmitt.githubapp.modules.navigation;

import java.util.LinkedHashMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.AnalyticsManager;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.presenters.RepositoryDetailsViewPresenter;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryDetailsModule {
    private RepositoryDetailsViewPresenter.RepositoryDetailsView mView;
    private long mRepositoryId;

    public RepositoryDetailsModule(RepositoryDetailsViewPresenter.RepositoryDetailsView view,
                                   long repositoryId) {
        mView = view;
        mRepositoryId = repositoryId;
    }

    @Provides
    @Singleton
    public RepositoryDetailsViewPresenter providePresenter(
            Observable<LinkedHashMap<Long, Repository>> repositories, AnalyticsManager analyticsManager) {
        return new RepositoryDetailsViewPresenter(mView, repositories, mRepositoryId,
                analyticsManager);
    }
}
