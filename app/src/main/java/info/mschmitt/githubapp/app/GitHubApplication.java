package info.mschmitt.githubapp.app;

import info.mschmitt.githubapp.components.DaggerGitHubApplicationComponent;
import info.mschmitt.githubapp.modules.GitHubApplicationModule;
import info.mschmitt.githubapp.modules.NetworkModule;

/**
 * @author Matthias Schmitt
 */
public class GitHubApplication extends android.app.Application
        implements RootViewFragment.Application, MainActivity.Application {
    private Component mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent =
                DaggerGitHubApplicationComponent.builder().networkModule(new NetworkModule(this))
                        .gitHubApplicationModule(new GitHubApplicationModule(this)).build();
        mApplicationComponent.inject(this);
    }

    @Override
    public RootViewFragment.SuperComponent getSuperComponent(RootViewFragment fragment) {
        return mApplicationComponent;
    }

    @Override
    public MainActivity.SuperComponent getSuperComponent(MainActivity activity) {
        return mApplicationComponent;
    }

    public interface Component
            extends RootViewFragment.SuperComponent, MainActivity.SuperComponent {
        void inject(GitHubApplication gitHubApplication);
    }
}
