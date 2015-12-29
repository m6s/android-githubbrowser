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

import javax.inject.Inject;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.domain.AnalyticsManager;
import info.mschmitt.githubapp.entities.Repository;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoryPagerViewModel extends BaseObservable {
    private static final String ARG_CURRENT_REPOSITORY_ID = "ARG_CURRENT_REPOSITORY_ID";
    private final AnalyticsManager mAnalyticsManager;
    private final ObservableList<Repository> mRepositories = new ObservableArrayList<>();
    private final Map<Long, Integer> mPageIndexes = new HashMap<>();
    private final NavigationHandler mNavigationHandler;
    private CompositeSubscription mSubscriptions;
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
                    mNavigationHandler.showRepository(repository);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            };

    @Inject
    public RepositoryPagerViewModel(AnalyticsManager analyticsManager,
                                    NavigationHandler navigationHandler) {
        mAnalyticsManager = analyticsManager;
        mNavigationHandler = navigationHandler;
    }

    public void onCreate(Bundle savedState) {
        mSubscriptions = new CompositeSubscription();
        long lastRepositoryId =
                savedState != null ? savedState.getLong(ARG_CURRENT_REPOSITORY_ID) : -1;
        mSubscriptions.add(mNavigationHandler.getRepositoryMap().subscribe((repositoryMap) -> {
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
        mSubscriptions.add(mNavigationHandler.getRepository().subscribe(this::selectRepository));
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

    public ObservableList<Repository> getRepositories() {
        return mRepositories;
    }

    public interface NavigationHandler {
        void showRepository(Repository repository);

        Observable<LinkedHashMap<Long, Repository>> getRepositoryMap();

        Observable<Repository> getRepository();
    }
}
