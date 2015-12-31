package info.mschmitt.githubapp.domain;

import android.content.Context;
import android.util.Log;

/**
 * @author Matthias Schmitt
 */
public class AnalyticsService {
    private Context mContext;

    public AnalyticsService(Context context) {
        mContext = context;
    }

    public void logScreenView(String screenName) {
        Log.d("X", "Logged screen name: " + screenName);
    }

    public void logError(Throwable throwable) {
        Log.d("X", "Error: " + throwable.getMessage());
    }
}
