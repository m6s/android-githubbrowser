package info.mschmitt.githubbrowser.network.internal.dagger;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubbrowser.network.GitHubService;
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
