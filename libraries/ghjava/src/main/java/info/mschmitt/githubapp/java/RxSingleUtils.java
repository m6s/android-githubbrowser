package info.mschmitt.githubapp.java;

import rx.Single;
import rx.Subscription;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;

/**
 * @author Matthias Schmitt
 */
public class RxSingleUtils {
    public static <T> void subscribe(Single<T> single, Action1<Subscription> onSubscribed) {
        subscribe(single, t -> {
        }, onSubscribed);
    }

    public static <T> void subscribe(Single<T> single, final Action1<? super T> onSuccess,
                                     Action1<Subscription> onSubscribed) {
        subscribe(single, onSuccess, throwable -> {
            throw new OnErrorNotImplementedException(throwable);
        }, onSubscribed);
    }

    public static <T> void subscribe(Single<T> single, final Action1<? super T> onSuccess,
                                     final Action1<Throwable> onError,
                                     Action1<Subscription> onSubscribed) {
        ConnectableObservable<T> connectableObservable = single.toObservable().publish();
        connectableObservable.subscribe(onSuccess, onError, () -> {
        });
        connectableObservable.connect(onSubscribed::call);
    }
}
