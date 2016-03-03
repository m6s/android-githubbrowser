package info.mschmitt.githubbrowser.ui.viewmodels;

import android.databinding.Bindable;
import android.databinding.PropertyChangeRegistry;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.BR;
import info.mschmitt.githubbrowser.android.databinding.DataBindingObservable;
import info.mschmitt.githubbrowser.domain.AnalyticsService;
import info.mschmitt.githubbrowser.domain.UserDownloader;
import info.mschmitt.githubbrowser.domain.Validator;
import info.mschmitt.githubbrowser.java.LoadingProgressManager;
import info.mschmitt.githubbrowser.java.ObjectsBackport;
import info.mschmitt.githubbrowser.ui.scopes.UsernameViewScope;
import rx.Completable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@UsernameViewScope
public class UsernameViewModel implements DataBindingObservable {
    private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();
    private final Validator mValidator;
    private final UserDownloader mUserDownloader;
    private final AnalyticsService mAnalyticsService;
    private final LoadingProgressManager mLoadingProgressManager;
    private final NavigationHandler mNavigationHandler;
    private CompositeSubscription mSubscriptions;
    private String mUsername;
    private String mUsernameError;
    private final TextWatcher mUsernameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            mUsername = s.toString();
            setUsernameError(null);
        }
    };

    @Inject
    public UsernameViewModel(Validator validator, UserDownloader userDownloader,
                             AnalyticsService analyticsService,
                             LoadingProgressManager loadingProgressManager,
                             NavigationHandler navigationHandler) {
        mValidator = validator;
        mUserDownloader = userDownloader;
        mAnalyticsService = analyticsService;
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

    public String getUsername() {
        return mUsername;
    }

    @SuppressWarnings("unused")
    public void onShowRepositoriesClick(View view) {
        showRepositories();
    }

    private void showRepositories() {
        if (!mValidator.validateUsername(mUsername)) {
            setUsernameError("Validation error");
            return;
        }
        Completable download =
                mUserDownloader.download(mUsername).observeOn(AndroidSchedulers.mainThread())
                        .toObservable().toCompletable()
                        .doOnUnsubscribe(() -> mLoadingProgressManager.notifyLoadingEnd(this));
        CompositeSubscription subscription = new CompositeSubscription();
        mLoadingProgressManager.notifyLoadingBegin(this, subscription::unsubscribe);
        subscription.add(download.subscribe(throwable -> {
            mAnalyticsService.logError(throwable);
            mNavigationHandler.showError(throwable, this::connectModel);
        }, () -> mNavigationHandler.showRepositorySplitView(mUsername)));
        mSubscriptions.add(subscription);
    }

    public TextWatcher getUsernameTextWatcher() {
        return mUsernameTextWatcher;
    }

    @Bindable
    public String getUsernameError() {
        return mUsernameError;
    }

    private void setUsernameError(String usernameError) {
        if (ObjectsBackport.equals(usernameError, mUsernameError)) {
            return;
        }
        mUsernameError = usernameError;
        mPropertyChangeRegistry.notifyChange(this, BR.usernameError);
    }

    @Bindable
    public boolean getLoading() {
        return mLoadingProgressManager.isLoading();
    }

    private void onNextLoadingState(boolean loading) {
        mPropertyChangeRegistry.notifyChange(this, BR.loading);
    }

    public void onLoad(Bundle savedState) {
        State.restoreInstanceState(this, savedState);
    }

    public void onResume() {
        mSubscriptions = new CompositeSubscription();
        connectModel();
        mAnalyticsService.logScreenView(getClass().getName());
    }

    private void connectModel() {
        mSubscriptions.add(mLoadingProgressManager.getLoadingStateObservable()
                .subscribe(this::onNextLoadingState));
    }

    public void onSave(Bundle outState) {
        State.saveInstanceState(this, outState);
    }

    public void onPause() {
        mSubscriptions.unsubscribe();
    }

    public boolean onAboutOptionsItemSelected() {
        mNavigationHandler.showAboutView();
        return true;
    }

    public interface NavigationHandler {
        void showRepositorySplitView(String username);

        void showError(Throwable throwable, Runnable retryHandler);

        void showAboutView();
    }

    private static class State {
        private static final String USERNAME = "USERNAME";

        private static void saveInstanceState(UsernameViewModel viewModel, Bundle outState) {
            outState.putString(USERNAME, viewModel.mUsername);
        }

        private static void restoreInstanceState(UsernameViewModel viewModel, Bundle savedState) {
            if (savedState == null) {
                return;
            }
            viewModel.mUsername = savedState.getString(USERNAME);
        }
    }
}
