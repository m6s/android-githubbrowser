package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import info.mschmitt.githubapp.app.FragmentActivity;
import info.mschmitt.githubapp.modules.ActivityModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent extends FragmentActivity.Component {
}
