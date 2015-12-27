package info.mschmitt.githubapp.app;

import info.mschmitt.githubapp.components.ApplicationComponent;
import info.mschmitt.githubapp.components.DaggerApplicationComponent;
import info.mschmitt.githubapp.modules.ApplicationModule;
import info.mschmitt.githubapp.modules.NetworkModule;

/**
 * @author Matthias Schmitt
 */
public class FragmentApplication extends android.app.Application
        implements RootFragment.Application {
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent =
                DaggerApplicationComponent.builder().networkModule(new NetworkModule(this))
                        .applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    @Override
    public RootFragment.SuperComponent getSuperComponent(RootFragment fragment) {
        return mApplicationComponent;
    }
}
