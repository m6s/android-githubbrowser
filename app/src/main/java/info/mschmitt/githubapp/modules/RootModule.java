package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.presenters.RootPresenter;

/**
 * @author Matthias Schmitt
 */
@Module
public class RootModule {
    @Provides
    @Singleton
    public RootPresenter providePresenter() {
        return new RootPresenter();
    }
}
