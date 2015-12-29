package info.mschmitt.githubapp.components;

import javax.inject.Singleton;

import dagger.Component;
import info.mschmitt.githubapp.app.FragmentActivity;
import info.mschmitt.githubapp.app.RootFragment;
import info.mschmitt.githubapp.modules.ActivityModule;
import info.mschmitt.githubapp.modules.ApplicationModule;
import info.mschmitt.githubapp.modules.NetworkModule;
import info.mschmitt.githubapp.modules.RootModule;

/**
 * @author Matthias Schmitt
 */
@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent
        extends RootFragment.SuperComponent, FragmentActivity.SuperComponent {
    @Override
    RootComponent plus(RootModule module);

    @Override
    ActivityComponent plus(ActivityModule module);
}
