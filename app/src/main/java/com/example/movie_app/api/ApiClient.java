package com.example.movie_app.api;

import android.content.Context;

import com.example.movie_app.R;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor;
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin;


public class ApiClient {

    public static Retrofit getClient(Context context) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        NetworkFlipperPlugin networkFlipperPlugin = new NetworkFlipperPlugin();
        OkHttpClient.Builder client =
                new OkHttpClient.Builder()
                .addNetworkInterceptor(new FlipperOkhttpInterceptor(networkFlipperPlugin));
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", context.getResources().getString(R.string.api_key))
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.api_base_path))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }
}
