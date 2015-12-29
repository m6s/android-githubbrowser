package info.mschmitt.githubapp.adapters;

import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import info.mschmitt.githubapp.android.presentation.PagerAdapterOnListChangedCallback;
import info.mschmitt.githubapp.app.RepositoryDetailsViewFragment;
import info.mschmitt.githubapp.entities.Repository;

/**
 * @author Matthias Schmitt
 */
public class RepositoryPagerAdapter extends FragmentStatePagerAdapter {
    private final ObservableList<Repository> mRepositories;
    private final PagerAdapterOnListChangedCallback<Repository> mCallback =
            new PagerAdapterOnListChangedCallback<>(this);

    public RepositoryPagerAdapter(FragmentManager fm, ObservableList<Repository> repositories) {
        super(fm);
        mRepositories = repositories;
    }

    @Override
    public Fragment getItem(int position) {
        return RepositoryDetailsViewFragment.newInstanceForRepositoryPosition(position);
    }

    @Override
    public int getCount() {
        return mRepositories.size();
    }

    public void onCreateView(Bundle savedInstanceState) {
        mRepositories.addOnListChangedCallback(mCallback);
    }

    public void onDestroyView() {
        mRepositories.removeOnListChangedCallback(mCallback);
    }
}
