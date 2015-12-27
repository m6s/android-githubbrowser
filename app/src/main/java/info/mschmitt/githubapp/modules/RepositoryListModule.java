package info.mschmitt.githubapp.modules;

import java.util.LinkedHashMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.presenters.RepositoryListPresenter;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryListModule {
    @Provides
    @Singleton
    public RepositoryListPresenter providePresenter(
            Observable<LinkedHashMap<Long, Repository>> repositories) {
        return new RepositoryListPresenter(repositories);
    }
}
