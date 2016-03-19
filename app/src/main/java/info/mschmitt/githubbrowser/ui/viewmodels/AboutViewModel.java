package info.mschmitt.githubbrowser.ui.viewmodels;

import android.content.res.Resources;
import android.databinding.Bindable;
import android.databinding.PropertyChangeRegistry;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.BR;
import info.mschmitt.githubbrowser.BuildConfig;
import info.mschmitt.githubbrowser.R;
import info.mschmitt.githubbrowser.android.databinding.DataBindingObservable;
import info.mschmitt.githubbrowser.app.qualifiers.ApplicationResources;
import info.mschmitt.githubbrowser.java.StringBackport;
import info.mschmitt.githubbrowser.ui.scopes.AboutViewScope;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@AboutViewScope
public class AboutViewModel implements DataBindingObservable {
    private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();
    private final Resources mResources;
    private final AnalyticsService mAnalyticsService;
    private final NavigationService mNavigationService;
    private CompositeSubscription mSubscriptions;
    private Spanned mDescription;

    @Inject
    public AboutViewModel(@ApplicationResources Resources resources,
                          AnalyticsService analyticsService, NavigationService NavigationService) {
        mResources = resources;
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
        String[] copyrightStrings = mResources.getStringArray(R.array.licensing);
        String allCopyrightsString = StringBackport.join("", copyrightStrings);
        String descriptionHtml = mResources
                .getString(R.string.about_view_template, BuildConfig.VERSION_NAME,
                        BuildConfig.VERSION_CODE, allCopyrightsString);
        setDescription(Html.fromHtml(descriptionHtml));
    }

    public void onSave(Bundle outState) {
        InstanceStateUtils.save(this, outState);
    }

    public void onPause() {
        mSubscriptions.unsubscribe();
    }

    @Bindable
    public Spanned getDescription() {
        return mDescription;
    }

    private void setDescription(Spanned description) {
        mDescription = description;
        mPropertyChangeRegistry.notifyChange(this, BR.description);
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
