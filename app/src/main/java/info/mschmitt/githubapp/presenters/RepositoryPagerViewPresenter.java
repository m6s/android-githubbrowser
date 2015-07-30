package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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

    @Inject
    public RepositoryPagerViewPresenter(Observable<List<Repository>> repositories,
                                        AnalyticsManager analyticsManager) {
        mRepositories = repositories;
        mAnalyticsManager = analyticsManager;
    }

    public void onCreate(Bundle savedState) {
        mAnalyticsManager.logScreenView(getClass().getName());
        mSubscriptions.add(mRepositories.subscribe((repositories) -> {
            mRepositoryIndexes.clear();
            int i = 0;
            for (Repository repository : repositories) {
                mRepositoryIndexes.put(repository, i++);
            }
            mView.getAdapter().getRepositories().clear();
            mView.getAdapter().getRepositories().addAll(repositories);
            mView.getAdapter().notifyDataSetChanged();
        }));
    }

    public void postInject(RepositoryPagerView view) {
        mView = view;
    }

    public void onSave(Bundle outState) {

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

    public void setCurrentItem(int currentItem) {
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
