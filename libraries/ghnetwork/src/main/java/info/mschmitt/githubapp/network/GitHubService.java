package info.mschmitt.githubapp.network;

import java.util.List;

import info.mschmitt.githubapp.network.responses.GetRepositoriesResponseItem;
import info.mschmitt.githubapp.network.responses.GetUserResponse;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
public interface GitHubService {
    @GET("/users/{username}")
    Observable<GetUserResponse> getUser(@Path("username") String username);

    @GET("/users/{username}/repos")
    Observable<List<GetRepositoriesResponseItem>> getUserRepositories(
            @Path("username") String username);
}
