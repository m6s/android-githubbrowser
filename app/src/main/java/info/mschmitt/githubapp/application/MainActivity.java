package info.mschmitt.githubapp.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.di.MainActivityModule;

public class MainActivity extends AppCompatActivity implements RootViewFragment.FragmentHost {
    private NavigationManager mNavigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Application) getApplication()).getSuperComponent(this).plus(new MainActivityModule())
                .inject(this);
        mNavigationManager.onCreate(this);
        setContentView(R.layout.main_activity);
    }

    @Override
    protected void onDestroy() {
        mNavigationManager.onDestroy(this);
        super.onDestroy();
    }

    @Inject
    public void setNavigationManager(NavigationManager navigationManager) {
        mNavigationManager = navigationManager;
    }

    @Override
    public void onBackPressed() {
        if (!mNavigationManager.onBackPressed()) {
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
        SuperComponent getSuperComponent(MainActivity activity);
    }
}
