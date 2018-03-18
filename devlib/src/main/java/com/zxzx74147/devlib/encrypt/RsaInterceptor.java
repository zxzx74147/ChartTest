package com.zxzx74147.devlib.encrypt;

import android.text.TextUtils;
import android.util.Log;

import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.network.NetworkConfig;
import com.zxzx74147.devlib.utils.RsaUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by zhengxin on 2018/3/16.
 */

public class RsaInterceptor implements Interceptor {
    private static final String TAG = RsaInterceptor.class.getSimpleName();

    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    static {
        RsaPare.sharedInstance().init();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {


        Request request = chain.request();
        return chain.proceed(request);
//        if(request.url().toString().contains("init")){
//            return chain.proceed(request);
//        }
//        RequestBody oldBody = request.body();
//        Buffer buffer = new Buffer();
//        oldBody.writeTo(buffer);
//        String strOldBody = buffer.readUtf8();
//        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
//        String strNewBody = RsaUtil.encrypt(RsaPare.sharedInstance().getPublicKey(), strOldBody);
//        Log.i(TAG,"REQUEST OLD"+oldBody);
//        Log.i(TAG,"REQUEST NEW"+strNewBody);
//        RequestBody body = RequestBody.create(mediaType, strNewBody);
//        HttpUrl url = request.url().newBuilder().addQueryParameter("format", "rsa").build();
//        request = request.newBuilder().header("Content-Type", body.contentType().toString()).header("Content-Length", String.valueOf(body.contentLength())).method(request.method(), body).url(url).build();
//        Response rsp = chain.proceed(request);
//        rsp.body().bytes();


//        return rsp;
    }
}
