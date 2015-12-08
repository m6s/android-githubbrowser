package info.mschmitt.githubapp.modules;

import java.util.List;

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
public class RepositoryListActivityModule {
    private String mUsername;

    public RepositoryListActivityModule(String username) {
        mUsername = username;
    }

    @Provides
    RepositoriesSplitViewPresenter provideRepositoriesSplitSceneViewPresenter(
            GitHubService gitHubService, AnalyticsManager analyticsManager) {
        RepositoriesSplitViewPresenter repositoriesSplitViewPresenter =
                new RepositoriesSplitViewPresenter(gitHubService, analyticsManager);
        repositoriesSplitViewPresenter.setUsername(mUsername);
        return repositoriesSplitViewPresenter;
    }


    @Provides
    Observable<List<Repository>> provideRepositories(RepositoriesSplitViewPresenter presenter) {
        return presenter.getRepositories();
    }
}
