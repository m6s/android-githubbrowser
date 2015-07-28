package info.mschmitt.githubapp.modules;

import java.util.List;

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
public class RepositoryListActivityModule {
    private String mUsername;

    public RepositoryListActivityModule(String username) {
        mUsername = username;
    }

    @Provides
    RepositoriesSplitSceneViewPresenter provideRepositoriesSplitSceneViewPresenter(
            GitHubService gitHubService, AnalyticsManager analyticsManager) {
        RepositoriesSplitSceneViewPresenter repositoriesSplitSceneViewPresenter =
                new RepositoriesSplitSceneViewPresenter(gitHubService, analyticsManager);
        repositoriesSplitSceneViewPresenter.setUsername(mUsername);
        return repositoriesSplitSceneViewPresenter;
    }


    @Provides
    Observable<List<Repository>> provideRepositories(RepositoriesSplitSceneViewPresenter presenter) {
        return presenter.getRepositories();
    }
}
