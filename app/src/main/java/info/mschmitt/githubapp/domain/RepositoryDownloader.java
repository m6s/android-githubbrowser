package info.mschmitt.githubapp.domain;

import java.util.LinkedHashMap;

import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.network.responses.GetRepositoriesResponseItem;
import rx.Single;

/**
 * @author Matthias Schmitt
 */
public class RepositoryDownloader {
    private final GitHubService mGitHubService;

    public RepositoryDownloader(GitHubService gitHubService) {
        mGitHubService = gitHubService;
    }

    public Single<LinkedHashMap<Long, Repository>> download(String username) {
        return mGitHubService.getUserRepositories(username).map(response -> {
            LinkedHashMap<Long, Repository> map = new LinkedHashMap<>();
            for (GetRepositoriesResponseItem item : response) {
                Repository repository =
                        Repository.builder().id(item.id).name(item.name).url(item.url).build();
                map.put(repository.id(), repository);
            }
            return map;
        });
    }
}
