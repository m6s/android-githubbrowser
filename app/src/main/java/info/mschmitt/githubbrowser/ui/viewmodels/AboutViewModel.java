package info.mschmitt.githubbrowser.ui.viewmodels;

import android.databinding.PropertyChangeRegistry;
import android.os.Bundle;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.android.databinding.DataBindingObservable;
import info.mschmitt.githubbrowser.ui.scopes.AboutViewScope;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@AboutViewScope
public class AboutViewModel implements DataBindingObservable {
    private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();
    private final AnalyticsService mAnalyticsService;
    private final NavigationService mNavigationService;
    private CompositeSubscription mSubscriptions;

    @Inject
    public AboutViewModel(AnalyticsService analyticsService, NavigationService NavigationService) {
        mAnalyticsService = analyticsService;
        mNavigationService = NavigationService;
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
        InstanceStateUtils.load(this, savedState);
    }

    public void onResume() {
        mSubscriptions = new CompositeSubscription();
        connectModel();
    }

    private void connectModel() {
    }

    public void onSave(Bundle outState) {
        InstanceStateUtils.save(this, outState);
    }

    public void onPause() {
        mSubscriptions.unsubscribe();
    }

    public interface NavigationService {
        void showError(Throwable throwable, Runnable retryHandler);
    }

    public interface AnalyticsService {
    }

    private static class InstanceStateUtils {
        private static final String TEST = "TEST";

        private static void save(AboutViewModel viewModel, Bundle outState) {
//            outState.putString(TEST, viewModel.mTest);
        }

        private static void load(AboutViewModel viewModel, Bundle savedState) {
            if (savedState == null) {
                return;
            }
//            viewModel.mTest = savedState.getString(TEST);
        }
    }
}
