package com.zxzx74147.stock.widget;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.data.CandleData;
import com.zxzx74147.devlib.json.JsonHelper;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.databinding.WidgetStockBinding;
import com.zxzx74147.stock.indicator.DataParse;
import com.zxzx74147.stock.viewmodel.StockViewModel;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class StockWidget extends FrameLayout {
    private static final String TAG = StockWidget.class.getSimpleName();

    private WidgetStockBinding mBinding = null;
    private StockViewModel mModel = null;
    private CandleData mCandleData = null;
    private DataParse mDataParse = new DataParse();

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
        mModel.getKLineData().setGood(good);
        mModel.getKLineData().setKType(1);
    }



    private void init() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.widget_stock, this, true);

        mModel = ViewModelProviders.of(ViewUtil.getFragmentActivity(StockWidget.this)).get(StockViewModel.class);

        mModel.getKLineData().observe(((FragmentActivity) getContext()), stockData->{
            if(stockData.hasError()){
                return;
            }
            Log.i(TAG, JsonHelper.toJson(stockData));
            mDataParse.clear();
            mDataParse.parseKLine(stockData.PriceKChartList.priceKChart);
            mBinding.klineview.setData(mDataParse);

        });

    }


}
