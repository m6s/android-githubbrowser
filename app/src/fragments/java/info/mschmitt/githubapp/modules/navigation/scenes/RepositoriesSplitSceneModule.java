package info.mschmitt.githubapp.modules.navigation.scenes;

import java.util.LinkedHashMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.AnalyticsManager;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.presenters.navigation.scenes.RepositoriesSplitSceneViewPresenter;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoriesSplitSceneModule {
    private RepositoriesSplitSceneViewPresenter.RepositoriesSplitSceneView mView;
    private String mUsername;

    public RepositoriesSplitSceneModule(
            RepositoriesSplitSceneViewPresenter.RepositoriesSplitSceneView view, String username) {
        mView = view;
        mUsername = username;
    }

    @Provides
    Observable<LinkedHashMap<Long, Repository>> provideRepositories(
            RepositoriesSplitSceneViewPresenter presenter) {
        return presenter.getRepositories();
    }

    @Provides
    @Singleton
    public RepositoriesSplitSceneViewPresenter providePresenter(GitHubService gitHubService,
                                                                AnalyticsManager analyticsManager) {
        return new RepositoriesSplitSceneViewPresenter(mUsername, mView, gitHubService,
                analyticsManager);
    }
}
