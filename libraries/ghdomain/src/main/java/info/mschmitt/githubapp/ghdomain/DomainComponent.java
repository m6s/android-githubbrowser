package info.mschmitt.githubapp.ghdomain;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Component(modules = {DomainModule.class})
interface DomainComponent {
    AnalyticsService getAnalyticsService();

    RepositoryDownloader getRepositoryDownloader();

    UserDownloader getUserDownloader();

    Validator getValidator();
}
