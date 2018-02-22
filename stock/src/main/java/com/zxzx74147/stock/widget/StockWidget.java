package com.zxzx74147.stock.widget;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.data.CandleData;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.databinding.WidgetStockBinding;
import com.zxzx74147.stock.viewmodel.StockViewModel;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class StockWidget extends FrameLayout {

    private WidgetStockBinding mBinding = null;
    private StockViewModel mModel = null;
    private CandleData mCandleData = null;

    public StockWidget(Context context) {
        this(context, null);
    }

    public StockWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StockWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setGood(GoodType good){
    }

    private void init() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.widget_stock, this, true);

        mModel = ViewModelProviders.of(ViewUtil.getFragmentActivity(StockWidget.this)).get(StockViewModel.class);

//        mModel.getKLineData().observe(((FragmentActivity) getContext()), stockData->{
//            if(stockData.hasError()){
//                return;
//            }
//            List<CandleEntry> list = DataConvUtil.convKLine(stockData.data);
//            CandleDataSet set1 = new CandleDataSet(list, "Data Set");
//            mCandleData = new CandleData(set1);
//            mBinding.candle.setData(mCandleData);
//        });

    }


}
