package com.zxzx74147.devlib.utils;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dinuscxj.refresh.MaterialDragDistanceConverter;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.interfaces.CommonListRequestCallback;
import com.zxzx74147.devlib.interfaces.IBaseListDataHolder;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.CommonRefreshView;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhengxin on 2018/2/20.
 */

public class RecyclerViewUtil {
    public static <T> void setupRecyclerView(RecyclerRefreshLayout mRecyclerRefreshLayout, RecyclerView mRecyclerView, CommonRecyclerViewAdapter<T> adapter, CommonListRequestCallback<T> callback) {
        final Disposable[] refreshDisposable = {null};
        final Disposable[] loadMoreDisposable = {null};
        final BaseListData[] lastData = {null};
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                DevLib.getApp().getResources().getDimensionPixelSize(R.dimen.default_gap_50), DevLib.getApp().getResources().getDimensionPixelSize(R.dimen.default_gap_50));
        mRecyclerRefreshLayout.setRefreshView(new CommonRefreshView(mRecyclerRefreshLayout.getContext()),layoutParams);
        mRecyclerRefreshLayout.setDragDistanceConverter(new MaterialDragDistanceConverter());
//        mRecyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                mRecyclerView.setAdapter(adapter);
//            }
//        });
        mRecyclerView.setAdapter(adapter);
        if (mRecyclerRefreshLayout != null) {
            RecyclerRefreshLayout.OnRefreshListener listener = () -> {
                NetworkApi.ApiSubscribe(callback.getObserverble(null), new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        refreshDisposable[0] = d;
                        if (loadMoreDisposable[0] != null) {
                            loadMoreDisposable[0].dispose();
                            loadMoreDisposable[0] = null;
                        }
                    }

                    @Override
                    public void onNext(Object o) {
                        mRecyclerRefreshLayout.setRefreshing(false);
                        if (((UniApiData) o).hasError()) {
                            ToastUtil.showToast(mRecyclerView.getContext(), ((UniApiData) o).error.usermsg);
                            return;
                        }
                        IBaseListDataHolder<T> iBaseListDataHolder = (IBaseListDataHolder<T>) o;
                        if (iBaseListDataHolder.getListData() == null) {
                            return;
                        }
                        lastData[0] = iBaseListDataHolder.getListData();
                        adapter.setNewData(iBaseListDataHolder.getListData().getListItems());
                        adapter.notifyDataSetChanged();
                        dealLoadMore(adapter, iBaseListDataHolder.getListData());
                        if (loadMoreDisposable[0] != null) {
                            loadMoreDisposable[0].dispose();
                            loadMoreDisposable[0] = null;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        mRecyclerRefreshLayout.setRefreshing(false);
                        loadMoreDisposable[0] = null;
                    }

                    @Override
                    public void onComplete() {

                    }
                });

            };
            mRecyclerRefreshLayout.setOnRefreshListener(listener);
            mRecyclerRefreshLayout.setRefreshing(true);
            listener.onRefresh();
        }

        adapter.setOnLoadMoreListener(() -> {
            if (lastData[0] == null) {
                return;
            }
            NetworkApi.ApiSubscribe(ViewUtil.getLivecircle(mRecyclerView),callback.getObserverble(lastData[0]), new Observer<UniApiData>() {
                @Override
                public void onSubscribe(Disposable d) {
                    refreshDisposable[0] = d;
                    if (loadMoreDisposable[0] != null) {
                        loadMoreDisposable[0].dispose();
                        loadMoreDisposable[0] = null;
                    }
                }

                @Override
                public void onNext(UniApiData o) {
                    if (((UniApiData) o).hasError()) {
                        ToastUtil.showToast(mRecyclerView.getContext(), ((UniApiData) o).error.usermsg);
                        return;
                    }
                    IBaseListDataHolder<T> iBaseListDataHolder = (IBaseListDataHolder<T>) o;
                    if (iBaseListDataHolder.getListData() == null) {
                        return;
                    }
                    lastData[0] = iBaseListDataHolder.getListData();
                    adapter.addData(iBaseListDataHolder.getListData().getListItems());
                    mRecyclerRefreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    dealLoadMore(adapter, iBaseListDataHolder.getListData());
                    if (loadMoreDisposable[0] != null) {
                        loadMoreDisposable[0].dispose();
                        loadMoreDisposable[0] = null;
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }, mRecyclerView);


    }

    public static void dealLoadMore(CommonRecyclerViewAdapter adapter, BaseListData data) {
        adapter.loadMoreComplete();
        adapter.setEnableLoadMore(false);
        if (data == null) {
            return;
        }
        if (data.hasMore != 0) {
            adapter.setEnableLoadMore(true);
        }

    }





}
