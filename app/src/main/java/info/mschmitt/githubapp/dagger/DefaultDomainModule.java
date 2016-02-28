package info.mschmitt.githubapp.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.BuildConfig;
import info.mschmitt.githubapp.application.qualifiers.ApplicationContext;
import info.mschmitt.githubapp.ghdomain.AnalyticsService;
import info.mschmitt.githubapp.ghdomain.Domain;
import info.mschmitt.githubapp.ghdomain.RepositoryDownloader;
import info.mschmitt.githubapp.ghdomain.UserDownloader;
import info.mschmitt.githubapp.ghdomain.Validator;
import info.mschmitt.githubapp.network.Network;

/**
 * @author Matthias Schmitt
 */
@Module
public class DefaultDomainModule {
    @Provides
    @GitHubApplicationScope
    Domain provideDomain(Network network, @ApplicationContext Context context) {
        return new Domain(network, context, BuildConfig.DEBUG);
    }

    @Provides
    @GitHubApplicationScope
    AnalyticsService provideAnalyticsService(Domain domain) {
        return domain.getAnalyticsService();
    }

    @Provides
    @GitHubApplicationScope
    Validator provideValidator(Domain domain) {
        return domain.getValidator();
    }

    @Provides
    @GitHubApplicationScope
    RepositoryDownloader provideRepositoryDownloader(Domain domain) {
        return domain.getRepositoryDownloader();
    }

    @Provides
    @GitHubApplicationScope
    UserDownloader provideUserDownloader(Domain domain) {
        return domain.getUserDownloader();
    }
}
