package info.mschmitt.githubapp.modules;

import java.util.LinkedHashMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.presenters.RepositoryListViewPresenter;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoryListModule {
    private RepositoryListViewPresenter.RepositoryListView mView;

    public RepositoryListModule(RepositoryListViewPresenter.RepositoryListView view) {
        mView = view;
    }

    @Provides
    @Singleton
    public RepositoryListViewPresenter providePresenter(
            Observable<LinkedHashMap<Long, Repository>> repositories) {
        return new RepositoryListViewPresenter(mView, repositories);
    }
}
