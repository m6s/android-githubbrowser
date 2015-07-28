package info.mschmitt.githubapp.network;

import com.squareup.okhttp.OkHttpClient;

/**
 * @author Matthias Schmitt
 */
public class NetworkProvider {
    private final RetrofitNetworkComponent mRetrofitNetworkComponent;

    public NetworkProvider(String endpoint) {
        this(DaggerRetrofitNetworkComponent.builder()
                .retrofitNetworkModule(new RetrofitNetworkModule(endpoint)).build());
    }

    public NetworkProvider(RetrofitNetworkComponent retrofitNetworkComponent) {
        mRetrofitNetworkComponent = retrofitNetworkComponent;
    }

    public GitHubService getGitHubService() {
        return mRetrofitNetworkComponent.getGitHubService();
    }

    public OkHttpClient getOkHttpClient() {
        return mRetrofitNetworkComponent.getOkHttpClient();
    }
}
