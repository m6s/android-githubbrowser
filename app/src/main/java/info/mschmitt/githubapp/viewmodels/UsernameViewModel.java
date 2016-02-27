package info.mschmitt.githubapp.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import javax.inject.Inject;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.di.UsernameViewScope;
import info.mschmitt.githubapp.domain.AnalyticsService;
import info.mschmitt.githubapp.domain.UserDownloader;
import info.mschmitt.githubapp.domain.Validator;
import info.mschmitt.githubapp.entities.User;
import info.mschmitt.githubapp.java.LoadingProgressManager;
import info.mschmitt.githubapp.java.ObjectsBackport;
import info.mschmitt.githubapp.java.RxSingleUtils;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@UsernameViewScope
public class UsernameViewModel extends BaseObservable {
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
        Single<User> download =
                mUserDownloader.download(mUsername).observeOn(AndroidSchedulers.mainThread())
                        .doOnUnsubscribe(() -> {
                            setLoading(false);
                            mLoadingProgressManager.notifyLoadingEnd(this);
                        });
        RxSingleUtils
                .subscribe(download, user -> mNavigationHandler.showRepositorySplitView(mUsername),
                        throwable -> {
                            mAnalyticsService.logError(throwable);
                            mNavigationHandler.showError(throwable, this::showRepositories);
                        }, subscription -> {
                            mSubscriptions.add(subscription);
                            setLoading(true);
                            mLoadingProgressManager
                                    .notifyLoadingBegin(this, subscription::unsubscribe);
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

    public void onLoad(Bundle savedState) {
        State.restoreInstanceState(this, savedState);
    }

    public void onResume() {
        mSubscriptions = new CompositeSubscription();
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
