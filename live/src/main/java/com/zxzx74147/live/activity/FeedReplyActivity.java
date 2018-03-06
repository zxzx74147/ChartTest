package com.zxzx74147.live.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.live.R;
import com.zxzx74147.live.data.Text;
import com.zxzx74147.live.databinding.FragmentReplyBinding;
import com.zxzx74147.live.fragments.FeedReplyFragment;

public class FeedReplyActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_reply);
        FeedReplyFragment fragment = (FeedReplyFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        fragment.setData((Text) mIntentData.data);
    }
}
