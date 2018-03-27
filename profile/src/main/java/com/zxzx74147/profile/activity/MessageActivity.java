package com.zxzx74147.profile.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.interfaces.CommonListRequestCallback;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.umeng.UmengAction;
import com.zxzx74147.devlib.umeng.UmengAgent;
import com.zxzx74147.devlib.utils.RecyclerViewUtil;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.RecycleViewDivider;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.data.Message;
import com.zxzx74147.profile.data.MessageListData;
import com.zxzx74147.profile.databinding.ActivityMessageBinding;
import com.zxzx74147.profile.databinding.ActivityMessageCenterBinding;
import com.zxzx74147.profile.storage.MessageCenterStorage;

import java.util.LinkedList;

import io.reactivex.Observable;

public class MessageActivity extends BaseActivity {
    private static final String TAG = MessageActivity.class.getSimpleName();


    private ActivityMessageBinding mBinding = null;
    private CommonRecyclerViewAdapter<Message> mAdapter = null;
    private MessageCenterStorage mFeedStorage = RetrofitClient.getClient().create(MessageCenterStorage.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengAgent.onEvent(UmengAction.ALUmengPageMsgCenterDetail);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message);
        initView();
    }

    private void initView() {
        mBinding.setData((Message) mIntentData.data);

    }


}
