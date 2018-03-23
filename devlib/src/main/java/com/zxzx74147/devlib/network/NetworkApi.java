package com.zxzx74147.devlib.network;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.util.Log;

import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.fragment.CommonNetworkErrorDialog;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.devlib.widget.CommonLoading;

import java.net.ConnectException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class NetworkApi {
    private static final String TAG = NetworkApi.class.getSimpleName();

    public static <T> void ApiSubscribe(Observable<? extends T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static <T extends UniApiData> void ApiSubscribe(Lifecycle lifecycle, Observable<? extends T> observable, Observer<T> observer) {

        final Disposable[] mDisposable = {null};
        if (lifecycle != null) {
            lifecycle.addObserver(new LifecycleObserver() {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                public void onDestroy() {
                    if (mDisposable[0] != null) {
                        mDisposable[0].dispose();
                        mDisposable[0] = null;
                    }
                }
            });
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    public static <T extends UniApiData> void ApiSubscribe(LifecycleOwner owner, Observable<? extends T> observable, boolean hasProgress, Consumer<T> observer, Class<T> mClass) {
        final Disposable[] mDisposable = {null};

        CommonLoading mProgress = null;
        if (hasProgress) {
            mProgress = new CommonLoading(ViewUtil.getContext(owner));
            mProgress.show();
        }
        CommonLoading finalMProgress = mProgress;


        if (owner != null) {
            owner.getLifecycle().addObserver(new LifecycleObserver() {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                public void onDestroy() {
                    if (mDisposable[0] != null) {
                        mDisposable[0].dispose();
                        mDisposable[0] = null;
                    }
                    if (finalMProgress != null) {
                        finalMProgress.dismiss();
                    }
                }
            });
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable[0] = d;
                    }

                    @Override
                    public void onNext(T o) {
                        try {
                            observer.accept(o);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mDisposable[0] = null;
                        if (finalMProgress != null) {
                            finalMProgress.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {


                        Log.e(TAG, e.getMessage());
                        mDisposable[0] = null;
                        T rsp = UniApiData.createError(e, mClass);
                        rsp.error.usermsg="网络错误";
                        try {
                            observer.accept(rsp);
                        } catch (Exception e1) {
                            e.printStackTrace();
                        }
                        if (finalMProgress != null) {
                            finalMProgress.dismiss();
                        }


                        if (e instanceof ConnectException || e instanceof HttpException) {
                            if (CommonNetworkErrorDialog.mDialog != null && CommonNetworkErrorDialog.mDialog.get() != null) {
                                return;
                            }
                            CommonNetworkErrorDialog dialog = CommonNetworkErrorDialog.newInstance();
                            ZXFragmentJumpHelper.startFragment((Context) owner, dialog, new CommonCallback() {
                                @Override
                                public void callback(Object item) {
                                    ApiSubscribe(owner, observable, hasProgress, observer, mClass);
                                    dialog.dismiss();
                                }
                            });
                            return;
                        }


                    }

                    @Override
                    public void onComplete() {
                        mDisposable[0] = null;
                    }
                });
    }

    public static <T extends UniApiData> void ApiSubscribe(LifecycleOwner owner, Observable<? extends T> observable, Consumer<T> observer, Class<T> mClass) {
        ApiSubscribe(owner, observable, false, observer, mClass);
    }

    public static <T> void ApiSubscribe(Observable<T> observable, Consumer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }


                    @Override
                    public void onNext(T o) {
                        try {
                            observer.accept(o);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e!=null&&e.getMessage()!=null) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
