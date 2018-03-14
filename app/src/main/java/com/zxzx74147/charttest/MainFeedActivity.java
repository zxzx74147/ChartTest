package com.zxzx74147.charttest;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.balance.activity.WithdrawActivity;
import com.zxzx74147.charttest.databinding.ActivityMainBinding;
import com.zxzx74147.charttest.databinding.ActivityMainFeedBinding;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.fragment.CommonFragmentDialog;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.devlib.widget.CommonLoading;
import com.zxzx74147.devlib.widget.CommonProgressDialog;
import com.zxzx74147.live.fragments.FeedFragment;
import com.zxzx74147.live.fragments.LiveListFragment;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.fragment.StockFragment;

public class MainFeedActivity extends BaseActivity {

    private ActivityMainFeedBinding mBinding = null;
    private UserViewModel mUserViewModel = null;


    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainFeedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_feed);
        mBinding.goodList.setCallback(mCallback);
        mBinding.goodList.setProvider(ViewModelProviders.of(this));
        mBinding.goodList.setLifeCircle(this);
        mBinding.setUser(AccountManager.sharedInstance().getUser());
        ViewUtil.startAnimition(mBinding.toLive);
        initView();
        initData();
    }

    private CommonCallback<GoodType> mCallback = item -> {
        StockFragment fragment = StockFragment.newInstance(item);
        fragment.show(getSupportFragmentManager(),fragment.getTag());

//        ZXFragmentJumpHelper.startFragment(this, ProfileFragment.class, null);
    };

    private void initView() {

//        mHeaderBinding= mBinding.headLayout;
//        mHeaderBinding =(LayoutLiveHeadBinding)mBinding.headLayout;
//        RxView.clicks(mBinding.headLayout.assetTotal).subscribe(o -> ZXFragmentJumpHelper.startFragment(MainActivity.this, ProfileFragment.class,null));

        RxView.clicks(mBinding.sendFeed).subscribe(o->{
            DialogItem item = new DialogItem();
            item.title = getResources().getString(R.string.send_remind);
            item.cancel = null;
            item.ok = getResources().getString(com.zxzx74147.balance.R.string.i_know);
            CommonFragmentDialog dialog = CommonFragmentDialog.newInstance(new IntentData<>(item));
            ZXFragmentJumpHelper.startFragment(MainFeedActivity.this, dialog, new CommonCallback() {
                @Override
                public void callback(Object item) {

                    return;
                }
            });
        });



    }

    private void initData() {
        mUserViewModel = ViewModelProviders.of(MainFeedActivity.this).get(UserViewModel.class);
        mUserViewModel.getUserUniLiveData().observe(MainFeedActivity.this, userUniData -> {
            if(userUniData.hasError()){
                return ;
            }
            mBinding.setUser(userUniData.user);
        });
    }




}