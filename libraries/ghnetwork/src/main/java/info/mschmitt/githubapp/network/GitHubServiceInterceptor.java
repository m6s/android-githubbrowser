package info.mschmitt.githubapp.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Matthias Schmitt
 */
public class GitHubServiceInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request customizedRequest = originalRequest.newBuilder()
//                .header("Accept", "application/json")
//                .header("Accept-Charset", "utf-8")
                .method(originalRequest.method(), originalRequest.body()).build();
        return chain.proceed(customizedRequest);
    }
}
