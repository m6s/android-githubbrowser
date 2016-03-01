package info.mschmitt.githubbrowser.ui.viewmodels;

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

import info.mschmitt.githubbrowser.BR;
import info.mschmitt.githubbrowser.android.presentation.DataBindingObservable;
import info.mschmitt.githubbrowser.entities.Repository;
import info.mschmitt.githubbrowser.ui.scopes.RepositoryPagerViewScope;
import info.mschmitt.githubbrowser.ui.viewmodels.qualifiers.RepositoryMapObservable;
import info.mschmitt.githubbrowser.ui.viewmodels.qualifiers.SelectedRepositorySubject;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@RepositoryPagerViewScope
public class RepositoryPagerViewModel implements DataBindingObservable {
    private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();
    private final Observable<LinkedHashMap<Long, Repository>> mRepositoryMapObservable;
    private final BehaviorSubject<Long> mSelectedRepositorySubject;
    private final ObservableList<Repository> mRepositories = new ObservableArrayList<>();
    private final Map<Long, Integer> mPageIndexes = new HashMap<>();
    private final NavigationHandler mNavigationHandler;
    private final ViewPager.OnPageChangeListener mOnPageChangeListener =
            new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    info.mschmitt.githubbrowser.entities.Repository repository =
                            mRepositories.get(position);
                    if (mSelectedRepositorySubject.getValue() != repository.id()) {
                        mSelectedRepositorySubject.onNext(repository.id());
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            };
    private CompositeSubscription mSubscriptions;

    @Inject
    public RepositoryPagerViewModel(@RepositoryMapObservable
                                        Observable<LinkedHashMap<Long, Repository>>
                                                repositoryMapObservable,
                                    @SelectedRepositorySubject
                                    BehaviorSubject<Long> selectedRepositorySubject,
                                    NavigationHandler navigationHandler) {
        mRepositoryMapObservable = repositoryMapObservable;
        mSelectedRepositorySubject = selectedRepositorySubject;
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
    }

    public void onResume() {
        mSubscriptions = new CompositeSubscription();
        mSubscriptions.add(mRepositoryMapObservable.subscribe(this::onNextRepositoryMap));
        mSubscriptions.add(mSelectedRepositorySubject.subscribe(this::onNextSelectedRepository));
    }

    private void onNextRepositoryMap(
            LinkedHashMap<Long, info.mschmitt.githubbrowser.entities.Repository> map) {
        mPageIndexes.clear();
        int i = 0;
        for (long id : map.keySet()) {
            mPageIndexes.put(id, i++);
        }
        mRepositories.clear();
        mRepositories.addAll(map.values());
        mPropertyChangeRegistry.notifyChange(this, BR.currentItem);
    }

    public void onSave(Bundle outState) {
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return mOnPageChangeListener;
    }

    public void onPause() {
        mSubscriptions.unsubscribe();
    }

    public void onNextSelectedRepository(long id) {
        if (id != -1) {
            mPropertyChangeRegistry.notifyChange(this, BR.currentItem);
        }
    }

    @Bindable
    public int getCurrentItem() {
        Integer index = mPageIndexes.get(mSelectedRepositorySubject.getValue());
        return index != null ? index : -1;
    }

    public ObservableList<Repository> getRepositories() {
        return mRepositories;
    }

    public interface NavigationHandler {
    }
}
