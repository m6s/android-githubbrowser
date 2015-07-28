package info.mschmitt.githubapp.android.presentation;

/**
 * @author Matthias Schmitt
 */
public interface OnErrorListener {
    OnErrorListener IGNORE = (sender, throwable, retryHandler) -> {
    };

    void onError(Object sender, Throwable throwable, Runnable retryHandler);
}
