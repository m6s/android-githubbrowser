package info.mschmitt.githubapp.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.entities.User;
import info.mschmitt.githubapp.network.responses.GetRepositoriesResponseItem;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
public class GitHubService {
    private GitHubRetrofitService mGitHubRetrofitService;

    public GitHubService(GitHubRetrofitService gitHubRetrofitService) {
        mGitHubRetrofitService = gitHubRetrofitService;
    }

    public Observable<User> getUser(String username) {
        return mGitHubRetrofitService.getUser(username)
                .map(response -> new User(response.login, response.id, response.url,
                        response.email));
    }

    public Observable<List<Repository>> getUserRepositories(String username) {
        return mGitHubRetrofitService.getUserRepositories(username).map(response -> {
            ArrayList<Repository> repositories = new ArrayList<>(response.size());
            for (GetRepositoriesResponseItem item : response) {
                Repository repository = new Repository(item.id, item.name, item.url);
                repositories.add(repository);
            }
            return Collections.unmodifiableList(repositories);
        });
    }
}
