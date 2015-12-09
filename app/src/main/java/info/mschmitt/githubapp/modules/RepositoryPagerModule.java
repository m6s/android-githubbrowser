package info.mschmitt.githubapp.modules;

import java.util.LinkedHashMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.domain.AnalyticsManager;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.presenters.RepositoryPagerPresenter;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryPagerModule {
    private RepositoryPagerPresenter.RepositoryPagerView mView;

    public RepositoryPagerModule(RepositoryPagerPresenter.RepositoryPagerView view) {
        mView = view;
    }

    @Provides
    @Singleton
    public RepositoryPagerPresenter providePresenter(
            Observable<LinkedHashMap<Long, Repository>> repositories,
            AnalyticsManager analyticsManager) {
        return new RepositoryPagerPresenter(mView, repositories, analyticsManager);
    }
}
