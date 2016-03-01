package info.mschmitt.githubbrowser.domain;

import javax.inject.Inject;
import javax.inject.Singleton;

import info.mschmitt.githubbrowser.network.GitHubService;
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

    public Single<info.mschmitt.githubbrowser.entities.User> download(String username) {
        return mGitHubService.getUser(username)
                .map(response -> info.mschmitt.githubbrowser.entities.User.builder().id(response.id)
                        .login(response.login)
                        .url(response.url).email(response.email).build());
    }
}
