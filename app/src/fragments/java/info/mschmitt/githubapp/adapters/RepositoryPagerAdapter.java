package info.mschmitt.githubapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.fragments.RepositoryDetailsFragment;

/**
 * @author Matthias Schmitt
 */
public class RepositoryPagerAdapter extends FragmentStatePagerAdapter {
    private List<Repository> mRepositories = new ArrayList<>();

    public RepositoryPagerAdapter(FragmentManager fm, List<Repository> repositories) {
        super(fm);
        mRepositories = repositories;
    }

    @Override
    public Fragment getItem(int position) {
        return RepositoryDetailsFragment.newInstanceForRepositoryPosition(position);
    }

    @Override
    public int getCount() {
        return mRepositories.size();
    }

    public Repository getRepository(int position) {
        return mRepositories.get(position);
    }

    public void clear() {
        mRepositories.clear();
    }

    public void addAll(Collection<Repository> repositories) {
        mRepositories.addAll(repositories);
    }
}
