package info.mschmitt.githubapp.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.network.responses.GetRepositoriesResponseItem;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
@Singleton
public class RepositoryDownloader {
    private final GitHubService mGitHubService;

    @Inject
    public RepositoryDownloader(GitHubService gitHubService) {
        mGitHubService = gitHubService;
    }

    public Observable<LinkedHashMap<Long, Repository>> download(String username) {
        return mGitHubService.getUserRepositories(username).map(response -> {
            ArrayList<Repository> repositories = new ArrayList<>(response.size());
            for (GetRepositoriesResponseItem item : response) {
                Repository repository = new Repository(item.id, item.name, item.url);
                repositories.add(repository);
            }
            return RepositoryDownloader.indexById(repositories);
        });
    }

    private static LinkedHashMap<Long, Repository> indexById(List<Repository> repositories) {
//        long seed = System.nanoTime();
//        repositories = new ArrayList<>(repositories);
//        Collections.shuffle(repositories, new Random(seed));
        LinkedHashMap<Long, Repository> map = new LinkedHashMap<>();
        for (Repository repository : repositories) {
            map.put(repository.getId(), repository);
        }
        return map;
    }
}