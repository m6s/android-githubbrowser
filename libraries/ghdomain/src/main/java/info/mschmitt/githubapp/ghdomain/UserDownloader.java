package info.mschmitt.githubapp.ghdomain;

import javax.inject.Inject;
import javax.inject.Singleton;

import info.mschmitt.githubapp.entities.User;
import info.mschmitt.githubapp.network.GitHubService;
import rx.Single;

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

    public Single<User> download(String username) {
        return mGitHubService.getUser(username)
                .map(response -> User.builder().id(response.id).login(response.login)
                        .url(response.url).email(response.email).build());
    }
}
