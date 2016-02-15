/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.mschmitt.githubapp.network.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.Result;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * 1. Reverses the changes of https://github.com/square/retrofit/pull/1117
 * <p>
 * I find it pointless to wrap a synchronous call in an Observable. That could be easily achieved by
 * Single.fromCallable().
 * <p>
 * 2. Adds Completable support, which didn't exist when async calling was removed
 */
public final class AsyncRxJavaCallAdapterFactory extends CallAdapter.Factory {
    private AsyncRxJavaCallAdapterFactory() {
    }

    public static AsyncRxJavaCallAdapterFactory create() {
        return new AsyncRxJavaCallAdapterFactory();
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        final String canonicalName = rawType.getCanonicalName();
        boolean isSingle = "rx.Single".equals(canonicalName);
        boolean isCompletable = "rx.Completable".equals(canonicalName);
        if (rawType != Observable.class && !isSingle && !isCompletable) {
            return null;
        }
        if (!isCompletable && !(returnType instanceof ParameterizedType)) {
            String name = isSingle ? "Single" : "Observable";
            throw new IllegalStateException(
                    name + " return type must be parameterized" + " as " + name + "<Foo> or " +
                            name + "<? extends Foo>");
        }
        CallAdapter<Observable<?>> callAdapter = getCallAdapter(returnType);
        if (isCompletable) {
            return CompletableHelper.makeCompletable(callAdapter);
        }
        if (isSingle) {
            // Add Single-converter wrapper from a separate class. This defers classloading such
            // that
            // regular Observable operation can be leveraged without relying on this unstable
            // RxJava API.
            return SingleHelper.makeSingle(callAdapter);
        }
        return callAdapter;
    }

    private CallAdapter<Observable<?>> getCallAdapter(Type returnType) {
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawObservableType = getRawType(observableType);
        if (rawObservableType == Response.class) {
            if (!(observableType instanceof ParameterizedType)) {
                throw new IllegalStateException("Response must be parameterized" +
                        " as Response<Foo> or Response<? extends Foo>");
            }
            Type responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            return new ResponseCallAdapter(responseType);
        }
        if (rawObservableType == Result.class) {
            if (!(observableType instanceof ParameterizedType)) {
                throw new IllegalStateException("Result must be parameterized" +
                        " as Result<Foo> or Result<? extends Foo>");
            }
            Type responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            return new ResultCallAdapter(responseType);
        }
        return new SimpleCallAdapter(observableType);
    }

    static final class CallOnSubscribe<T> implements Observable.OnSubscribe<Response<T>> {
        private final Call<T> originalCall;

        private CallOnSubscribe(Call<T> originalCall) {
            this.originalCall = originalCall;
        }

        @Override
        public void call(final Subscriber<? super Response<T>> subscriber) {
            // Since Call is a one-shot type, clone it for each new subscriber.
            final Call<T> call = originalCall.clone();
            // Attempt to cancel the call if it is still in-flight on unsubscription.
            subscriber.add(Subscriptions.create(new Action0() {
                @Override
                public void call() {
                    call.cancel();
                }
            }));
            if (subscriber.isUnsubscribed()) {
                return;
            }
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(response);
                        subscriber.onCompleted();
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(t);
                    }
                }
            });
        }
    }

    static final class ResponseCallAdapter implements CallAdapter<Observable<?>> {
        private final Type responseType;

        ResponseCallAdapter(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> Observable<Response<R>> adapt(Call<R> call) {
            return Observable.create(new CallOnSubscribe<>(call));
        }
    }

    static final class SimpleCallAdapter implements CallAdapter<Observable<?>> {
        private final Type responseType;

        SimpleCallAdapter(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> Observable<R> adapt(Call<R> call) {
            return Observable.create(new CallOnSubscribe<>(call)) //
                    .flatMap(new Func1<Response<R>, Observable<R>>() {
                        @Override
                        public Observable<R> call(Response<R> response) {
                            if (response.isSuccess()) {
                                return Observable.just(response.body());
                            }
                            return Observable.error(new HttpException(response));
                        }
                    });
        }
    }

    static final class ResultCallAdapter implements CallAdapter<Observable<?>> {
        private final Type responseType;

        ResultCallAdapter(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> Observable<Result<R>> adapt(Call<R> call) {
            return Observable.create(new CallOnSubscribe<>(call)) //
                    .map(new Func1<Response<R>, Result<R>>() {
                        @Override
                        public Result<R> call(Response<R> response) {
                            return Result.response(response);
                        }
                    }).onErrorReturn(new Func1<Throwable, Result<R>>() {
                        @Override
                        public Result<R> call(Throwable throwable) {
                            return Result.error(throwable);
                        }
                    });
        }
    }

    private static final class SingleHelper {
        static CallAdapter<Single<?>> makeSingle(final CallAdapter<Observable<?>> callAdapter) {
            return new CallAdapter<Single<?>>() {
                @Override
                public Type responseType() {
                    return callAdapter.responseType();
                }

                @Override
                public <R> Single<?> adapt(Call<R> call) {
                    Observable<?> observable = callAdapter.adapt(call);
                    return observable.toSingle();
                }
            };
        }
    }

    private static final class CompletableHelper {
        static CallAdapter<Completable> makeCompletable(
                final CallAdapter<Observable<?>> callAdapter) {
            return new CallAdapter<Completable>() {
                @Override
                public Type responseType() {
                    return callAdapter.responseType();
                }

                @Override
                public <R> Completable adapt(Call<R> call) {
                    Observable<?> observable = callAdapter.adapt(call);
                    return observable.toCompletable();
                }
            };
        }
    }
//    private static final class CompletableHelper {
//
//        private static final CallAdapter<Completable> COMPLETABLE_CALL_ADAPTER
//                = new CallAdapter<Completable>() {
//            @Override public Type responseType() {
//                return Void.class;
//            }
//
//            @Override public Completable adapt(Call call) {
//                return Completable.create(new CompletableCallOnSubscribe(call));
//            }
//        };
//
//        static CallAdapter<Completable> makeCompletable() {
//            return COMPLETABLE_CALL_ADAPTER;
//        }
//
//        private static final class CompletableCallOnSubscribe
//                implements Completable.CompletableOnSubscribe {
//            private final Call originalCall;
//
//            CompletableCallOnSubscribe(Call originalCall) {
//                this.originalCall = originalCall;
//            }
//
//            @Override
//            public void call(final Completable.CompletableSubscriber subscriber) {
//                // Since Call is a one-shot type, clone it for each new subscriber.
//                final Call call = originalCall.clone();
//
//                // Attempt to cancel the call if it is still in-flight on unsubscription.
//                Subscription subscription = Subscriptions.create(new Action0() {
//                    @Override public void call() {
//                        call.cancel();
//                    }
//                });
//                subscriber.onSubscribe(subscription);
//
//                try {
//                    Response response = call.execute();
//                    if (!subscription.isUnsubscribed()) {
//                        if (response.isSuccess()) {
//                            subscriber.onCompleted();
//                        } else {
//                            subscriber.onError(new HttpException(response));
//                        }
//                    }
//                } catch (Throwable t) {
//                    Exceptions.throwIfFatal(t);
//                    if (!subscription.isUnsubscribed()) {
//                        subscriber.onError(t);
//                    }
//                }
//            }
//        }
//    }
}
