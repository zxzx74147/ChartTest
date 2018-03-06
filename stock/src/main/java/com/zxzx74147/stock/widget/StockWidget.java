package com.zxzx74147.stock.widget;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding2.support.design.widget.RxTabLayout;
import com.zxzx74147.devlib.interfaces.IViewModelHolder;
import com.zxzx74147.devlib.json.JsonHelper;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.data.Price;
import com.zxzx74147.stock.databinding.WidgetStockBinding;
import com.zxzx74147.stock.indicator.DataParse;
import com.zxzx74147.stock.viewmodel.PriceViewModel;
import com.zxzx74147.stock.viewmodel.StockViewModel;


/**
 * Created by zhengxin on 2018/2/8.
 */

public class StockWidget extends FrameLayout implements IViewModelHolder {
    private static final String TAG = StockWidget.class.getSimpleName();

    private WidgetStockBinding mBinding = null;
    private StockViewModel mStockViewModel = null;
    private PriceViewModel mPriceViewModel = null;
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
        mStockViewModel.getKLineData().setGood(good);
        mPriceViewModel.getReadTimeLiveData().setGood(good);
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
        RxTabLayout.selections(mBinding.tabLayout).subscribe(tab -> {
            if (mStockViewModel != null) {
                mStockViewModel.getKLineData().setKType(String.valueOf(tab.getPosition()));
            }
        });
    }


    @Override
    public void setProvider(ViewModelProvider provider) {
        mStockViewModel = provider.get(StockViewModel.class);
        mPriceViewModel = provider.get(PriceViewModel.class);
    }

    @Override
    public void setLifeCircle(LifecycleOwner owner) {
        mStockViewModel.getKLineData().observe(owner, stockData -> {
            if (stockData.hasError()) {
                return;
            }
//            Log.i(TAG, JsonHelper.toJson(stockData));
            mDataParse.clear();
            mDataParse.parseKLine(stockData.PriceKChartList.priceKChart);
            mBinding.klineview.setData(mDataParse);

        });

        mPriceViewModel.getReadTimeLiveData().observe(owner, realtime -> {
            if (realtime.hasError()) {
                return;
            }
            Price price = realtime.priceRealtimeList.priceRealtime.get(0);
            mBinding.setPrice(price);
        });
    }
}
