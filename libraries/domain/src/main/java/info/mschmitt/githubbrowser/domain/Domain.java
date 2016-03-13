package info.mschmitt.githubbrowser.domain;

import android.content.Context;

import info.mschmitt.githubbrowser.domain.internal.dagger.DaggerDomainComponent;
import info.mschmitt.githubbrowser.domain.internal.dagger.DomainComponent;
import info.mschmitt.githubbrowser.domain.internal.dagger.DomainModule;
import info.mschmitt.githubbrowser.network.Network;

/**
 * @author Matthias Schmitt
 */
public class Domain {
    private final DomainComponent mDomainComponent;

    public Domain(Network network, Context context, boolean debug) {
        mDomainComponent = DaggerDomainComponent.builder()
                .domainModule(new DomainModule(network, context, debug)).build();
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
