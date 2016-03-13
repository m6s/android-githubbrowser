package info.mschmitt.githubbrowser.ui.viewmodels;

import android.databinding.Bindable;
import android.databinding.PropertyChangeRegistry;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import javax.inject.Inject;

import info.mschmitt.githubbrowser.BR;
import info.mschmitt.githubbrowser.android.databinding.DataBindingObservable;
import info.mschmitt.githubbrowser.entities.Repository;
import info.mschmitt.githubbrowser.ui.scopes.RepositoryDetailsViewScope;
import info.mschmitt.githubbrowser.ui.viewmodels.qualifiers.RepositoryMapObservable;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
@RepositoryDetailsViewScope
public class RepositoryDetailsViewModel implements DataBindingObservable {
    private final Observable<LinkedHashMap<Long, Repository>> mRepositoryMapObservable;
    private final AnalyticsService mAnalyticsService;
    private final PropertyChangeRegistry mPropertyChangeRegistry = new PropertyChangeRegistry();
    private Observable<Repository> mRepositoryObservable;
    private CompositeSubscription mSubscriptions;
    private String mRepositoryUrl;
    private String mRepositoryName;

    @Inject
    public RepositoryDetailsViewModel(@RepositoryMapObservable
                                      Observable<LinkedHashMap<Long, Repository>>
                                                  repositoryMapObservable,
                                      AnalyticsService analyticsService) {
        mRepositoryMapObservable = repositoryMapObservable;
        mAnalyticsService = analyticsService;
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
        connectModel();
        mAnalyticsService.logRepositoryDetailsShown();
    }

    private void connectModel() {
        mSubscriptions.add(mRepositoryObservable.subscribe(this::onNextRepository));
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

    public interface AnalyticsService {
        void logRepositoryDetailsShown();
    }
}
