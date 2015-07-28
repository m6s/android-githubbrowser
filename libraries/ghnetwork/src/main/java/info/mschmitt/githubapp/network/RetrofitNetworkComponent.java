package info.mschmitt.githubapp.network;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Component(modules = {RetrofitNetworkModule.class})
public interface RetrofitNetworkComponent {
    OkHttpClient getOkHttpClient();

    GitHubService getGitHubService();
}
