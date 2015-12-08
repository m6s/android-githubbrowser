package info.mschmitt.githubapp.modules;

import java.util.LinkedHashMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.AnalyticsManager;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.presenters.RepositoriesSplitViewPresenter;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoriesSplitSceneModule {
    private RepositoriesSplitViewPresenter.RepositoriesSplitSceneView mView;
    private String mUsername;

    public RepositoriesSplitSceneModule(
            RepositoriesSplitViewPresenter.RepositoriesSplitSceneView view, String username) {
        mView = view;
        mUsername = username;
    }

    @Provides
    Observable<LinkedHashMap<Long, Repository>> provideRepositories(
            RepositoriesSplitViewPresenter presenter) {
        return presenter.getRepositories();
    }

    @Provides
    @Singleton
    public RepositoriesSplitViewPresenter providePresenter(GitHubService gitHubService,
                                                                AnalyticsManager analyticsManager) {
        return new RepositoriesSplitViewPresenter(mUsername, mView, gitHubService,
                analyticsManager);
    }
}
