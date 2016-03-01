package info.mschmitt.githubbrowser.network.internal.utils;

import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Single;
import rx.subscriptions.Subscriptions;

/**
 * @author Matthias Schmitt
 */
public class OkHttpUtils {
    public static <T> Single<T> toSingle(OkHttpClient client, Request request,
                                         TypeAdapter<T> typeAdapter,
                                         TypeAdapter<? extends Throwable> errorAdapter) {
        return Single.create(subscriber -> {
            if (subscriber.isUnsubscribed()) {
                return;
            }
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (subscriber.isUnsubscribed()) {
                        return;
                    }
                    subscriber.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (subscriber.isUnsubscribed()) {
                        return;
                    }
                    if (response.isSuccessful()) {
                        T t = typeAdapter.fromJson(response.body().charStream());
                        subscriber.onSuccess(t);
                    } else {
                        Throwable throwable = errorAdapter.fromJson(response.body().charStream());
                        subscriber.onError(throwable);
                    }
                }
            });
            subscriber.add(Subscriptions.create(call::cancel));
        });
    }
}
