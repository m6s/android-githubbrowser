package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.MainActivity;
import info.mschmitt.githubapp.modules.MainActivityModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {MainActivityModule.class})
public interface MainActivityComponent extends MainActivity.Component {
}
