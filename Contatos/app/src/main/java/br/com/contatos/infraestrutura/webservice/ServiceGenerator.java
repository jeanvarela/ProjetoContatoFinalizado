package br.com.contatos.infraestrutura.webservice;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author JEAN
 *
 * Função: Classe utilitária para realizar acesso ao webservice
 */

public class ServiceGenerator {

    //URL base do endpoint.
    public static final String API_BASE_URL = "http://192.168.0.111:8080/projetocontatos/rest/";

    private static Retrofit retrofit;

    public static <S> S createService(Class<S> serviceClass) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(100000, TimeUnit.SECONDS);
        httpClient.writeTimeout(100000, TimeUnit.SECONDS);
        httpClient.connectTimeout(100000,TimeUnit.SECONDS);

        httpClient.addInterceptor(loggingInterceptor);

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.TRANSIENT);
        Gson gson = builder.create();

        retrofit = new Retrofit.Builder()
                               .baseUrl(API_BASE_URL)
                               .addConverterFactory(GsonConverterFactory.create(gson))
                               .client(httpClient.build())
                               .build();

        return retrofit.create(serviceClass);
    }
}