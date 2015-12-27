package info.mschmitt.githubapp.modules;

import java.util.LinkedHashMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.domain.AnalyticsManager;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.presenters.RepositoryDetailsPresenter;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryDetailsModule {
    @Provides
    @Singleton
    public RepositoryDetailsPresenter providePresenter(
            Observable<LinkedHashMap<Long, Repository>> repositories,
            AnalyticsManager analyticsManager) {
        return new RepositoryDetailsPresenter(repositories, analyticsManager);
    }
}
