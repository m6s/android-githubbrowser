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
package info.mschmitt.githubapp.network.internal.utils;

import com.google.gson.reflect.TypeToken;

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
import rx.subscriptions.Subscriptions;

/**
 * 1. Reverses the changes of https://github.com/square/retrofit/pull/1117
 * <p>
 * I find it pointless to wrap a synchronous call in an Observable. That could be easily achieved by
 * Single.fromCallable().
 * <p>
 * 2. Adds Completable support, which didn't exist when async calling was removed
 * <p>
 * 3. Adds the possibility to set an error handler
 */
public final class AsyncRxJavaCallAdapterFactory extends CallAdapter.Factory {
    public static final Type TYPE_VOID = new TypeToken<Void>() {
    }.getType();
    private final ErrorHandler mErrorHandler;

    private AsyncRxJavaCallAdapterFactory(ErrorHandler errorHandler) {
        mErrorHandler = errorHandler;
    }

    public static AsyncRxJavaCallAdapterFactory create(ErrorHandler errorHandler) {
        return new AsyncRxJavaCallAdapterFactory(errorHandler);
    }

    public static AsyncRxJavaCallAdapterFactory create() {
        return new AsyncRxJavaCallAdapterFactory(HttpException::new);
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        boolean isSingle = rawType == Single.class;
        boolean isCompletable = rawType == Completable.class;
        boolean isObservable = rawType == Observable.class;
        if (!isObservable && !isSingle && !isCompletable) {
            return null;
        }
        if (isCompletable) {
            CallAdapter<Observable<?>> callAdapter =
                    new SimpleCallAdapter(TYPE_VOID, mErrorHandler);
            return makeCompletable(callAdapter);
        }
        checkParameterizedType(returnType);
        if (isSingle) {
            CallAdapter<Observable<?>> callAdapter = newCallAdapter(returnType, mErrorHandler);
            return makeSingle(callAdapter);
        }
        return newCallAdapter(returnType, mErrorHandler);
    }

    private static CallAdapter<Completable> makeCompletable(
            CallAdapter<Observable<?>> callAdapter) {
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

    private static void checkParameterizedType(Type returnType) {
        Class<?> rawType = getRawType(returnType);
        String name = rawType.getSimpleName();
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException(
                    name + " return type must be parameterized as " + name + "<Foo> or " +
                            name + "<? extends Foo>");
        }
    }

    private CallAdapter<Observable<?>> newCallAdapter(Type returnType, ErrorHandler errorHandler) {
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawObservableType = getRawType(observableType);
        if (rawObservableType == Response.class) {
            checkParameterizedType(observableType);
            Type responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            return new ResponseCallAdapter(responseType);
        }
        if (rawObservableType == Result.class) {
            checkParameterizedType(observableType);
            Type responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            return new ResultCallAdapter(responseType);
        }
        return new SimpleCallAdapter(observableType, errorHandler);
    }

    private static CallAdapter<Single<?>> makeSingle(CallAdapter<Observable<?>> callAdapter) {
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

    /**
     * @author Matthias Schmitt
     */
    public interface ErrorHandler {
        Throwable handleError(Response<?> response);
    }

    static final class CallOnSubscribe<T> implements Observable.OnSubscribe<Response<T>> {
        private final Call<T> originalCall;

        private CallOnSubscribe(Call<T> originalCall) {
            this.originalCall = originalCall;
        }

        @Override
        public void call(final Subscriber<? super Response<T>> subscriber) {
            final Call<T> call = originalCall.clone();
            subscriber.add(Subscriptions.create(call::cancel));
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

    private static final class SimpleCallAdapter implements CallAdapter<Observable<?>> {
        private final Type responseType;
        private final ErrorHandler mErrorHandler;

        SimpleCallAdapter(Type responseType, ErrorHandler errorHandler) {
            this.responseType = responseType;
            mErrorHandler = errorHandler;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> Observable<R> adapt(Call<R> call) {
            return Observable.create(new CallOnSubscribe<>(call)).flatMap(response -> {
                if (response.isSuccess()) {
                    return Observable.just(response.body());
                }
                return Observable.error(mErrorHandler.handleError(response));
            });
        }
    }

    private static final class ResultCallAdapter implements CallAdapter<Observable<?>> {
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
                    .map(Result::response).onErrorReturn(Result::error);
        }
    }
}
