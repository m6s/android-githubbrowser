package info.mschmitt.githubbrowser.domain.internal.dagger;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubbrowser.domain.AnalyticsService;
import info.mschmitt.githubbrowser.domain.RepositoryDownloader;
import info.mschmitt.githubbrowser.domain.UserDownloader;
import info.mschmitt.githubbrowser.domain.Validator;

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
