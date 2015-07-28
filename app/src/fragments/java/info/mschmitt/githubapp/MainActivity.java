package info.mschmitt.githubapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import info.mschmitt.githubapp.android.presentation.OnBackPressedListener;
import info.mschmitt.githubapp.android.presentation.Presentable;
import info.mschmitt.githubapp.fragments.navigation.MainFragment;
import info.mschmitt.githubapp.presenters.navigation.MainViewPresenter;

public class MainActivity extends AppCompatActivity implements MainFragment.FragmentHost {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
    }

    @Override
    public void onBackPressed() {
        Presentable presentable =
                (Presentable) getSupportFragmentManager().findFragmentById(R.id.fragment);
        Object presenter = presentable.getPresenter();
        boolean handled = presenter instanceof OnBackPressedListener &&
                ((OnBackPressedListener) presenter).onBackPressed();
        if (!handled) {
            super.onBackPressed();
        }
    }

    @Override
    public MainViewPresenter.ParentPresenter getPresenter() {
        return null;
    }
}
