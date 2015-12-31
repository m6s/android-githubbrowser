package info.mschmitt.githubapp.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.di.MainActivityModule;
import info.mschmitt.githubapp.utils.LoadingProgressManager;

public class MainActivity extends AppCompatActivity implements RootViewFragment.FragmentHost {
    private NavigationManager mNavigationManager;
    private LoadingProgressManager mLoadingProgressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Application) getApplication()).getComponent().plus(new MainActivityModule())
                .inject(this);
        mNavigationManager.onMainActivityCreated(this);
        setContentView(R.layout.main_activity);
    }

    @Override
    protected void onDestroy() {
        mNavigationManager.onMainActivityDestroyed();
        super.onDestroy();
    }

    @Inject
    public void setNavigationManager(NavigationManager navigationManager) {
        mNavigationManager = navigationManager;
    }

    @Inject
    public void setLoadingProgressManager(LoadingProgressManager loadingProgressManager) {
        mLoadingProgressManager = loadingProgressManager;
    }

    @Override
    public void onBackPressed() {
        if (mLoadingProgressManager.cancelAllTasks(true)) {
            return;
        }
        if (!mNavigationManager.goBack()) {
            super.onBackPressed();
        }
    }

    public interface Component {
        void inject(MainActivity activity);
    }

    public interface SuperComponent {
        Component plus(MainActivityModule module);
    }

    public interface Application {
        SuperComponent getComponent();
    }
}
