package info.mschmitt.githubapp.network;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * @author Matthias Schmitt
 */
@Module
class RetrofitNetworkModule {
    private String mEndpoint;

    public RetrofitNetworkModule(String endpoint) {
        mEndpoint = endpoint;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        return okHttpClient;
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(OkHttpClient okHttpClient) {
        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setClient(new OkClient(okHttpClient)).setEndpoint(mEndpoint);
        if (BuildConfig.DEBUG) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        return builder.build();
    }

    @Provides
    @Singleton
    GitHubRetrofitService provideGitHubRetrofitService(RestAdapter restAdapter) {
        return restAdapter.create(GitHubRetrofitService.class);
    }

    @Provides
    @Singleton
    GitHubService provideGitHubService(GitHubRetrofitService gitHubRetrofitService) {
        return new GitHubService(gitHubRetrofitService);
    }
}
