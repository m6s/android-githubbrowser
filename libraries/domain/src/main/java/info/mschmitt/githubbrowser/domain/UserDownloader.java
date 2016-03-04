package info.mschmitt.githubbrowser.domain;

import javax.inject.Inject;
import javax.inject.Singleton;

import info.mschmitt.githubbrowser.entities.User;
import info.mschmitt.githubbrowser.network.GitHubService;
import info.mschmitt.githubbrowser.network.responses.GetUserResponse;
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
        return mGitHubService.getUser(username).map(this::processDownloadResponse);
    }

    private User processDownloadResponse(GetUserResponse response) {
        return User.builder().id(response.id).login(response.login).url(response.url)
                .email(response.email).build();
    }
}
