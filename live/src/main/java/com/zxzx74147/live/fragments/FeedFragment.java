package com.zxzx74147.live.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zxzx74147.devlib.BR;
import com.zxzx74147.devlib.base.BaseBindingViewHolder;
import com.zxzx74147.devlib.base.BaseFragment;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.interfaces.CommonListRequestCallback;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.RecyclerViewUtil;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.RecycleViewDivider;
import com.zxzx74147.live.R;
import com.zxzx74147.live.data.LikeData;
import com.zxzx74147.live.data.Text;
import com.zxzx74147.live.data.TextListData;
import com.zxzx74147.live.databinding.FragmentFeedBinding;
import com.zxzx74147.live.databinding.ItemTextBinding;
import com.zxzx74147.live.stroage.FeedStorage;

import java.util.LinkedList;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * A fragment representing a list of Items.
 */
public class FeedFragment extends BaseFragment {


    private FragmentFeedBinding mBinding = null;
    private CommonRecyclerViewAdapter<Text> mAdapter = null;
    private FeedStorage mFeedStorage = RetrofitClient.getClient().create(FeedStorage.class);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.list.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayout.VERTICAL,
                getResources().getDimensionPixelOffset(R.dimen.default_gap_16), getResources().getColor(R.color.div_default)));
        mAdapter = new CommonRecyclerViewAdapter<Text>(new LinkedList<>()){
            @Override
            protected void convert(BaseBindingViewHolder helper, Text item) {
                super.convert(helper, item);
                ItemTextBinding binding = (ItemTextBinding) helper.mBinding;
                binding.setLikeCallback(new CommonCallback<Text>() {
                    @Override
                    public void callback(Text item) {
                        likeText(item);
                    }
                });

            }
        };
        mBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setMultiTypeDelegate(new CommonMultiTypeDelegate());

        RecyclerViewUtil.setupRecyclerView(mBinding.refreshLayout, mBinding.list, mAdapter, new CommonListRequestCallback<Text>() {
            @Override
            public Observable<TextListData> getObserverble(BaseListData listdata) {
                if (listdata == null) {
                    return mFeedStorage.feedGetList(0);
                } else {
                    return mFeedStorage.feedGetList(listdata.nextPage);
                }
            }
        });


    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {

        }
    }

    private void likeText(Text text){
        Observable<LikeData> observable= null;
        if(text.isLove==0){
            observable = mFeedStorage.feedLove(text.textId);
        }else{
            observable = mFeedStorage.feedHate(text.textId);
        }
        NetworkApi.ApiSubscribe(observable, new Consumer<LikeData>() {
            @Override
            public void accept(LikeData likeData) throws Exception {
                if(likeData.hasError()){
                    return;
                }
                text.loveNum = likeData.loveNum;
                text.isLove = text.isLove==0? 1:0;
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {


    }

}
