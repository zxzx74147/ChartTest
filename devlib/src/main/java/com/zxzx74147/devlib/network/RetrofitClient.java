package com.zxzx74147.devlib.network;

import android.text.TextUtils;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.zxzx74147.devlib.encrypt.RsaInterceptor;
import com.zxzx74147.devlib.modules.account.AccountManager;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class RetrofitClient {
    private static final String TAG = RetrofitClient.class.getSimpleName();
    private static Dispatcher dispatcher=new Dispatcher();

    private static Retrofit mStockRetrofit;
    private static Retrofit mRetrofit;
    private static Retrofit mRSARetrofit;
    private RetrofitClient(){
    }

    static{
        dispatcher.setMaxRequests(30);


        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.connectTimeout(7, TimeUnit.SECONDS);
        builder.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("User-Agent", NetworkConfig.UA).build();

            if(!TextUtils.isEmpty(AccountManager.sharedInstance().getUid())) {
                if("POST".equals(request.method())&&request.body() instanceof FormBody) {
                    if(request.body() instanceof FormBody){
                        Request.Builder requestBuilder = request.newBuilder();
                        FormBody.Builder newFormBody = new FormBody.Builder();
                        FormBody oidFormBody = (FormBody) request.body();
                        for (int i = 0;i<oidFormBody.size();i++){
                            newFormBody.addEncoded(oidFormBody.encodedName(i),oidFormBody.encodedValue(i));
                        }
                        newFormBody.add("uId",AccountManager.sharedInstance().getUid());
                        request = requestBuilder.method(request.method(),newFormBody.build()).build();
                        if(request.body()!=null) {
                            Buffer buffer = new Buffer();
                            request.body().writeTo(buffer);
                            Log.i(TAG+"POST",request.url().toString()+(request.url().toString().contains("?")? "&":"?")+buffer.readUtf8());
                        }
                    }
                }else{
                    HttpUrl url = request.url().newBuilder().addQueryParameter("uId", AccountManager.sharedInstance().getUid()).build();
                    request = request.newBuilder().url(url).build();
                    Log.i(TAG+request.method(),request.url().toString());
                }
            }


            return chain.proceed(request);
        });
        builder.addInterceptor(new StethoInterceptor());
        OkHttpClient client = builder.build();
        mStockRetrofit = new Retrofit.Builder()
                .baseUrl(NetworkConfig.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        builder.addInterceptor(new RsaInterceptor());
        builder.dispatcher(dispatcher);
        client = builder.build();
        mRetrofit= new Retrofit.Builder()
                .baseUrl(NetworkConfig.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();


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

//    public static Retrofit getRSAClient(){
//        return mRSARetrofit;
//    }

    public static Retrofit getStockClient(){
        return mStockRetrofit;
    }

    public static void cancelAll(){
        if(dispatcher!=null) {
            dispatcher.cancelAll();
        }
    }
}
