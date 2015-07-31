package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.HashMap;
import java.util.LinkedHashMap;
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
    private static final String ARG_CURRENT_REPOSITORY_ID = "ARG_CURRENT_REPOSITORY_ID";
    private final CompositeSubscription mSubscriptions = new CompositeSubscription();
    private final AnalyticsManager mAnalyticsManager;
    private final Observable<LinkedHashMap<Long, Repository>> mRepositories;
    private final Map<Long, Integer> mPageIndexes = new HashMap<>();
    private final RepositoryPagerView mView;
    private boolean mIgnoreOnPageSelected;
    private long mCurrentRepositoryId;
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
                    Repository repository = mView.getAdapter().getRepository(position);
                    setCurrentRepositoryId(repository.getId());
                    mView.getParentPresenter()
                            .onRepositorySelected(RepositoryPagerViewPresenter.this, repository);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            };

    public RepositoryPagerViewPresenter(RepositoryPagerView view,
                                        Observable<LinkedHashMap<Long, Repository>> repositories,
                                        AnalyticsManager analyticsManager) {
        mView = view;
        mRepositories = repositories;
        mAnalyticsManager = analyticsManager;
    }

    public void onCreate(Bundle savedState) {
        long lastRepositoryId =
                savedState != null ? savedState.getLong(ARG_CURRENT_REPOSITORY_ID) : -1;
        mSubscriptions.add(mRepositories.subscribe((repositories) -> {
            mPageIndexes.clear();
            int i = 0;
            for (long id : repositories.keySet()) {
                mPageIndexes.put(id, i++);
            }
            mView.getAdapter().clear();
            mView.getAdapter().addAll(repositories.values());
            mView.getAdapter().notifyDataSetChanged();
            if (lastRepositoryId != -1) {
                setCurrentRepositoryId(lastRepositoryId);
            }
        }));
        mAnalyticsManager.logScreenView(getClass().getName());
    }

    private void setCurrentRepositoryId(long repositoryId) {
        mIgnoreOnPageSelected = true;
        mCurrentRepositoryId = repositoryId;
        notifyPropertyChanged(BR.currentItem);
    }

    public void onSave(Bundle outState) {
        outState.putLong(ARG_CURRENT_REPOSITORY_ID, mCurrentRepositoryId);
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return mOnPageChangeListener;
    }

    public void onDestroy() {
        mSubscriptions.unsubscribe();
    }

    public void selectRepository(Repository repository) {
        setCurrentRepositoryId(repository.getId());
    }

    @Bindable
    public int getCurrentItem() {
        Integer integer = mPageIndexes.get(mCurrentRepositoryId);
        return integer != null ? integer : -1;
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
