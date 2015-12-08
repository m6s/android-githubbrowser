package info.mschmitt.githubapp;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubapp.components.MainComponent;
import info.mschmitt.githubapp.fragments.MainFragment;
import info.mschmitt.githubapp.modules.AppModule;
import info.mschmitt.githubapp.modules.NetworkModule;
import info.mschmitt.githubapp.modules.MainModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent extends MainFragment.SuperComponent {
    @Override
    MainComponent plus(MainModule module);
}
