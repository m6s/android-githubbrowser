package info.mschmitt.githubbrowser.ui.viewmodels;

import android.databinding.Bindable;
import android.databinding.PropertyChangeRegistry;
import android.os.Bundle;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.BR;
import info.mschmitt.githubbrowser.android.presentation.DataBindingObservable;
import info.mschmitt.githubbrowser.java.LoadingProgressManager;
import info.mschmitt.githubbrowser.ui.scopes.RootViewScope;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@RootViewScope
public class RootViewModel implements DataBindingObservable {
    private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();
    private final LoadingProgressManager mLoadingProgressManager;
    private final NavigationHandler mNavigationHandler;
    private CompositeSubscription mSubscriptions;

    @Inject
    public RootViewModel(LoadingProgressManager loadingProgressManager,
                         NavigationHandler navigationHandler) {
        mLoadingProgressManager = loadingProgressManager;
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
        mSubscriptions.add(mLoadingProgressManager.getLoadingStateObservable()
                .subscribe(this::onNextLoadingState));
    }

    public void onSave(Bundle outState) {
    }

    public void onPause() {
        mLoadingProgressManager.cancelAllTasks(false);
        mSubscriptions.unsubscribe();
    }

    @Bindable
    public boolean getLoading() {
        return mLoadingProgressManager.isLoading();
    }

    private void onNextLoadingState(boolean loading) {
        mPropertyChangeRegistry.notifyChange(this, BR.loading);
    }

    public boolean onBackPressed() {
        return mLoadingProgressManager.cancelAllTasks(true) || mNavigationHandler.goBack();
    }

    public interface NavigationHandler {
        boolean goBack();
    }
}
