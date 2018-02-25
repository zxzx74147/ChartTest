package com.zxzx74147.stock.widget;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.github.mikephil.charting.data.CandleData;
import com.jakewharton.rxbinding2.support.design.widget.RxTabLayout;
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

    public void setGood(GoodType good) {
        mModel.getKLineData().setGood(good);
        mModel.getKLineData().setKType(getResources().getString(R.string.stock_k_1m_s));
        try {
            RxTabLayout.select(mBinding.tabLayout).accept(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initView() {

    }

    private void init() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.widget_stock, this, true);
        initView();
        mModel = ViewModelProviders.of(ViewUtil.getFragmentActivity(StockWidget.this)).get(StockViewModel.class);

//        RxTabLayout.selectionEvents(mBinding.tabLayout).subscribe(tab->{
//            String item = tab.tab().getTag().toString();
//            mModel.getKLineData().setKType(item);
//        });
        RxTabLayout.selections(mBinding.tabLayout).subscribe(tab -> {
            mModel.getKLineData().setKType(String.valueOf(tab.getPosition()));
        });

        mModel.getKLineData().observe(ViewUtil.getFragmentActivity(StockWidget.this), stockData -> {
            if (stockData.hasError()) {
                return;
            }
            Log.i(TAG, JsonHelper.toJson(stockData));
            mDataParse.clear();
            mDataParse.parseKLine(stockData.PriceKChartList.priceKChart);
            mBinding.klineview.setData(mDataParse);

        });

    }


}
