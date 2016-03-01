package info.mschmitt.githubbrowser.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Matthias Schmitt
 */
public class LoadingProgressManager {
    private final List<Object> mLoadingQueue = new ArrayList<>();
    private final Map<Object, Runnable> mCancellationHandlers = new HashMap<>();
    private final BehaviorSubject<Boolean> mLoadingSubject = BehaviorSubject.create(false);

    public boolean cancelAllTasks(boolean runCancelHandlers) {
        if (isLoading()) {
            List<Runnable> cancellationHandlers =
                    runCancelHandlers ? new ArrayList<>(mCancellationHandlers.values()) :
                            Collections.emptyList();
            mCancellationHandlers.clear();
            mLoadingQueue.clear();
            setLoading(false);
            for (Runnable cancellationHandler : cancellationHandlers) {
                cancellationHandler.run();
            }
            return true;
        }
        return false;
    }

    public boolean isLoading() {
        return mLoadingSubject.getValue();
    }

    private void setLoading(boolean loading) {
        mLoadingSubject.onNext(loading);
    }

    public Observable<Boolean> getLoadingStateObservable() {
        return mLoadingSubject.asObservable();
    }

    public void notifyLoadingBegin(Object sender, Runnable cancelHandler) {
        mLoadingQueue.add(sender);
        mCancellationHandlers.put(sender, cancelHandler);
        setLoading(true);
    }

    public void notifyLoadingEnd(Object sender) {
        mLoadingQueue.remove(sender);
        mCancellationHandlers.remove(sender);
        setLoading(!mLoadingQueue.isEmpty());
    }
}
