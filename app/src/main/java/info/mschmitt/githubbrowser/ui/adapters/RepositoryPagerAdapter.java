package info.mschmitt.githubbrowser.ui.adapters;

import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import info.mschmitt.githubbrowser.android.presentation.PagerAdapterOnListChangedCallback;
import info.mschmitt.githubbrowser.entities.Repository;
import info.mschmitt.githubbrowser.ui.fragments.RepositoryDetailsViewFragment;

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
