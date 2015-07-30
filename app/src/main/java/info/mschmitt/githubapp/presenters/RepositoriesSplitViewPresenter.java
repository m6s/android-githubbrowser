package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.os.Bundle;

import info.mschmitt.githubapp.AnalyticsManager;
import info.mschmitt.githubapp.android.presentation.OnBackPressedListener;
import info.mschmitt.githubapp.android.presentation.OnErrorListener;
import info.mschmitt.githubapp.android.presentation.OnLoadingListener;
import info.mschmitt.githubapp.entities.Repository;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoriesSplitViewPresenter extends BaseObservable
        implements OnBackPressedListener, RepositoryListViewPresenter.ParentPresenter,
        RepositoryPagerViewPresenter.ParentPresenter {
    public static final String STATE_DETAILS_VIEW_ACTIVE = "STATE_DETAILS_VIEW_ACTIVE";
    private final CompositeSubscription mSubscriptions = new CompositeSubscription();
    private final AnalyticsManager mAnalyticsManager;
    private final RepositoriesSplitView mView;
    private boolean mDetailsViewActive;

    public RepositoriesSplitViewPresenter(RepositoriesSplitView view,
                                          AnalyticsManager analyticsManager) {
        mView = view;
        mAnalyticsManager = analyticsManager;
    }

    public boolean isDetailsViewActive() {
        return mDetailsViewActive;
    }

    public void onCreate(Bundle savedState) {
        mAnalyticsManager.logScreenView(getClass().getName());
        if (savedState != null) {
            mDetailsViewActive = savedState.getBoolean(STATE_DETAILS_VIEW_ACTIVE); //TODO presenter
        }
    }

    public void onSave(Bundle outState) {
        outState.putBoolean(STATE_DETAILS_VIEW_ACTIVE, mDetailsViewActive);
    }

    public void onDestroy() {
        mSubscriptions.unsubscribe();
    }

    @Override
    public void onRepositorySelected(Object sender, Repository repository) {
        if (sender == mView.getMasterPresenter()) {
            if (!mDetailsViewActive) {
                mView.showDetailsView();
                mDetailsViewActive = true;
            }
            mView.getDetailsPresenter().selectRepository(repository);
        } else if (sender == mView.getDetailsPresenter()) {
            mView.getMasterPresenter().selectRepository(repository);
        } else {
            throw new AssertionError();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mDetailsViewActive && mView.getDetailsPresenter().onBackPressed()) {
            return true;
        }
        if (mDetailsViewActive && !mView.isInSplitMode()) {
            mView.hideDetailsView();
            mDetailsViewActive = false;
            return true;
        }
        return mView.getMasterPresenter().onBackPressed();
    }

    public interface RepositoriesSplitView {
        RepositoryListViewPresenter getMasterPresenter();

        RepositoryPagerViewPresenter getDetailsPresenter();

        void showDetailsView();

        ParentPresenter getParentPresenter();

        boolean isInSplitMode();

        void hideDetailsView();
    }

    public interface ParentPresenter extends OnLoadingListener, OnErrorListener {
    }
}
