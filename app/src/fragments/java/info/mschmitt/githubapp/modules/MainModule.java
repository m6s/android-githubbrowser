package info.mschmitt.githubapp.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.presenters.navigation.MainViewPresenter;

/**
 * @author Matthias Schmitt
 */
@Module
public class MainModule {
    private MainViewPresenter.MainView mView;

    public MainModule(MainViewPresenter.MainView view) {
        mView = view;
    }

    @Provides
    @Singleton
    public MainViewPresenter providePresenter() {
        return new MainViewPresenter(mView);
    }
}
