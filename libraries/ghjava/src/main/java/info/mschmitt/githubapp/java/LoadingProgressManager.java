package info.mschmitt.githubapp.java;

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
    private final BehaviorSubject<Boolean> mLoadingSubject = BehaviorSubject.create();
    private boolean mLoading;

    public boolean cancelAllTasks(boolean runCancelHandlers) {
        if (mLoading) {
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

    private void setLoading(boolean loading) {
        mLoading = loading;
        mLoadingSubject.onNext(loading);
    }

    public Observable<Boolean> isLoading() {
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
