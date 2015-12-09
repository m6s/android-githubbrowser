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
public class RepositoryDetailsPresenter extends BaseObservable {
    private final Observable<LinkedHashMap<Long, Repository>> mRepositories;
    private final AnalyticsManager mAnalyticsManager;
    private Observable<Repository> mRepository;
    private CompositeSubscription mSubscriptions;
    private RepositoryDetailsView mView;
    private String mRepositoryUrl;
    private String mRepositoryName;

    public RepositoryDetailsPresenter(Observable<LinkedHashMap<Long, Repository>> repositories,
                                      AnalyticsManager analyticsManager) {
        mRepositories = repositories;
        mAnalyticsManager = analyticsManager;
    }

    public void onCreateForPosition(RepositoryDetailsView view, int position, Bundle savedState) {
        mRepository = mapByRepositoryPosition(mRepositories, position);
        onCreate(view, savedState);
    }

    @NonNull
    private static Observable<Repository> mapByRepositoryPosition(
            Observable<LinkedHashMap<Long, Repository>> repositories, int position) {
        return repositories.map(nextRepositories -> {
            Collection<Repository> values = nextRepositories.values();
            return values.size() > position ? new ArrayList<>(values).get(position) : null;
        }).filter(nextRepository -> nextRepository != null);
    }

    private void onCreate(RepositoryDetailsView view, Bundle savedState) {
        mSubscriptions = new CompositeSubscription();
        mView = view;
        mSubscriptions.add(mRepository.subscribe(this::setRepository));
        mAnalyticsManager.logScreenView(getClass().getName());
    }

    public void onCreateForId(RepositoryDetailsView view, long repositoryId, Bundle savedState) {
        mRepository = mapByRepositoryId(mRepositories, repositoryId);
        onCreate(view, savedState);
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
        mView = null;
    }

    public interface RepositoryDetailsView {
        ParentPresenter getParentPresenter();
    }

    public interface ParentPresenter {
    }
}
