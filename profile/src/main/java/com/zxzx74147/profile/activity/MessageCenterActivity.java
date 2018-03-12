package com.zxzx74147.profile.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.interfaces.CommonListRequestCallback;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.RecyclerViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.RecycleViewDivider;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.data.Message;
import com.zxzx74147.profile.data.MessageListData;
import com.zxzx74147.profile.databinding.ActivityMessageCenterBinding;
import com.zxzx74147.profile.storage.MessageCenterStorage;

import java.io.Serializable;
import java.util.LinkedList;

import io.reactivex.Observable;

public class MessageCenterActivity extends BaseActivity {
    private static final String TAG = MessageCenterActivity.class.getSimpleName();


    private ActivityMessageCenterBinding mBinding = null;
    private CommonRecyclerViewAdapter<Message> mAdapter = null;
    private MessageCenterStorage mFeedStorage = RetrofitClient.getClient().create(MessageCenterStorage.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message_center);
        initView();
    }

    private void initView() {
        mBinding.list.addItemDecoration(new RecycleViewDivider(this, LinearLayout.HORIZONTAL,
                getResources().getDimensionPixelOffset(R.dimen.default_gap_24), getResources().getColor(R.color.div_default)));
        mAdapter = new CommonRecyclerViewAdapter<Message>(new LinkedList<>());
        mBinding.list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setMultiTypeDelegate(new CommonMultiTypeDelegate());
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ZXActivityJumpHelper.startActivity(MessageCenterActivity.this,MessageActivity.class,new IntentData((Serializable) adapter.getItem(position)));
            }
        });


        RecyclerViewUtil.setupRecyclerView(mBinding.refreshLayout, mBinding.list, mAdapter, new CommonListRequestCallback<Message>() {
            @Override
            public Observable<MessageListData> getObserverble(BaseListData listdata) {
                if (listdata == null) {
                    return mFeedStorage.msgcenterGetList(0);
                } else {
                    return mFeedStorage.msgcenterGetList(listdata.nextPage);
                }
            }
        });

    }


}
