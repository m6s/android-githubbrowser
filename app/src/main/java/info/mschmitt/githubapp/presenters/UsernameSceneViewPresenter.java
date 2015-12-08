package info.mschmitt.githubapp.presenters;

import android.databinding.BaseObservable;
import android.os.Bundle;

import javax.inject.Inject;

import info.mschmitt.githubapp.android.presentation.OnErrorListener;
import info.mschmitt.githubapp.android.presentation.OnLoadingListener;
import info.mschmitt.githubapp.presenters.UsernameViewPresenter;

/**
 * @author Matthias Schmitt
 */
public class UsernameSceneViewPresenter extends BaseObservable
        implements UsernameViewPresenter.ParentPresenter {
    private final UsernameSceneView mView;

    @Inject
    public UsernameSceneViewPresenter(UsernameSceneView view) {
        mView = view;
    }

    public void onCreate(Bundle savedState) {
    }

    public void onSave(Bundle outState) {
    }

    public void onDestroy() {
        // stop long running operations
    }

    @Override
    public void onShowRepositories(Object sender, String username) {
        mView.showRepositories(sender, username);
    }

    @Override
    public void onError(Object sender, Throwable throwable, Runnable retryHandler) {
        mView.getParentPresenter().onError(sender, throwable, retryHandler);
    }

    @Override
    public void onLoading(Object sender, boolean complete, Runnable cancelHandler) {
        mView.getParentPresenter().onLoading(sender, complete, cancelHandler);
    }

    public interface UsernameSceneView {
        ParentPresenter getParentPresenter();

        void showRepositories(Object sender, String username);
    }

    public interface ParentPresenter extends OnLoadingListener, OnErrorListener {
    }
}
