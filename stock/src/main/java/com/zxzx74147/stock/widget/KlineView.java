package com.zxzx74147.stock.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.DashPathEffect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;
import com.jakewharton.rxbinding2.widget.RxRadioGroup;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.databinding.WidgetKlineBinding;
import com.zxzx74147.stock.indicator.DataParse;
import com.zxzx74147.stock.utils.ChartUtil;

import java.util.ArrayList;

/**
 * Created by zhengxin on 2018/2/13.
 */

public class KlineView extends LinearLayout {
    private static final String TAG = KlineView.class.getSimpleName();

    WidgetKlineBinding mBinding = null;

    private DataParse mDataParse;


    public KlineView(Context context) {
        super(context);
        init();
    }

    public KlineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KlineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.widget_kline, this, true);
        ChartUtil.setChart(mBinding.kline);
        ChartUtil.setRealTimeChart(mBinding.klineRealtime);
        ChartUtil.setChart(mBinding.kline2);
        ChartUtil.showHighline(mBinding.kline, mBinding.kline2);
        ChartUtil.setMarkerView(mBinding.kline);
        ChartUtil.setMarkerView2(mBinding.kline2);

//        ChartUtil.showHighline();
        mBinding.kline.setExtraBottomOffset(3);
        mBinding.kline.setExtraTopOffset(5);
        mBinding.klineRealtime.setExtraBottomOffset(3);
        mBinding.klineRealtime.setExtraTopOffset(5);
        mBinding.kline2.setExtraBottomOffset(3);
        mBinding.kline2.setExtraTopOffset(5);

        XAxis xAxis = mBinding.kline2.getXAxis();
        xAxis.setDrawLabels(false);
        YAxis axisRKline = mBinding.kline2.getAxisRight();
        axisRKline.setLabelCount(3, true);

        initChartListener();

        RxRadioGroup.checkedChanges(mBinding.klineSelect).subscribe((Integer checkId) -> {
            refresh();
        });

        RxRadioGroup.checkedChanges(mBinding.indicatorSelect).subscribe((Integer checkId) -> {
            refresh();
        });
    }

    public void setData(DataParse dataParse) {
        boolean needRefresh = false;
        if (mDataParse == null || mDataParse.mType != dataParse.mType) {
            needRefresh = true;
        }
        mDataParse = dataParse;
        refresh();
        mBinding.klineRealtime.setVisibility(View.GONE);
        mBinding.klineOther.setVisibility(VISIBLE);
        if (needRefresh) {
            ChartUtil.fitData(mBinding.kline);
            ChartUtil.fitData(mBinding.kline2);
        }
    }

    public void setRealTime(DataParse dataParse) {
        boolean needRefresh = false;
        if (mDataParse == null || mDataParse.mType != dataParse.mType) {
            needRefresh = true;
        }
        mDataParse = dataParse;
        refreshReadTime();

        mBinding.klineRealtime.setVisibility(View.VISIBLE);
        mBinding.klineOther.setVisibility(INVISIBLE);
        if (needRefresh) {
            mBinding.klineRealtime.highlightValue(null);
        }
    }

    private void refreshReadTime() {
        setReadTime();
    }


    private void refresh() {
        if (mDataParse == null) {
            return;
        }
        {
            int checkId = mBinding.indicatorSelect.getCheckedRadioButtonId();
            if (checkId == R.id.btn_macd) {
                setMacd();
            } else if (checkId == R.id.btn_kdj) {
                setKDJ();
            } else if (checkId == R.id.btn_rsi) {
                setRSI();
            }
        }
        {
            int checkId = mBinding.klineSelect.getCheckedRadioButtonId();
            if (checkId == R.id.btn_sma) {
                setSMA();
            } else if (checkId == R.id.btn_ema) {
                setEMA();
            } else if (checkId == R.id.btn_boll) {
                setBOLL();
            }
        }
    }


    private void initChartListener() {
        mBinding.kline.setOnChartGestureListener(new CoupleChartGestureListener(mBinding.kline, mBinding.kline2));
        mBinding.kline2.setOnChartGestureListener(new CoupleChartGestureListener(mBinding.kline2, mBinding.kline));
//        mBinding.kline.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartVolume));
//        mBinding.kline2.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartPrice));
//        mChartPrice.setOnTouchListener(new ChartInfoViewHandler(mChartPrice));
//        mChartVolume.setOnTouchListener(new ChartInfoViewHandler(mChartVolume));
    }

    private void setKData(CombinedData combinedData) {
        CandleDataSet set = new CandleDataSet(mDataParse.getCandleEntries(), "");
        ChartUtil.setCandDataleSet(set);
        CandleData candleData = new CandleData(set);
        combinedData.setData(candleData);

        XAxis xAxis = mBinding.kline.getXAxis();
        xAxis.setValueFormatter(ChartUtil.getAxisValueFormatter1m(mDataParse.getCandleEntries()));
    }

    private void setSMA() {
        CombinedData combinedData = new CombinedData();
        setKData(combinedData);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        LineDataSet lineMA5 = new LineDataSet(mDataParse.getMa5DataL(), "MA5");
        LineDataSet lineMA10 = new LineDataSet(mDataParse.getMa10DataL(), "MA10");
        LineDataSet lineMA20 = new LineDataSet(mDataParse.getMa20DataL(), "MA20");
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

    private void setEMA() {
        CombinedData combinedData = new CombinedData();
        setKData(combinedData);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        LineDataSet lineMA5 = new LineDataSet(mDataParse.getExpmaData5(), "EMA5");
        LineDataSet lineMA10 = new LineDataSet(mDataParse.getExpmaData10(), "EMA10");
        LineDataSet lineMA20 = new LineDataSet(mDataParse.getExpmaData20(), "EMA20");
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

    private void setBOLL() {
        CombinedData combinedData = new CombinedData();
        setKData(combinedData);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        LineDataSet bollD = new LineDataSet(mDataParse.getBollDataDN(), "LOWER");
        LineDataSet bollU = new LineDataSet(mDataParse.getBollDataUP(), "UPPER");
        LineDataSet bollM = new LineDataSet(mDataParse.getBollDataMB(), "MID");
        ChartUtil.setLineDataleSet(bollD, getResources().getColor(R.color.stock_kline_ma5));
        ChartUtil.setLineDataleSet(bollU, getResources().getColor(R.color.stock_kline_ma10));
        ChartUtil.setLineDataleSet(bollM, getResources().getColor(R.color.stock_kline_ma20));
        sets.add(bollD);
        sets.add(bollU);
        sets.add(bollM);

        LineData lineData = new LineData(sets);
        combinedData.setData(lineData);
        mBinding.kline.setData(combinedData);
        mBinding.kline.invalidate();
    }

    private void setMacd() {
        CombinedData combinedData = new CombinedData();

        BarDataSet set = new BarDataSet(mDataParse.getMacdData(), "");
        ChartUtil.setBarDataSet(set);
        BarData barData = new BarData(set);
        combinedData.setData(barData);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        LineDataSet lineDea = new LineDataSet(mDataParse.getDeaData(), "DEA");
        LineDataSet lineDif = new LineDataSet(mDataParse.getDeaData(), "DIF");
        ChartUtil.setLineDataleSet(lineDea, getResources().getColor(R.color.stock_kline_dea), true);
        ChartUtil.setLineDataleSet(lineDif, getResources().getColor(R.color.stock_kline_dif), true);
        sets.add(lineDea);
        sets.add(lineDif);
        LineData lineData = new LineData(sets);
        combinedData.setData(lineData);
        mBinding.kline2.setData(combinedData);
        mBinding.kline2.invalidate();
    }

    private void setKDJ() {
        CombinedData combinedData = new CombinedData();


        ArrayList<ILineDataSet> sets = new ArrayList<>();
        LineDataSet lineK = new LineDataSet(mDataParse.getkData(), "K");
        LineDataSet lineD = new LineDataSet(mDataParse.getdData(), "D");
        LineDataSet lineJ = new LineDataSet(mDataParse.getjData(), "J");
        ChartUtil.setLineDataleSet(lineK, getResources().getColor(R.color.stock_kline_dea), true);
        ChartUtil.setLineDataleSet(lineD, getResources().getColor(R.color.stock_kline_ma5), true);
        ChartUtil.setLineDataleSet(lineJ, getResources().getColor(R.color.stock_kline_dif), true);
        sets.add(lineK);
        sets.add(lineD);
        sets.add(lineJ);
        LineData lineData = new LineData(sets);
        combinedData.setData(lineData);
        ((CombinedChartRenderer) mBinding.kline2.getRenderer()).getSubRenderers().clear();
        mBinding.kline2.setData(combinedData);
        mBinding.kline2.invalidate();
    }

    private void setRSI() {
        CombinedData combinedData = new CombinedData();

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        LineDataSet lineK = new LineDataSet(mDataParse.getRsiData6(), "RSI6");
        LineDataSet lineD = new LineDataSet(mDataParse.getRsiData12(), "RSI12");
        LineDataSet lineJ = new LineDataSet(mDataParse.getRsiData24(), "RSI21");
        ChartUtil.setLineDataleSet(lineK, getResources().getColor(R.color.stock_kline_dea), true);
        ChartUtil.setLineDataleSet(lineD, getResources().getColor(R.color.stock_kline_ma5), true);
        ChartUtil.setLineDataleSet(lineJ, getResources().getColor(R.color.stock_kline_dif), true);
        sets.add(lineK);
        sets.add(lineD);
        sets.add(lineJ);
        LineData lineData = new LineData(sets);
        combinedData.setData(lineData);
        ((CombinedChartRenderer) mBinding.kline2.getRenderer()).getSubRenderers().clear();
        mBinding.kline2.setData(combinedData);
        mBinding.kline2.invalidate();

    }


    private void setReadTime() {
        CombinedData combinedData = new CombinedData();
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        LineDataSet lineK = new LineDataSet(mDataParse.getReadTime(), "");
        ChartUtil.setupRealTimeY(mBinding.klineRealtime, mDataParse.getReadTime());
        ChartUtil.setLineDataleSet(lineK, getResources().getColor(R.color.stock_kline_realtime));
        sets.add(lineK);
        lineK.setDrawFilled(true);
        lineK.setFillDrawable(getResources().getDrawable(R.drawable.fade_black));
        lineK.setFormLineWidth(1f);
        lineK.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        lineK.setFormSize(15.f);
        lineK.setHighlightEnabled(true);
        lineK.setDrawHighlightIndicators(true);
        lineK.setHighlightLineWidth(1f);
        LineData lineData = new LineData(sets);
        combinedData.setData(lineData);
        ((CombinedChartRenderer) mBinding.klineRealtime.getRenderer()).getSubRenderers().clear();
        mBinding.klineRealtime.setData(combinedData);
        mBinding.klineRealtime.invalidate();
    }
}
