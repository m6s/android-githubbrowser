package info.mschmitt.githubbrowser.ui;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import info.mschmitt.githubbrowser.app.dagger.ApplicationContext;
import info.mschmitt.githubbrowser.ui.viewmodels.AboutViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositoryDetailsViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositoryListViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.RepositorySplitViewModel;
import info.mschmitt.githubbrowser.ui.viewmodels.UsernameViewModel;

/**
 * @author Matthias Schmitt
 */
@Singleton
public class AnalyticsService
        implements UsernameViewModel.AnalyticsService, RepositorySplitViewModel.AnalyticsService,
        RepositoryListViewModel.AnalyticsService, RepositoryDetailsViewModel.AnalyticsService,
        AboutViewModel.AnalyticsService {
    private Context mContext;

    @Inject
    public AnalyticsService(@ApplicationContext Context context) {
        mContext = context;
    }

    @Override
    public void logRepositoryDetailsShown() {
        Log.d("X", "Repository details shown");
    }

    @Override
    public void logRepositoryListShown() {
        Log.d("X", "Repository list shown");
    }

    @Override
    public void logUsernameShown() {
        Log.d("X", "Username shown");
    }

    @Override
    public void logError(Throwable throwable) {
        Log.d("X", "Error: " + throwable.getMessage());
    }
}
