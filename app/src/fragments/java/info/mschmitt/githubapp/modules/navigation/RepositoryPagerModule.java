package info.mschmitt.githubapp.modules.navigation;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.AnalyticsManager;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.presenters.RepositoryPagerViewPresenter;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryPagerModule {
    private RepositoryPagerViewPresenter.RepositoryPagerView mView;

    public RepositoryPagerModule(RepositoryPagerViewPresenter.RepositoryPagerView view) {
        mView = view;
    }

    @Provides
    @Singleton
    public RepositoryPagerViewPresenter providePresenter(Observable<List<Repository>> repositories,
                                                         AnalyticsManager analyticsManager) {
        return new RepositoryPagerViewPresenter(mView, repositories, analyticsManager);
    }
}
