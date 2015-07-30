package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;

import javax.inject.Inject;

import info.mschmitt.githubapp.AnalyticsManager;
import info.mschmitt.githubapp.BR;
import info.mschmitt.githubapp.entities.Repository;

/**
 * @author Matthias Schmitt
 */
public class RepositoryDetailsViewPresenter extends BaseObservable {
    public static final String STATE_REPOSITORY = "STATE_REPOSITORY";
    private AnalyticsManager mAnalyticsManager;
    private Repository mRepository;
    private RepositoryDetailsView mView;

    @Inject
    public RepositoryDetailsViewPresenter(AnalyticsManager analyticsManager) {
        mAnalyticsManager = analyticsManager;
    }

    public void onCreate(Bundle savedState) {
        if (savedState != null) {
            mRepository = (Repository) savedState.getSerializable(STATE_REPOSITORY);
        }
        mAnalyticsManager.logScreenView(getClass().getName());
    }

    public void postInject(RepositoryDetailsView view) {
        mView = view;
    }

    public void onSave(Bundle outState) {
        outState.putSerializable(STATE_REPOSITORY, mRepository);
    }

    public Repository getRepository() {
        return mRepository;
    }

    public void setRepository(Repository repository) {
        mRepository = repository;
        notifyPropertyChanged(BR.repositoryName);
        notifyPropertyChanged(BR.repositoryUrl);
    }

    @Bindable
    public String getRepositoryName() {
        return mRepository != null ? mRepository.getName() : "";
    }

    @Bindable
    public String getRepositoryUrl() {
        return mRepository != null ? mRepository.getUrl() : "";
    }

    public void onDestroy() {

    }

    public interface RepositoryDetailsView {
        ParentPresenter getParentPresenter();
    }

    public interface ParentPresenter {
    }
}
