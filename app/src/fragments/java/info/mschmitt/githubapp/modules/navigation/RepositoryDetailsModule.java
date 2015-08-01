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
    private final int mPosition;
    private final RepositoryDetailsViewPresenter.RepositoryDetailsView mView;

    public RepositoryDetailsModule(RepositoryDetailsViewPresenter.RepositoryDetailsView view,
                                   int position) {
        mView = view;
        mPosition = position;
    }

    @Provides
    @Singleton
    public RepositoryDetailsViewPresenter providePresenter(
            Observable<LinkedHashMap<Long, Repository>> repositories,
            AnalyticsManager analyticsManager) {
        return RepositoryDetailsViewPresenter
                .createForRepositoryPosition(mView, repositories, analyticsManager, mPosition);
    }
}
