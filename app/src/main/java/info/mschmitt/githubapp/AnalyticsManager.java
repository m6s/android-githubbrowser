package info.mschmitt.githubapp;

import android.app.Application;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Matthias Schmitt
 */
@Singleton
public class AnalyticsManager {
    private Application mApplication;

    @Inject
    public AnalyticsManager(Application application) {
        mApplication = application;
    }

    public void logScreenView(String screenName) {
        Log.d("X", "Logged screen name: " + screenName);
    }

    public void logError(Throwable throwable) {
        Log.d("X", "Error: " + throwable.getMessage());
    }
}
