package info.mschmitt.githubapp.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import javax.inject.Inject;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.android.presentation.ObjectsBackport;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.domain.UserDownloader;
import info.mschmitt.githubapp.domain.Validator;
import info.mschmitt.githubapp.entities.User;
import info.mschmitt.githubapp.utils.LoadingProgressManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class UsernameViewModel extends BaseObservable {
    public static final String STATE_USER_NAME = "STATE_USER_NAME";
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
    private boolean mLoading;

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
        ConnectableObservable<User> user =
                mUserDownloader.download(mUsername).observeOn(AndroidSchedulers.mainThread())
                        .doOnUnsubscribe(() -> {
                            setLoading(false);
                            mLoadingProgressManager.notifyLoadingEnd(this);
                        }).publish();
        user.subscribe(nextUser -> mNavigationHandler.showRepositorySplitView(mUsername),
                throwable -> {
                    mAnalyticsService.logError(throwable);
                    mNavigationHandler.showError(throwable, this::showRepositories);
                });
        user.connect(subscription -> {
            mSubscriptions.add(subscription);
            setLoading(true);
            mLoadingProgressManager.notifyLoadingBegin(this, subscription::unsubscribe);
        });
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
        notifyPropertyChanged(BR.usernameError);
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

    public void onCreate(Bundle savedState) {
        mSubscriptions = new CompositeSubscription();
        if (savedState != null) {
            mUsername = savedState.getString(STATE_USER_NAME);
        }
    }

    public void onSave(Bundle outState) {
        outState.putString(STATE_USER_NAME, mUsername);
    }

    public void onDestroy() {
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
}
