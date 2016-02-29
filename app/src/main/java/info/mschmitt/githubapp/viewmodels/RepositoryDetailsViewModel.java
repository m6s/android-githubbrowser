package info.mschmitt.githubapp.viewmodels;

import android.databinding.Bindable;
import android.databinding.PropertyChangeRegistry;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import javax.inject.Inject;

import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.android.presentation.DataBindingObservable;
import info.mschmitt.githubapp.entities.Repository;
import info.mschmitt.githubapp.ghdomain.AnalyticsService;
import info.mschmitt.githubapp.scopes.RepositoryDetailsViewScope;
import info.mschmitt.githubapp.viewmodels.qualifiers.RepositoryMapObservable;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@RepositoryDetailsViewScope
public class RepositoryDetailsViewModel implements DataBindingObservable {
    private final Observable<LinkedHashMap<Long, Repository>> mRepositoryMapObservable;
    private final AnalyticsService mAnalyticsService;
    private final NavigationHandler mNavigationHandler;
    private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();
    private Observable<Repository> mRepositoryObservable;
    private CompositeSubscription mSubscriptions;
    private String mRepositoryUrl;
    private String mRepositoryName;

    @Inject
    public RepositoryDetailsViewModel(@RepositoryMapObservable
                                      Observable<LinkedHashMap<Long, Repository>>
                                                  repositoryMapObservable,
                                      AnalyticsService analyticsService,
                                      NavigationHandler navigationHandler) {
        mRepositoryMapObservable = repositoryMapObservable;
        mAnalyticsService = analyticsService;
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

    public void onLoadForPosition(int position, Bundle savedState) {
        mRepositoryObservable = mapByRepositoryPosition(mRepositoryMapObservable, position);
        onLoad(savedState);
    }

    private static Observable<Repository> mapByRepositoryPosition(
            Observable<LinkedHashMap<Long, Repository>> repositories, int position) {
        return repositories.map(nextRepositories -> {
            Collection<Repository> values = nextRepositories.values();
            return values.size() > position ? new ArrayList<>(values).get(position) : null;
        }).filter(nextRepository -> nextRepository != null);
    }

    private void onLoad(Bundle savedState) {
    }

    public void onResume() {
        mSubscriptions = new CompositeSubscription();
        mSubscriptions.add(mRepositoryObservable.subscribe(this::onNextRepository));
        mAnalyticsService.logScreenView(getClass().getName());
    }

    public void onLoadForId(long repositoryId, Bundle savedState) {
        mRepositoryObservable = mapByRepositoryId(mRepositoryMapObservable, repositoryId);
        onLoad(savedState);
    }

    private static Observable<Repository> mapByRepositoryId(
            Observable<LinkedHashMap<Long, Repository>> repositories, long repositoryId) {
        return repositories.map(nextRepositories -> nextRepositories.get(repositoryId))
                .filter(nextRepository -> nextRepository != null);
    }

    public void onSave(Bundle outState) {
    }

    private void onNextRepository(Repository repository) {
        mRepositoryName = repository.name();
        mRepositoryUrl = repository.url();
        mPropertyChangeRegistry.notifyChange(this, BR.repositoryName);
        mPropertyChangeRegistry.notifyChange(this, BR.repositoryUrl);
    }

    @Bindable
    public String getRepositoryName() {
        return mRepositoryName;
    }

    @Bindable
    public String getRepositoryUrl() {
        return mRepositoryUrl;
    }

    public void onPause() {
        mSubscriptions.unsubscribe();
    }

    public interface NavigationHandler {
    }
}
