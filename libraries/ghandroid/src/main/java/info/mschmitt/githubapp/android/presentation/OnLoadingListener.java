package info.mschmitt.githubapp.android.presentation;

/**
 * @author Matthias Schmitt
 */
public interface OnLoadingListener {
    OnLoadingListener IGNORE = (sender, subscription, complete) -> {
    };

    void onLoading(Object sender, boolean complete, Runnable cancelHandler);
}
