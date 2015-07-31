package info.mschmitt.githubapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.fragments.RepositoryDetailsFragment;

/**
 * @author Matthias Schmitt
 */
public class RepositoryPagerAdapter extends FragmentStatePagerAdapter {
    private List<Repository> mRepositories;

    public RepositoryPagerAdapter(FragmentManager fm, List<Repository> repositories) {
        super(fm);
        mRepositories = repositories;
    }

    public List<Repository> getRepositories() {
        return mRepositories;
    }

    @Override
    public Fragment getItem(int position) {
        return RepositoryDetailsFragment.newInstance(mRepositories.get(position).getId());
    }

    @Override
    public int getCount() {
        return mRepositories.size();
    }
}
