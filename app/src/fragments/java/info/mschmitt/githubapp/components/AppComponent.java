package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubapp.fragments.navigation.MainFragment;
import info.mschmitt.githubapp.modules.AppModule;
import info.mschmitt.githubapp.modules.MainModule;
import info.mschmitt.githubapp.modules.NetworkModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent extends MainFragment.SuperComponent {
    MainComponent plus(MainModule module);

    void inject(MainFragment fragment);
}
