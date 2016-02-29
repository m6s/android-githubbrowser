package info.mschmitt.githubapp.ghdomain.internal.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.network.GitHubService;
import info.mschmitt.githubapp.network.Network;

/**
 * @author Matthias Schmitt
 */
@Module
public class DomainModule {
    private final Network mNetwork;
    private final boolean mDebug;
    private final Context mContext;

    public DomainModule(Network network, Context context, boolean debug) {
        mNetwork = network;
        mDebug = debug;
        mContext = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    GitHubService provideGitHubService() {
        return mNetwork.getGitHubService();
    }
}
