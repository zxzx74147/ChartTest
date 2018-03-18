package com.zxzx74147.devlib.network;

import android.text.TextUtils;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.zxzx74147.devlib.encrypt.RsaInterceptor;
import com.zxzx74147.devlib.modules.account.AccountManager;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class RetrofitClient {
    private static final String TAG = RetrofitClient.class.getSimpleName();
    private static Retrofit mStockRetrofit;
    private static Retrofit mRetrofit;
    private static Retrofit mRSARetrofit;
    private RetrofitClient(){
    }

    static{
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.connectTimeout(7, TimeUnit.SECONDS);
        builder.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("User-Agent", NetworkConfig.UA).build();
            if(!TextUtils.isEmpty(AccountManager.sharedInstance().getUid())) {
                HttpUrl url = request.url().newBuilder().addQueryParameter("uId", AccountManager.sharedInstance().getUid()).build();
                request = request.newBuilder().url(url).build();
            }

            Log.i(TAG,request.url().toString());
            return chain.proceed(request);
        });
        builder.addInterceptor(new StethoInterceptor());
        OkHttpClient client = builder.build();
        mStockRetrofit = new Retrofit.Builder()
                .baseUrl(NetworkConfig.HOST_STOCK)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();


        client = builder.build();
        mRetrofit= new Retrofit.Builder()
                .baseUrl(NetworkConfig.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        builder.addInterceptor(new RsaInterceptor());
        client = builder.build();
        mRSARetrofit= new Retrofit.Builder()
                .baseUrl(NetworkConfig.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

    }

    public static Retrofit getClient(){
        return mRetrofit;
    }

    public static Retrofit getRSAClient(){
        return mRSARetrofit;
    }

    public static Retrofit getStockClient(){
        return mStockRetrofit;
    }
}
