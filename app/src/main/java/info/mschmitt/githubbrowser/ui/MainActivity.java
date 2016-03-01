package info.mschmitt.githubbrowser.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import info.mschmitt.githubbrowser.ui.fragments.RootViewFragment;

public class MainActivity extends AppCompatActivity implements RootViewFragment.FragmentHost {
    private RootViewFragment mRootViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(info.mschmitt.githubbrowser.R.layout.main_activity);
    }

    @Override
    public void onBackPressed() {
        if (!mRootViewFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        mRootViewFragment = (RootViewFragment) fragment;
        super.onAttachFragment(fragment);
    }

    public interface Application {
    }
}
