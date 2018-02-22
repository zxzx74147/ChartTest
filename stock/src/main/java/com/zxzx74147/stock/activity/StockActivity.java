package com.zxzx74147.stock.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.databinding.ActivityStockBinding;

public class StockActivity extends BaseActivity {

    private ActivityStockBinding mBinding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_stock);
//        IntentData<GoodItem> item = ZXActivityJumpHelper.getIntentData(getIntent());
//        mBinding.stockView.setGood(item.data);

    }
}
