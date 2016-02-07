package info.mschmitt.githubapp.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * @author Matthias Schmitt
 */
@Module
class RetrofitNetworkModule {
    private final boolean mDebug;
    private String mEndpoint;

    public RetrofitNetworkModule(String endpoint, boolean debug) {
        mEndpoint = endpoint;
        mDebug = debug;
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
    Gson provideGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(OkHttpClient okHttpClient, Gson gson) {
        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setClient(new OkClient(okHttpClient)).setEndpoint(mEndpoint)
                .setConverter(new GsonConverter(gson));
        if (mDebug) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        return builder.build();
    }

    @Provides
    @Singleton
    GitHubService provideGitHubService(RestAdapter restAdapter) {
        return restAdapter.create(GitHubService.class);
    }
}
