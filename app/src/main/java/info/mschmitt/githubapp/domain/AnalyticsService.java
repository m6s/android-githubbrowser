package info.mschmitt.githubapp.domain;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author Matthias Schmitt
 */
@Singleton
public class AnalyticsService {
    private Context mContext;

    @Inject
    public AnalyticsService(@Named("ApplicationContext") Context context) {
        mContext = context;
    }

    public void logScreenView(String screenName) {
        Log.d("X", "Logged screen name: " + screenName);
    }

    public void logError(Throwable throwable) {
        Log.d("X", "Error: " + throwable.getMessage());
    }
}
