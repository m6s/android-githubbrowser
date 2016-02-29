package info.mschmitt.githubapp.ghdomain;

import android.content.Context;

import info.mschmitt.githubapp.ghdomain.internal.dagger.DaggerDomainComponent;
import info.mschmitt.githubapp.ghdomain.internal.dagger.DomainComponent;
import info.mschmitt.githubapp.ghdomain.internal.dagger.DomainModule;
import info.mschmitt.githubapp.network.Network;

/**
 * @author Matthias Schmitt
 */
public class Domain {
    private final DomainComponent mDomainComponent;

    public Domain(Network network, Context context, boolean debug) {
        mDomainComponent = DaggerDomainComponent.builder()
                .domainModule(new DomainModule(network, context, debug)).build();
    }

    public AnalyticsService getAnalyticsService() {
        return mDomainComponent.getAnalyticsService();
    }

    public RepositoryDownloader getRepositoryDownloader() {
        return mDomainComponent.getRepositoryDownloader();
    }

    public UserDownloader getUserDownloader() {
        return mDomainComponent.getUserDownloader();
    }

    public Validator getValidator() {
        return mDomainComponent.getValidator();
    }
}
