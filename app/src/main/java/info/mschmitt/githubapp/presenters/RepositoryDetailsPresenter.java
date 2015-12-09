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
    private final CompositeSubscription mSubscriptions = new CompositeSubscription();
    private final Observable<Repository> mRepository;
    private final AnalyticsManager mAnalyticsManager;
    private final RepositoryDetailsView mView;
    private String mRepositoryUrl;
    private String mRepositoryName;

    public RepositoryDetailsPresenter(RepositoryDetailsView view, Observable<Repository> repository,
                                      AnalyticsManager analyticsManager) {
        mView = view;
        mRepository = repository;
        mAnalyticsManager = analyticsManager;
    }

    public static RepositoryDetailsPresenter createForRepositoryId(RepositoryDetailsView view,
                                                                       Observable<LinkedHashMap<Long, Repository>> repositories,
                                                                       AnalyticsManager
                                                                               analyticsManager,
                                                                       long repositoryId) {
        return new RepositoryDetailsPresenter(view,
                mapByRepositoryId(repositories, repositoryId), analyticsManager);
    }

    @NonNull
    private static Observable<Repository> mapByRepositoryId(
            Observable<LinkedHashMap<Long, Repository>> repositories, long repositoryId) {
        return repositories.map(nextRepositories -> nextRepositories.get(repositoryId))
                .filter(nextRepository -> nextRepository != null);
    }

    public static RepositoryDetailsPresenter createForRepositoryPosition(
            RepositoryDetailsView view, Observable<LinkedHashMap<Long, Repository>> repositories,
            AnalyticsManager analyticsManager, int position) {
        return new RepositoryDetailsPresenter(view,
                mapByRepositoryPosition(repositories, position), analyticsManager);
    }

    @NonNull
    private static Observable<Repository> mapByRepositoryPosition(
            Observable<LinkedHashMap<Long, Repository>> repositories, int position) {
        return repositories.map(nextRepositories -> {
            Collection<Repository> values = nextRepositories.values();
            return values.size() > position ? new ArrayList<>(values).get(position) : null;
        }).filter(nextRepository -> nextRepository != null);
    }

    public void onCreate(Bundle savedState) {
        mSubscriptions.add(mRepository.subscribe(this::setRepository));
        mAnalyticsManager.logScreenView(getClass().getName());
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

    public interface RepositoryDetailsView {
        ParentPresenter getParentPresenter();
    }

    public interface ParentPresenter {
    }
}
