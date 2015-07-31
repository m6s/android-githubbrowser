package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.List;

import info.mschmitt.githubapp.AnalyticsManager;
import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.entities.Repository;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Matthias Schmitt
 */
public class RepositoryDetailsViewPresenter extends BaseObservable {
    private final CompositeSubscription mSubscriptions = new CompositeSubscription();
    private final Observable<Repository> mRepository;
    private final AnalyticsManager mAnalyticsManager;
    private final RepositoryDetailsView mView;
    private String mRepositoryUrl;
    private String mRepositoryName;

    public RepositoryDetailsViewPresenter(RepositoryDetailsView view,
                                          Observable<List<Repository>> repositories,
                                          long repositoryId, AnalyticsManager analyticsManager) {
        this(view, mapByRepositoryId(repositories, repositoryId), analyticsManager);
    }

    public RepositoryDetailsViewPresenter(RepositoryDetailsView view,
                                          Observable<Repository> repository,
                                          AnalyticsManager analyticsManager) {
        mView = view;
        mRepository = repository;
        mAnalyticsManager = analyticsManager;
    }

    @NonNull
    private static Observable<Repository> mapByRepositoryId(
            Observable<List<Repository>> repositories, long repositoryId) {
        return repositories.map(nextRepositories -> {
            for (Repository repository : nextRepositories) {
                if (repository.getId() == repositoryId) {
                    return repository;
                }
            }
            return null;
        }).filter(nextRepository -> nextRepository != null);
    }

    public void onCreate(Bundle savedState) {
        mSubscriptions.add(mRepository.subscribe(this::setLastRepository));
        mAnalyticsManager.logScreenView(getClass().getName());
    }

    public void onSave(Bundle outState) {
    }

    public void setLastRepository(Repository repository) {
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
