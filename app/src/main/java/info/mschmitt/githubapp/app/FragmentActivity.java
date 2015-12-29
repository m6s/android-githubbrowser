package info.mschmitt.githubapp.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.modules.ActivityModule;

public class FragmentActivity extends AppCompatActivity implements RootFragment.FragmentHost {
    private NavigationManager mNavigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Application) getApplication()).getSuperComponent(this).plus(new ActivityModule())
                .inject(this);
        mNavigationManager.onCreate(this);
        setContentView(R.layout.activity);
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
        void inject(FragmentActivity activity);
    }

    public interface SuperComponent {
        Component plus(ActivityModule module);
    }

    public interface Application {
        SuperComponent getSuperComponent(FragmentActivity activity);
    }
}
