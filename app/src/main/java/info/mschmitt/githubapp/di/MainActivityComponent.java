package info.mschmitt.githubapp.di;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.application.MainActivity;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {MainActivityModule.class})
public interface MainActivityComponent extends MainActivity.Component {
}
