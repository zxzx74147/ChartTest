package com.zxzx74147.stock.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.databinding.WidgetKlineBinding;
import com.zxzx74147.stock.indicator.DataParse;
import com.zxzx74147.stock.utils.ChartUtil;

import java.util.ArrayList;

/**
 * Created by zhengxin on 2018/2/13.
 */

public class RealTimeView extends LinearLayout {

    WidgetKlineBinding mBinding = null;

    private DataParse mDataParse;


    public RealTimeView(Context context) {
        super(context);
        init();
    }

    public RealTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RealTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.widget_kline, this, true);
        ChartUtil.setChart(mBinding.kline);
        ChartUtil.setChart(mBinding.kline2);
        initChartListener();
    }

    public void setData(DataParse dataParse) {
        mDataParse = dataParse;
        {
            CombinedData combinedData = new CombinedData();
            CandleDataSet set = new CandleDataSet(dataParse.getCandleEntries(), "");
            ChartUtil.setCandDataleSet(set);
            CandleData candleData = new CandleData(set);
            combinedData.setData(candleData);

            XAxis xAxis = mBinding.kline.getXAxis();

            xAxis.setValueFormatter(ChartUtil.getAxisValueFormatter1m(dataParse.getCandleEntries()));


            ArrayList<ILineDataSet> sets = new ArrayList<>();
            LineDataSet lineMA5 = new LineDataSet(dataParse.getMa5DataL(), "ma5");
            LineDataSet lineMA10 = new LineDataSet(dataParse.getMa10DataL(), "ma10");
            LineDataSet lineMA20 = new LineDataSet(dataParse.getMa20DataL(), "ma20");
            ChartUtil.setLineDataleSet(lineMA5, getResources().getColor(R.color.stock_kline_ma5));
            ChartUtil.setLineDataleSet(lineMA10, getResources().getColor(R.color.stock_kline_ma10));
            ChartUtil.setLineDataleSet(lineMA20, getResources().getColor(R.color.stock_kline_ma20));
            sets.add(lineMA5);
            sets.add(lineMA10);
            sets.add(lineMA20);

            LineData lineData = new LineData(sets);
            combinedData.setData(lineData);
            mBinding.kline.setData(combinedData);
            mBinding.kline.invalidate();


        }
        {
            CombinedData combinedData = new CombinedData();

            XAxis xAxis = mBinding.kline2.getXAxis();
            xAxis.setDrawLabels(false);
            xAxis.setValueFormatter(ChartUtil.getAxisValueFormatter1m(dataParse.getCandleEntries()));

            BarDataSet set = new BarDataSet(dataParse.getMacdData(), "");
            ChartUtil.setBarDataSet(set);
            BarData barData = new BarData(set);
            combinedData.setData(barData);


            ArrayList<ILineDataSet> sets = new ArrayList<>();
            LineDataSet lineDea = new LineDataSet(dataParse.getDeaData(), "dea");
            LineDataSet lineDif = new LineDataSet(dataParse.getDeaData(), "dif");
            ChartUtil.setLineDataleSet(lineDea, getResources().getColor(R.color.stock_kline_dea));
            ChartUtil.setLineDataleSet(lineDif, getResources().getColor(R.color.stock_kline_dif));
            sets.add(lineDea);
            sets.add(lineDif);
            LineData lineData = new LineData(sets);
            combinedData.setData(lineData);
            mBinding.kline2.setData(combinedData);
            mBinding.kline2.invalidate();
        }
    }


    private void initChartListener() {
        mBinding.kline.setOnChartGestureListener(new CoupleChartGestureListener(mBinding.kline, mBinding.kline2));
        mBinding.kline2.setOnChartGestureListener(new CoupleChartGestureListener(mBinding.kline, mBinding.kline2));
//        mBinding.kline.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartVolume));
//        mBinding.kline2.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartPrice));
//        mChartPrice.setOnTouchListener(new ChartInfoViewHandler(mChartPrice));
//        mChartVolume.setOnTouchListener(new ChartInfoViewHandler(mChartVolume));
    }

    private void setMacd() {

    }
}
