package info.mschmitt.githubbrowser.app.dagger;

import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubbrowser.BuildConfig;
import info.mschmitt.githubbrowser.R;
import info.mschmitt.githubbrowser.network.Network;

/**
 * @author Matthias Schmitt
 */
@Module
public class DefaultNetworkModule {
    @Provides
    @Singleton
    Network provideNetwork(Resources resources) {
        return new Network(resources.getString(R.string.base_url), BuildConfig.DEBUG);
    }
}
