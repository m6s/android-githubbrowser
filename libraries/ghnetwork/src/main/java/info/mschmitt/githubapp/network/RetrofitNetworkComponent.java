package info.mschmitt.githubapp.network;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Component(modules = {RetrofitNetworkModule.class})
public interface RetrofitNetworkComponent {
    OkHttpClient getOkHttpClient();

    GitHubService getGitHubService();
}
