package com.androidtitan.culturedapp.main.web.retrofit;

import com.androidtitan.culturedapp.main.web.ArticleDeserializer;
import com.androidtitan.culturedapp.main.web.DateDeserializer;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amohnacs on 3/21/16.
 */
public class ServiceGenerator {

    public static final String NEWYORKTIMES_BASE_URL = "http://api.nytimes.com";

    private static Retrofit.Builder nytRetrofitBulder = new Retrofit.Builder()
            . baseUrl(NEWYORKTIMES_BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(buildGsonConverter());

    public static <S> S createService(Class<S> serviceClass) {

        Retrofit retrofit = nytRetrofitBulder.client(httpClient().build()).build();
        return retrofit.create(serviceClass);
    }

    public static Retrofit retrofit = nytRetrofitBulder.client(httpClient().build()).build();

    private static OkHttpClient.Builder httpClient() {
        //logger
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder().addInterceptor(interceptor);
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //adding custom deserializer
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(Article.class, new ArticleDeserializer());
        gsonBuilder.serializeNulls();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();

        Gson myGson = gsonBuilder.create();


        return GsonConverterFactory.create(myGson);
    }

}
