package info.mschmitt.githubapp.dagger;

import android.content.res.Resources;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.BuildConfig;
import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.network.Network;

/**
 * @author Matthias Schmitt
 */
@Module
public class DefaultNetworkModule {
    @Provides
    @GitHubApplicationScope
    Network provideNetwork(Resources resources) {
        return new Network(resources.getString(R.string.base_url), BuildConfig.DEBUG);
    }
}
