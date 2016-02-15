package info.mschmitt.githubapp.network;

import okhttp3.OkHttpClient;

/**
 * @author Matthias Schmitt
 */
public class NetworkProvider {
    private final RetrofitNetworkComponent mRetrofitNetworkComponent;

    public NetworkProvider(String baseUrl, boolean debug) {
        this(DaggerRetrofitNetworkComponent.builder()
                .retrofitNetworkModule(new RetrofitNetworkModule(baseUrl, debug)).build());
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
