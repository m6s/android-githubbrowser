package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.mschmitt.githubapp.AnalyticsManager;
import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.adapters.RepositoryPagerAdapter;
import info.mschmitt.githubapp.android.presentation.OnBackPressedListener;
import info.mschmitt.githubapp.entities.Repository;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoryPagerViewPresenter extends BaseObservable
        implements OnBackPressedListener, RepositoryDetailsViewPresenter.ParentPresenter {
    public static final String ARG_CURRENT_ITEM = "ARG_CURRENT_ITEM";
    private final CompositeSubscription mSubscriptions = new CompositeSubscription();
    private final AnalyticsManager mAnalyticsManager;
    private Observable<List<Repository>> mRepositories;
    private int mCurrentItem;
    private Map<Repository, Integer> mRepositoryIndexes = new HashMap<>();
    private boolean mIgnoreOnPageSelected;
    private RepositoryPagerView mView;
    private final ViewPager.OnPageChangeListener mOnPageChangeListener =
            new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    if (mIgnoreOnPageSelected) {
                        mIgnoreOnPageSelected = false;
                        return;
                    }
                    Repository repository = mView.getAdapter().getRepositories().get(position);
                    mView.getParentPresenter()
                            .onRepositorySelected(RepositoryPagerViewPresenter.this, repository);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            };

    public RepositoryPagerViewPresenter(RepositoryPagerView view,
                                        Observable<List<Repository>> repositories,
                                        AnalyticsManager analyticsManager) {
        mView = view;
        mRepositories = repositories;
        mAnalyticsManager = analyticsManager;
    }

    public void onCreate(Bundle savedState) {
        int lastCurrentItem = savedState != null ? savedState.getInt(ARG_CURRENT_ITEM) : -1;
        mSubscriptions.add(mRepositories.subscribe((repositories) -> {
            mRepositoryIndexes.clear();
            int i = 0;
            for (Repository repository : repositories) {
                mRepositoryIndexes.put(repository, i++);
            }
            mView.getAdapter().getRepositories().clear();
            mView.getAdapter().getRepositories().addAll(repositories);
            mView.getAdapter().notifyDataSetChanged();
            if (lastCurrentItem != -1) {
                setCurrentItem(lastCurrentItem);
            }
        }));
        mAnalyticsManager.logScreenView(getClass().getName());
    }

    public void onSave(Bundle outState) {
        outState.putInt(ARG_CURRENT_ITEM, mCurrentItem);
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return mOnPageChangeListener;
    }

    public void onDestroy() {
        mSubscriptions.unsubscribe();
    }

    public void selectRepository(Repository repository) {
        setCurrentItem(mRepositoryIndexes.get(repository));
    }

    @Bindable
    public int getCurrentItem() {
        return mCurrentItem;
    }

    private void setCurrentItem(int currentItem) {
        mIgnoreOnPageSelected = true;
        mCurrentItem = currentItem;
        notifyPropertyChanged(BR.currentItem);
    }

    public RepositoryPagerAdapter getAdapter() {
        return mView.getAdapter();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public interface RepositoryPagerView {
        RepositoryPagerAdapter getAdapter();

        ParentPresenter getParentPresenter();
    }

    public interface ParentPresenter {
        void onRepositorySelected(Object sender, Repository repository);
    }
}
