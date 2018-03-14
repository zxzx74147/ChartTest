package com.zxzx74147.devlib.network;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.widget.CommonLoading;
import com.zxzx74147.devlib.widget.CommonProgressDialog;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhengxin on 2018/2/12.
 */

public class NetworkApi {
    public static <T> void ApiSubscribe(Observable<? extends T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static <T extends UniApiData> void ApiSubscribe(Lifecycle lifecycle,Observable<? extends T> observable, Observer<T> observer) {

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


    public static <T extends UniApiData> void ApiSubscribe(LifecycleOwner owner,Observable<? extends T> observable,boolean hasProgress, Consumer<T> observer, Class<T> mClass) {
        final Disposable[] mDisposable = {null};

        CommonLoading mProgress = null;
        if(hasProgress){
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
                    if(finalMProgress!=null){
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
                        if(finalMProgress !=null){
                            finalMProgress.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDisposable[0] = null;
                        T rsp = UniApiData.createError(e, mClass);
                        try {
                            observer.accept(rsp);
                        } catch (Exception e1) {
                            e.printStackTrace();
                        }
                        if(finalMProgress !=null){
                            finalMProgress.dismiss();
                        }


                    }

                    @Override
                    public void onComplete() {
                        mDisposable[0] = null;
                    }
                });
    }

    public static <T extends UniApiData> void ApiSubscribe(LifecycleOwner owner, Observable<? extends T> observable, Consumer<T> observer, Class<T> mClass) {
        ApiSubscribe(owner,observable,false,observer,mClass);
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
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



}
