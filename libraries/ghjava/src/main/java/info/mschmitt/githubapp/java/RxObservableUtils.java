package info.mschmitt.githubapp.java;

import rx.Observable;
import rx.Subscription;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;

/**
 * @author Matthias Schmitt
 */
public class RxObservableUtils {
    public static <T> void subscribe(Observable<T> observable, Action1<Subscription> onSubscribed) {
        subscribe(observable, t -> {
        }, onSubscribed);
    }

    public static <T> void subscribe(Observable<T> observable, final Action1<? super T> onNext,
                                     Action1<Subscription> onSubscribed) {
        subscribe(observable, onNext, throwable -> {
            throw new OnErrorNotImplementedException(throwable);
        }, onSubscribed);
    }

    public static <T> void subscribe(Observable<T> observable, final Action1<? super T> onNext,
                                     final Action1<Throwable> onError,
                                     Action1<Subscription> onSubscribed) {
        subscribe(observable, onNext, onError, () -> {
        }, onSubscribed);
    }

    public static <T> void subscribe(Observable<T> observable, final Action1<? super T> onNext,
                                     final Action1<Throwable> onError, final Action0 onComplete,
                                     Action1<Subscription> onSubscribed) {
        ConnectableObservable<T> connectableObservable = observable.publish();
        connectableObservable.subscribe(onNext, onError, onComplete);
        connectableObservable.connect(onSubscribed::call);
    }
}
