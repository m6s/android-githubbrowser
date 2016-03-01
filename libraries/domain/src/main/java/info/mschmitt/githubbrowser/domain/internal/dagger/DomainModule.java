package info.mschmitt.githubbrowser.domain.internal.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubbrowser.network.GitHubService;
import info.mschmitt.githubbrowser.network.Network;

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
