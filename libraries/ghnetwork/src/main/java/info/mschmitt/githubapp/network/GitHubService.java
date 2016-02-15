package info.mschmitt.githubapp.network;

import java.util.List;

import info.mschmitt.githubapp.network.responses.GetRepositoriesResponseItem;
import info.mschmitt.githubapp.network.responses.GetUserResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Single;

/**
 * @author Matthias Schmitt
 */
public interface GitHubService {
    @GET("users/{username}")
    Single<GetUserResponse> getUser(@Path("username") String username);

    @GET("users/{username}/repos")
    Single<List<GetRepositoriesResponseItem>> getUserRepositories(
            @Path("username") String username);
}
