package com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simplicity.simplicityaclientforreddit.ui.main.Global;
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP;

import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static Retrofit authenticatedRetrofit;
    private static final String BASE_URL = "https://www.reddit.com/";
    private static final String AUTHENTICATED_BASE_URL = "https://oauth.reddit.com/";
    private static final boolean useLogging = true;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new ApiLogger());
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            if (useLogging) {
                httpClient.addInterceptor(logging);
            }
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitInstanceWithCustomConverter(Type type, Object typeAdapter) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new ApiLogger());
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            if (useLogging) {
                httpClient.addInterceptor(logging);
            }
        return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(createGsonConverter(type, typeAdapter))
                    .client(httpClient.build())
                    .build();
    }

    public static Retrofit getRetrofitAuthenticatedInstance() {
        if (authenticatedRetrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new ApiLogger());
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.authenticator(new TokenAuthenticator());
            if (useLogging) {
                httpClient.addInterceptor(logging);
            }
            String accessToken = new SettingsSP().loadSetting(SettingsSP.KEY_ACCESS_TOKEN, "");
            if(!accessToken.isEmpty()) {
                httpClient.addInterceptor(chain -> {
                        Request request = chain.request().newBuilder().addHeader("Authorization", "bearer " + accessToken).build();
                        return chain.proceed(request);
                });
            }
            authenticatedRetrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(AUTHENTICATED_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return authenticatedRetrofit;
    }

    private static Converter.Factory createGsonConverter(Type type, Object typeAdapter) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(type, typeAdapter);
        Gson gson = gsonBuilder.create();

        return GsonConverterFactory.create(gson);
    }


}