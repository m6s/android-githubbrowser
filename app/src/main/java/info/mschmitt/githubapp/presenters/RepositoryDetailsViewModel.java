package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.domain.AnalyticsManager;
import info.mschmitt.githubapp.entities.Repository;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoryDetailsViewModel extends BaseObservable {
    private final AnalyticsManager mAnalyticsManager;
    private final NavigationHandler mNavigationHandler;
    private Observable<Repository> mRepository;
    private CompositeSubscription mSubscriptions;
    private String mRepositoryUrl;
    private String mRepositoryName;

    public RepositoryDetailsViewModel(AnalyticsManager analyticsManager,
                                      NavigationHandler navigationHandler) {
        mAnalyticsManager = analyticsManager;
        mNavigationHandler = navigationHandler;
    }

    public void onCreateForPosition(int position, Bundle savedState) {
        mRepository = mapByRepositoryPosition(mNavigationHandler.getRepositoryMap(), position);
        onCreate(savedState);
    }

    @NonNull
    private static Observable<Repository> mapByRepositoryPosition(
            Observable<LinkedHashMap<Long, Repository>> repositories, int position) {
        return repositories.map(nextRepositories -> {
            Collection<Repository> values = nextRepositories.values();
            return values.size() > position ? new ArrayList<>(values).get(position) : null;
        }).filter(nextRepository -> nextRepository != null);
    }

    private void onCreate(Bundle savedState) {
        mSubscriptions = new CompositeSubscription();
        mSubscriptions.add(mRepository.subscribe(this::setRepository));
        mAnalyticsManager.logScreenView(getClass().getName());
    }

    public void onCreateForId(long repositoryId, Bundle savedState) {
        mRepository = mapByRepositoryId(mNavigationHandler.getRepositoryMap(), repositoryId);
        onCreate(savedState);
    }

    @NonNull
    private static Observable<Repository> mapByRepositoryId(
            Observable<LinkedHashMap<Long, Repository>> repositories, long repositoryId) {
        return repositories.map(nextRepositories -> nextRepositories.get(repositoryId))
                .filter(nextRepository -> nextRepository != null);
    }

    public void onSave(Bundle outState) {
    }

    private void setRepository(Repository repository) {
        mRepositoryName = repository.getName();
        mRepositoryUrl = repository.getUrl();
        notifyPropertyChanged(BR.repositoryName);
        notifyPropertyChanged(BR.repositoryUrl);
    }

    @Bindable
    public String getRepositoryName() {
        return mRepositoryName;
    }

    @Bindable
    public String getRepositoryUrl() {
        return mRepositoryUrl;
    }

    public void onDestroy() {
        mSubscriptions.unsubscribe();
    }

    public interface NavigationHandler {
        Observable<LinkedHashMap<Long, Repository>> getRepositoryMap();
    }
}
