package info.mschmitt.githubapp.domain;

import javax.inject.Inject;
import javax.inject.Singleton;

import info.mschmitt.githubapp.entities.User;
import info.mschmitt.githubapp.network.GitHubService;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
@Singleton
public class UserDownloader {
    private final GitHubService mGitHubService;

    @Inject
    public UserDownloader(GitHubService gitHubService) {
        mGitHubService = gitHubService;
    }

    public Observable<User> download(String username) {
        return mGitHubService.getUser(username)
                .map(response -> new User(response.login, response.id, response.url,
                        response.email));
    }
}
