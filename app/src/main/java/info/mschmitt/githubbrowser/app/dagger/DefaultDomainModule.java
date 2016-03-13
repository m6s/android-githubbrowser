package info.mschmitt.githubbrowser.app.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubbrowser.BuildConfig;
import info.mschmitt.githubbrowser.domain.Domain;
import info.mschmitt.githubbrowser.domain.RepositoryDownloader;
import info.mschmitt.githubbrowser.domain.UserDownloader;
import info.mschmitt.githubbrowser.domain.Validator;
import info.mschmitt.githubbrowser.network.Network;

/**
 * @author Matthias Schmitt
 */
@Module
public class DefaultDomainModule {
    @Provides
    @Singleton
    Domain provideDomain(Network network, @ApplicationContext Context context) {
        return new Domain(network, context, BuildConfig.DEBUG);
    }

    @Provides
    @Singleton
    Validator provideValidator(Domain domain) {
        return domain.getValidator();
    }

    @Provides
    @Singleton
    RepositoryDownloader provideRepositoryDownloader(Domain domain) {
        return domain.getRepositoryDownloader();
    }

    @Provides
    @Singleton
    UserDownloader provideUserDownloader(Domain domain) {
        return domain.getUserDownloader();
    }
}
