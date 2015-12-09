package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.MainThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.android.presentation.OnBackPressedListener;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class GitHubBrowserPresenter extends BaseObservable
        implements OnBackPressedListener, UsernamePresenter.ParentPresenter,
        RepositorySplitPresenter.ParentPresenter {
    private GitHubBrowserView mView;
    private CompositeSubscription mSubscriptions;
    private boolean mLoading;
    private List<Object> mLoadingQueue = new ArrayList<>();
    private Map<Object, Runnable> mCancelHandlers = new HashMap<>();

    @MainThread
    public void onCreate(GitHubBrowserView view, Bundle savedState) {
        mSubscriptions = new CompositeSubscription();
        mView = view;
    }

    @MainThread
    public void onSave(Bundle outState) {
    }

    @MainThread
    public void onDestroy() {
        mSubscriptions.unsubscribe();
        mView = null;
    }

    @MainThread
    @Override
    public void onLoading(Object sender, boolean complete, Runnable cancelHandler) {
        if (complete) {
            mLoadingQueue.remove(sender);
            mCancelHandlers.remove(sender);
            setLoading(!mLoadingQueue.isEmpty());
        } else {
            mLoadingQueue.add(sender);
            mCancelHandlers.put(sender, cancelHandler);
            setLoading(true);
        }
    }

    @MainThread
    @Override
    public boolean onBackPressed() {
        if (isLoading()) {
            ArrayList<Runnable> cancelHandlers = new ArrayList<>(mCancelHandlers.values());
            mCancelHandlers.clear();
            mLoadingQueue.clear();
            setLoading(false);
            for (Runnable cancelHandler : cancelHandlers) {
                cancelHandler.run();
            }
            return true;
        }
        boolean handled;
        Object childPresenter = mView.getChildPresenter();
        if (childPresenter instanceof OnBackPressedListener) {
            handled = ((OnBackPressedListener) childPresenter).onBackPressed();
            if (handled) {
                return true;
            }
        }
        handled = mView.tryShowPreviousChildView();
        return handled;
    }

    @Bindable
    public boolean isLoading() {
        return mLoading;
    }

    private void setLoading(boolean loading) {
        if (loading == mLoading) {
            return;
        }
        mLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    @Override
    public void onError(Object sender, Throwable throwable, Runnable retryHandler) {
        mView.showErrorDialog(throwable, retryHandler);
    }

    public interface GitHubBrowserView {
        ParentPresenter getParentPresenter();

        Object getChildPresenter();

        boolean tryShowPreviousChildView();

        void showErrorDialog(Throwable throwable, Runnable retryHandler);
    }

    public interface ParentPresenter {
    }
}
