package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.android.presentation.OnBackPressedListener;
import info.mschmitt.githubapp.domain.AnalyticsManager;
import info.mschmitt.githubapp.entities.Repository;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoryPagerPresenter extends BaseObservable
        implements OnBackPressedListener, RepositoryDetailsPresenter.ParentPresenter {
    private static final String ARG_CURRENT_REPOSITORY_ID = "ARG_CURRENT_REPOSITORY_ID";
    private final AnalyticsManager mAnalyticsManager;
    private final Observable<LinkedHashMap<Long, Repository>> mRepositoryMapObservable;
    private final ObservableList<Repository> mRepositories = new ObservableArrayList<>();
    private final Map<Long, Integer> mPageIndexes = new HashMap<>();
    private CompositeSubscription mSubscriptions;
    private RepositoryPagerView mView;
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
                    Repository repository = mRepositories.get(position);
                    setCurrentRepositoryId(repository.getId());
                    mView.getParentPresenter()
                            .onRepositorySelected(RepositoryPagerPresenter.this, repository);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            };

    public RepositoryPagerPresenter(
            Observable<LinkedHashMap<Long, Repository>> repositoryMapObservable,
            AnalyticsManager analyticsManager) {
        mRepositoryMapObservable = repositoryMapObservable;
        mAnalyticsManager = analyticsManager;
    }

    public void onCreate(RepositoryPagerView view, Bundle savedState) {
        mSubscriptions = new CompositeSubscription();
        mView = view;
        long lastRepositoryId =
                savedState != null ? savedState.getLong(ARG_CURRENT_REPOSITORY_ID) : -1;
        mSubscriptions.add(mRepositoryMapObservable.subscribe((repositoryMap) -> {
            mPageIndexes.clear();
            int i = 0;
            for (long id : repositoryMap.keySet()) {
                mPageIndexes.put(id, i++);
            }
            mRepositories.clear();
            mRepositories.addAll(repositoryMap.values());
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
        mView = null;
    }

    public void selectRepository(Repository repository) {
        setCurrentRepositoryId(repository.getId());
    }

    @Bindable
    public int getCurrentItem() {
        Integer integer = mPageIndexes.get(mCurrentRepositoryId);
        return integer != null ? integer : -1;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public ObservableList<Repository> getRepositories() {
        return mRepositories;
    }

    public interface RepositoryPagerView {
        ParentPresenter getParentPresenter();
    }

    public interface ParentPresenter {
        void onRepositorySelected(Object sender, Repository repository);
    }
}
