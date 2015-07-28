package info.mschmitt.githubapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import info.mschmitt.githubapp.adapters.RepositoryListAdapter;
import info.mschmitt.githubapp.components.RepositoryListActivityComponent;
import info.mschmitt.githubapp.databinding.RepositoryListViewBinding;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.modules.RepositoryListActivityModule;
import info.mschmitt.githubapp.presenters.RepositoryListViewPresenter;


/**
 * @author Matthias Schmitt
 */
public class RepositoryListActivity extends AppCompatActivity
        implements RepositoryListViewPresenter.RepositoryListView,
        RepositoryListViewPresenter.ParentPresenter {
    private static final String ARG_USERNAME = "arg_username";
    private final RepositoryListAdapter mAdapter =
            new RepositoryListAdapter(this, new ArrayList<>());
    private RepositoryListViewPresenter mPresenter;

    public static void startWithUsername(String username, Context packageContext) {
        Intent intent = new Intent(packageContext, RepositoryListActivity.class);
        intent.putExtra(ARG_USERNAME, username);
        packageContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String username = getIntent().getStringExtra(ARG_USERNAME);
        RepositoryListActivityComponent activityComponent =
                ((GitHubClientApplication) getApplicationContext()).getAppComponent()
                        .plus(new RepositoryListActivityModule(username));
        activityComponent.inject(this);
        mPresenter.onCreate(this, savedInstanceState);
        RepositoryListViewBinding binding =
                DataBindingUtil.setContentView(this, R.layout.repository_list_view);
        binding.setPresenter(mPresenter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSave(outState);
    }

    @Inject
    public void setPresenter(RepositoryListViewPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RepositoryListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public RepositoryListViewPresenter.ParentPresenter getParentPresenter() {
        return this;
    }

    @Override
    public void onRepositorySelected(Object sender, Repository repository) {
        RepositoryDetailsActivity.startWithRepository(repository, this);
    }
}
