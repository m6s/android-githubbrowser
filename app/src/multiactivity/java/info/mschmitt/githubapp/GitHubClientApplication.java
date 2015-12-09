package info.mschmitt.githubapp;

import android.app.Application;

import info.mschmitt.githubapp.components.DaggerGitHubBrowserApplicationComponent;
import info.mschmitt.githubapp.components.GitHubBrowserApplicationComponent;
import info.mschmitt.githubapp.modules.GitHubBrowserApplicationModule;
import info.mschmitt.githubapp.modules.NetworkModule;

/**
 * @author Matthias Schmitt
 */
public class GitHubClientApplication extends Application {
    private GitHubBrowserApplicationComponent mGitHubBrowserApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mGitHubBrowserApplicationComponent = DaggerGitHubBrowserApplicationComponent.builder().networkModule(new NetworkModule(this))
                .appModule(new GitHubBrowserApplicationModule(this)).build();
    }

    public GitHubBrowserApplicationComponent getGitHubBrowserApplicationComponent() {
        return mGitHubBrowserApplicationComponent;
    }
}
