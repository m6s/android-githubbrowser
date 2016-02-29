package info.mschmitt.githubapp.viewmodels;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.PropertyChangeRegistry;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.android.presentation.DataBindingObservable;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.ghdomain.AnalyticsService;
import info.mschmitt.githubapp.scopes.RepositoryPagerViewScope;
import info.mschmitt.githubapp.viewmodels.qualifiers.RepositoryMapObservable;
import info.mschmitt.githubapp.viewmodels.qualifiers.SelectedRepositorySubject;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@RepositoryPagerViewScope
public class RepositoryPagerViewModel implements DataBindingObservable {
    private static final String STATE_CURRENT_REPOSITORY_ID = "STATE_CURRENT_REPOSITORY_ID";
    private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();
    private final Observable<LinkedHashMap<Long, Repository>> mRepositoryMapObservable;
    private final BehaviorSubject<Long> mSelectedRepositoryIdSubject;
    private final AnalyticsService mAnalyticsService;
    private final ObservableList<Repository> mRepositories = new ObservableArrayList<>();
    private final Map<Long, Integer> mPageIndexes = new HashMap<>();
    private final NavigationHandler mNavigationHandler;
    private CompositeSubscription mSubscriptions;
    private long mCurrentRepositoryId;
    private final ViewPager.OnPageChangeListener mOnPageChangeListener =
            new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    if (mRepositories.isEmpty()) {
                        return;
                    }
                    Repository repository = mRepositories.get(position);
                    if (mCurrentRepositoryId != repository.id()) {
                        mSelectedRepositoryIdSubject.onNext(repository.id());
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            };

    @Inject
    public RepositoryPagerViewModel(@RepositoryMapObservable
                                    Observable<LinkedHashMap<Long, Repository>>
                                                repositoryMapObservable,
                                    @SelectedRepositorySubject
                                    BehaviorSubject<Long> selectedRepositoryIdSubject,
                                    AnalyticsService analyticsService,
                                    NavigationHandler navigationHandler) {
        mRepositoryMapObservable = repositoryMapObservable;
        mSelectedRepositoryIdSubject = selectedRepositoryIdSubject;
        mAnalyticsService = analyticsService;
        mNavigationHandler = navigationHandler;
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mPropertyChangeRegistry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mPropertyChangeRegistry.remove(callback);
    }

    public void onLoad(Bundle savedState) {
        mCurrentRepositoryId =
                savedState != null ? savedState.getLong(STATE_CURRENT_REPOSITORY_ID) : -1;
    }

    public void onResume() {
        mSubscriptions = new CompositeSubscription();
        mSubscriptions.add(mRepositoryMapObservable.subscribe((repositoryMap) -> {
            mPageIndexes.clear();
            int i = 0;
            for (long id : repositoryMap.keySet()) {
                mPageIndexes.put(id, i++);
            }
            mRepositories.clear();
            mRepositories.addAll(repositoryMap.values());
            if (mCurrentRepositoryId != -1) {
                setCurrentRepositoryId(mCurrentRepositoryId);
            }
        }));
        mSubscriptions.add(mSelectedRepositoryIdSubject.subscribe(this::onNextRepositorySelected));
        mAnalyticsService.logScreenView(getClass().getName());
    }

    private void setCurrentRepositoryId(long repositoryId) {
        mCurrentRepositoryId = repositoryId;
        mPropertyChangeRegistry.notifyChange(this, BR.currentItem);
    }

    public void onSave(Bundle outState) {
        outState.putLong(STATE_CURRENT_REPOSITORY_ID, mCurrentRepositoryId);
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return mOnPageChangeListener;
    }

    public void onPause() {
        mSubscriptions.unsubscribe();
    }

    public void onNextRepositorySelected(long id) {
        if (id != -1) {
            setCurrentRepositoryId(id);
        }
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
