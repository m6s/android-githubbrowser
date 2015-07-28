package info.mschmitt.githubapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import info.mschmitt.githubapp.components.RepositoryDetailsActivityComponent;
import info.mschmitt.githubapp.databinding.RepositoryDetailsViewBinding;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.modules.RepositoryDetailsActivityModule;
import info.mschmitt.githubapp.presenters.RepositoryDetailsViewPresenter;


/**
 * @author Matthias Schmitt
 */
public class RepositoryDetailsActivity extends AppCompatActivity
        implements RepositoryDetailsViewPresenter.RepositoryDetailsView,
        RepositoryDetailsViewPresenter.ParentPresenter {
    private static final String ARG_REPOSITORY = "arg_repository";
    private RepositoryDetailsViewPresenter mPresenter;

    public static void startWithRepository(Repository repository, Context packageContext) {
        Intent intent = new Intent(packageContext, RepositoryDetailsActivity.class);
        intent.putExtra(ARG_REPOSITORY, repository);
        packageContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RepositoryDetailsActivityComponent component =
                ((GitHubClientApplication) getApplicationContext()).getAppComponent()
                        .plus(new RepositoryDetailsActivityModule(this));
        component.inject(this);
        Bundle extras = getIntent().getExtras();
        Repository repository = (Repository) extras.getSerializable(ARG_REPOSITORY);
        mPresenter.setRepository(repository);
        mPresenter.onCreate(this, savedInstanceState);
        RepositoryDetailsViewBinding binding =
                DataBindingUtil.setContentView(this, R.layout.repository_details_view);
        binding.setPresenter(mPresenter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSave(outState);
    }

    @Inject
    public void setPresenter(RepositoryDetailsViewPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RepositoryDetailsViewPresenter.ParentPresenter getParentPresenter() {
        return this;
    }
}
