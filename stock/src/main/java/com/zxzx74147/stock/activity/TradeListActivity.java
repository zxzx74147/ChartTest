package com.zxzx74147.stock.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.BR;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.base.BaseBindingViewHolder;
import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.interfaces.CommonListRequestCallback;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.RecyclerViewUtil;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.Position;
import com.zxzx74147.stock.data.PositionListData;
import com.zxzx74147.stock.databinding.ActivityTradeBinding;
import com.zxzx74147.stock.databinding.ItemTradeBinding;
import com.zxzx74147.stock.storage.TradesStorage;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;

public class TradeListActivity extends BaseActivity {

    private ActivityTradeBinding mBinding = null;
    private TradesStorage mTradeStorage = null;

    private List<Position> mData = new LinkedList<>();
    private CommonRecyclerViewAdapter<Position> mAdapter = null;
//    private PositionList mLatestPositon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_trade);

        mTradeStorage = RetrofitClient.getClient().create(TradesStorage.class);


        CommonMultiTypeDelegate delegate = new CommonMultiTypeDelegate();
        delegate.registViewType(Position.class, R.layout.item_trade);
//        CommonNoItemBinding mNoDatabinding = DataBindingUtil.inflate()

        mAdapter = new CommonRecyclerViewAdapter<Position>(mData){
            @Override
            protected void convert(BaseBindingViewHolder helper, Position item) {
                ItemTradeBinding itemCommonBinding = (ItemTradeBinding) helper.mBinding;
                itemCommonBinding.setVariable(BR.data, item);
                RxView.clicks(itemCommonBinding.head).subscribe(o -> {
                    if(itemCommonBinding.expand.getVisibility()==View.VISIBLE){
                        itemCommonBinding.expand.setVisibility(View.GONE);
                        itemCommonBinding.imageButton.setImageResource(R.drawable.icon_expand);
                    }else if(itemCommonBinding.expand.getVisibility()==View.GONE){
                        itemCommonBinding.expand.setVisibility(View.VISIBLE);
                        itemCommonBinding.imageButton.setImageResource(R.drawable.icon_putaway);
                    }

                });
            }
        };
        mAdapter.setMultiTypeDelegate(delegate);
        mBinding.list.setLayoutManager(new LinearLayoutManager(this));
        mBinding.list.setAdapter(mAdapter);
        initView();
    }

    private void initView(){
        RecyclerViewUtil.setupRecyclerView(mBinding.refreshLayout, mBinding.list, mAdapter, new CommonListRequestCallback<Position>() {
            @Override
            public Observable<PositionListData> getObserverble(BaseListData listdata) {
                if(listdata==null){
                    return mTradeStorage.positionGetHisList(0);
                }else
                {
                    return mTradeStorage.positionGetHisList(listdata.nextPage);
                }
            }
        });
//        mBinding.refreshLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refresh();
//            }
//        });
//
//        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                loadMore();
//            }
//        },mBinding.list);
//        mAdapter.isUpFetchEnable();

    }

//    private void refresh() {
//        NetworkApi.ApiSubscribe(mTradeStorage.positionGetHisList(0), positionListData -> {
//            mBinding.refreshLayout.setRefreshing(false);
//            if (positionListData.hasError()) {
//                ToastUtil.showToast(TradeListActivity.this,positionListData.error.usermsg);
//                return;
//            }
////            mAdapter.setEmptyView(R.string.no_trade);
//            if (positionListData.positionList == null) {
//                return;
//            }
//            mData.clear();
//            mData.addAll(positionListData.positionList.position);
//            mAdapter.notifyDataSetChanged();
//
//            RecyclerViewUtil.dealLoadMore(mAdapter,positionListData.positionList);
//        });
//    }
//
//    private void loadMore() {
//        if(mLatestPositon==null){
//            return;
//        }
//        NetworkApi.ApiSubscribe(mTradeStorage.positionGetHisList(mLatestPositon.nextPage), positionListData -> {
//            mBinding.refreshLayout.setRefreshing(false);
//            if (positionListData.hasError()) {
//                ToastUtil.showToast(TradeListActivity.this,positionListData.error.usermsg);
//                return;
//            }
////            mAdapter.setEmptyView(R.string.no_trade);
//            if (positionListData.positionList == null) {
//                return;
//            }
//            mData.addAll(positionListData.positionList.position);
//            mAdapter.notifyDataSetChanged();
//            RecyclerViewUtil.dealLoadMore(mAdapter,positionListData.positionList);
//
//        });
//    }


}
