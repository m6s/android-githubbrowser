package info.mschmitt.githubapp.ghdomain.internal.dagger;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubapp.ghdomain.AnalyticsService;
import info.mschmitt.githubapp.ghdomain.RepositoryDownloader;
import info.mschmitt.githubapp.ghdomain.UserDownloader;
import info.mschmitt.githubapp.ghdomain.Validator;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Component(modules = {DomainModule.class})
public interface DomainComponent {
    AnalyticsService getAnalyticsService();

    RepositoryDownloader getRepositoryDownloader();

    UserDownloader getUserDownloader();

    Validator getValidator();
}
