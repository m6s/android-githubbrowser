package info.mschmitt.githubbrowser.domain;

import java.util.LinkedHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import info.mschmitt.githubbrowser.entities.Repository;
import info.mschmitt.githubbrowser.network.GitHubService;
import info.mschmitt.githubbrowser.network.responses.GetRepositoriesResponseItem;
import rx.Single;

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

    public Single<LinkedHashMap<Long, Repository>> download(String username) {
        return mGitHubService.getUserRepositories(username).map(response -> {
            LinkedHashMap<Long, Repository> map = new LinkedHashMap<>();
            for (GetRepositoriesResponseItem item : response) {
                map.put(item.id,
                        Repository.builder().id(item.id).name(item.name).url(item.url).build());
            }
            return map;
        });
    }
}
