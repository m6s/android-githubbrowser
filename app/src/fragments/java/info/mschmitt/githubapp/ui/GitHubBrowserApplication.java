package info.mschmitt.githubapp.ui;

import info.mschmitt.githubapp.components.DaggerGitHubBrowserApplicationComponent;
import info.mschmitt.githubapp.components.GitHubBrowserApplicationComponent;
import info.mschmitt.githubapp.modules.GitHubBrowserApplicationModule;
import info.mschmitt.githubapp.modules.NetworkModule;

/**
 * @author Matthias Schmitt
 */
public class GitHubBrowserApplication extends android.app.Application
        implements GitHubBrowserFragment.Application {
    private GitHubBrowserApplicationComponent mGitHubBrowserApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mGitHubBrowserApplicationComponent = DaggerGitHubBrowserApplicationComponent.builder()
                .networkModule(new NetworkModule(this))
                .gitHubBrowserApplicationModule(new GitHubBrowserApplicationModule(this)).build();
    }

    public GitHubBrowserApplicationComponent getGitHubBrowserApplicationComponent() {
        return mGitHubBrowserApplicationComponent;
    }

    @Override
    public GitHubBrowserFragment.SuperComponent getSuperComponent(GitHubBrowserFragment fragment) {
        return mGitHubBrowserApplicationComponent;
    }
}
