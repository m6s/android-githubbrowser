package info.mschmitt.githubapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import info.mschmitt.githubapp.components.UsernameActivityComponent;
import info.mschmitt.githubapp.databinding.UsernameViewBinding;
import info.mschmitt.githubapp.modules.UsernameActivityModule;
import info.mschmitt.githubapp.presenters.UsernameViewPresenter;

/**
 * @author Matthias Schmitt
 */
public class UsernameActivity extends AppCompatActivity
        implements UsernameViewPresenter.UsernameView, UsernameViewPresenter.ParentPresenter {
    private UsernameViewPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UsernameActivityComponent activityComponent =
                ((GitHubClientApplication) getApplicationContext()).getAppComponent()
                        .plus(new UsernameActivityModule());
        activityComponent.inject(this);
        mPresenter.onCreate(this, savedInstanceState);
        UsernameViewBinding binding = DataBindingUtil.setContentView(this, R.layout.username_view);
        binding.setPresenter(mPresenter);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSave(outState);
    }

    @Inject
    public void setPresenter(UsernameViewPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public UsernameViewPresenter.ParentPresenter getParentPresenter() {
        return this;
    }

    @Override
    public void onShowRepositories(Object sender, String username) {
        RepositoryListActivity.startWithUsername(username, this);
    }

    @Override
    public void onError(Object sender, Throwable throwable, Runnable retryHandler) {

    }

    @Override
    public void onLoading(Object sender, boolean complete, Runnable cancelHandler) {

    }
}
