package info.mschmitt.githubapp.modules;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.domain.AnalyticsManager;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.presenters.RepositorySplitPresenter;
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
    RepositorySplitPresenter provideRepositoriesSplitSceneViewPresenter(
            GitHubService gitHubService, AnalyticsManager analyticsManager) {
        RepositorySplitPresenter repositorySplitPresenter =
                new RepositorySplitPresenter(gitHubService, analyticsManager);
        repositorySplitPresenter.setUsername(mUsername);
        return repositorySplitPresenter;
    }


    @Provides
    Observable<List<Repository>> provideRepositories(RepositorySplitPresenter presenter) {
        return presenter.getRepositories();
    }
}
