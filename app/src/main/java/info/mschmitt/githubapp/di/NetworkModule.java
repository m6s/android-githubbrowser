package info.mschmitt.githubapp.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.BuildConfig;
import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.network.NetworkProvider;

/**
 * @author Matthias Schmitt
 */
@Module
public class NetworkModule {
    private final NetworkProvider mNetworkProvider;

    public NetworkModule(Context context) {
        mNetworkProvider =
                new NetworkProvider(context.getString(R.string.endpoint), BuildConfig.DEBUG);
    }

    @Provides
    @Singleton
    GitHubService provideGitHubService() {
        return mNetworkProvider.getGitHubService();
    }
}
