package info.mschmitt.githubapp.viewmodels;

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
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.entities.Repository;
import rx.Observable;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoryPagerViewModel extends BaseObservable {
    private static final String ARG_CURRENT_REPOSITORY_ID = "ARG_CURRENT_REPOSITORY_ID";
    private final Observable<LinkedHashMap<Long, Repository>> mRepositoryMap;
    private final Subject<Repository, Repository> mSelectedRepository;
    private final AnalyticsService mAnalyticsService;
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
                    mSelectedRepository.onNext(repository);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            };

    public RepositoryPagerViewModel(Observable<LinkedHashMap<Long, Repository>> repositoryMap,
                                    Subject<Repository, Repository> selectedRepository,
                                    AnalyticsService analyticsService,
                                    NavigationHandler navigationHandler) {
        mRepositoryMap = repositoryMap;
        mSelectedRepository = selectedRepository;
        mAnalyticsService = analyticsService;
        mNavigationHandler = navigationHandler;
    }

    public void onCreate(Bundle savedState) {
        mSubscriptions = new CompositeSubscription();
        long lastRepositoryId =
                savedState != null ? savedState.getLong(ARG_CURRENT_REPOSITORY_ID) : -1;
        mSubscriptions.add(mRepositoryMap.subscribe((repositoryMap) -> {
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
        mSubscriptions.add(mSelectedRepository.subscribe(this::selectRepository));
        mAnalyticsService.logScreenView(getClass().getName());
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
    }
}
