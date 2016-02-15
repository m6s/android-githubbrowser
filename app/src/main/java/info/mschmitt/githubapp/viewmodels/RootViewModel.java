package info.mschmitt.githubapp.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;

import javax.inject.Inject;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.di.RootViewScope;
import info.mschmitt.githubapp.java.LoadingProgressManager;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@RootViewScope
public class RootViewModel extends BaseObservable {
    private final LoadingProgressManager mLoadingProgressManager;
    private final NavigationHandler mNavigationHandler;
    private CompositeSubscription mSubscriptions;
    private boolean mLoading;

    @Inject
    public RootViewModel(LoadingProgressManager loadingProgressManager,
                         NavigationHandler navigationHandler) {
        mLoadingProgressManager = loadingProgressManager;
        mNavigationHandler = navigationHandler;
    }

    public void onLoad(Bundle savedState) {
    }

    public void onResume() {
        mSubscriptions = new CompositeSubscription();
        mSubscriptions.add(mLoadingProgressManager.isLoading().subscribe(this::setLoading));
    }

    public void onSave(Bundle outState) {
    }

    public void onPause() {
        mLoadingProgressManager.cancelAllTasks(false);
        mSubscriptions.unsubscribe();
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

    public boolean onBackPressed() {
        return mLoadingProgressManager.cancelAllTasks(true) || mNavigationHandler.goBack();
    }

    public interface NavigationHandler {
        boolean goBack();
    }
}
