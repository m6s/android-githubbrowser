package info.mschmitt.githubapp.adapters;

import android.databinding.ObservableList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import info.mschmitt.githubapp.android.presentation.PagerAdapterOnListChangedCallback;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.ui.RepositoryDetailsFragment;

/**
 * @author Matthias Schmitt
 */
public class RepositoryPagerAdapter extends FragmentStatePagerAdapter {
    private final ObservableList<Repository> mRepositories;

    public RepositoryPagerAdapter(FragmentManager fm, ObservableList<Repository> repositories) {
        super(fm);
        mRepositories = repositories;
        mRepositories.addOnListChangedCallback(new PagerAdapterOnListChangedCallback<>(this));
    }

    @Override
    public Fragment getItem(int position) {
        return RepositoryDetailsFragment.newInstanceForRepositoryPosition(position);
    }

    @Override
    public int getCount() {
        return mRepositories.size();
    }
}
