package com.zxzx74147.stock.utils;

import android.graphics.Paint;
import android.util.Log;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.mychart.MyBottomMarkerView;
import com.github.mikephil.charting.mychart.MyCombinedChart;
import com.github.mikephil.charting.mychart.MyLeftMarkerView;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.utils.DisplayUtil;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.indicator.KLineBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/8.
 */

public class ChartUtil {

    private static SimpleDateFormat TIME_PARSER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static SimpleDateFormat TIME_HHMM = new SimpleDateFormat("HH:mm");

    public static <T extends Entry> IAxisValueFormatter getAxisValueFormatter1m(List<T> data) {
        IAxisValueFormatter formatter = (value, axis) -> {
            int index = (int) value;
            if (index < data.size()) {
                KLineBean entry = (KLineBean) data.get(index).getData();
                Date temp = null;
                try {
                    temp = (TIME_PARSER.parse(entry.date));
                    return TIME_HHMM.format(temp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return "";

        };
        return formatter;
    }

    public static void setupRealTimeY(CombinedChart chart, List<Entry> data) {
        float start = data.get(0).getY();
        float min = start;
        float max = start;
        for (Entry item : data) {
            float value = item.getY();
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        float diff = Math.max(Math.abs(min - start), Math.abs(max - start));
        IAxisValueFormatter xformatter = (value, axis) -> {
            int index = (int) value;
            if (index < data.size()) {
                KLineBean entry = (KLineBean) data.get(index).getData();
                Date temp = null;
                try {
                    temp = (TIME_PARSER.parse(entry.date));
                    return TIME_HHMM.format(temp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                KLineBean entry = (KLineBean) data.get(0).getData();
                try {
                    Date temp = (TIME_PARSER.parse(entry.date));
                    temp.setTime(temp.getTime() + index * 60 * 1000);
                    return TIME_HHMM.format(temp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return "";
        };

        IAxisValueFormatter yrformatter = (value, axis) -> {
            return String.format("%+.2f%%", 100 * (value - start) / start);
//            int index = (int) value;
//            if (index < data.size()) {
//                KLineBean entry = (KLineBean) data.get(index).getData();
//                return String.format("%+.2f%",100*(entry.close-start)/start);
//            }
//            return "";
        };

        YAxis axisRKline = chart.getAxisRight();

        axisRKline.setAxisMaximum(start + diff * 1.1f);
        axisRKline.setAxisMinimum(start - diff * 1.1f);

        axisRKline.setDrawGridLines(true);
        axisRKline.setDrawAxisLine(false);
        axisRKline.setDrawZeroLine(false);
        axisRKline.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisRKline.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRKline.setDrawLabels(true);
        axisRKline.setGridColor(DevLib.getApp().getResources().getColor(R.color.stock_grid));
        axisRKline.setGridLineWidth(1f);
        axisRKline.setTextColor(DevLib.getApp().getResources().getColor(R.color.text_light_grey));
        axisRKline.setLabelCount(5, true); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        axisRKline.setSpaceTop(0);//距离顶部留白
        axisRKline.setValueFormatter(yrformatter);


        LimitLine ll = new LimitLine(0);
        ll.setLineWidth(1f);
        ll.setLineColor(DevLib.getApp().getResources().getColor(R.color.text_black));
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLineWidth(1);
        axisRKline.addLimitLine(ll);
        axisRKline.removeAllLimitLines();


        YAxis axisLKline = chart.getAxisLeft();

        axisLKline.setDrawGridLines(false);
        axisLKline.setDrawAxisLine(false);
        axisLKline.setDrawZeroLine(false);
        axisLKline.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLKline.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLKline.setDrawLabels(true);
        axisLKline.setTextColor(DevLib.getApp().getResources().getColor(R.color.text_light_grey));
        axisLKline.setLabelCount(5, true); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        axisLKline.setSpaceTop(0);//距离顶部留白


        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(xformatter);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(1260);
        xAxis.setYOffset(0);
    }

    public static <T extends Entry> IAxisValueFormatter getYValuePercentFormatter(List<T> data) {

        IAxisValueFormatter formatter = (value, axis) -> {
            int index = (int) value;
            if (index < data.size()) {
                KLineBean entry = (KLineBean) data.get(index).getData();
                Date temp = null;
                try {
                    temp = (TIME_PARSER.parse(entry.date));
                    return TIME_HHMM.format(temp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return "";

        };
        return formatter;
    }

    public static long parserTime(String date) {
        try {
            Date f = TIME_PARSER.parse(date);
            return f.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setCandDataleSet(CandleDataSet set) {
        set.setHighlightEnabled(true);
        set.setDrawHorizontalHighlightIndicator(true);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setShadowWidth(1f);
        set.setDrawIcons(false);
        set.setDecreasingColor(DevLib.getApp().getResources().getColor(R.color.stock_down));//设置开盘价高于收盘价的颜色
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(DevLib.getApp().getResources().getColor(R.color.stock_up));//设置开盘价地狱收盘价的颜色
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setNeutralColor(DevLib.getApp().getResources().getColor(R.color.stock_grey));//设置开盘价等于收盘价的颜色
        set.setShadowColorSameAsCandle(true);
        set.setHighlightLineWidth(1f);
        set.setHighLightColor(DevLib.getApp().getResources().getColor(R.color.text_black));
        set.setDrawValues(false);
        set.setValueTextColor(DevLib.getApp().getResources().getColor(R.color.text_black));

    }

    public static void setLineDataleSet(LineDataSet set, int color) {
        setLineDataleSet(set, color,false);
    }

    public static void setLineDataleSet(LineDataSet set, int color,boolean highlight) {
        set.setHighlightEnabled(highlight);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setHighLightColor(DevLib.getApp().getResources().getColor(R.color.text_black));
        set.setDrawValues(false);
        set.setLineWidth(1f);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setDrawIcons(false);
        set.setHighlightLineWidth(1f);
        set.setColor(color);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);


//        set.setHighlightEnabled(true);
//        set.setDrawHorizontalHighlightIndicator(false);
//        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
//        set.setDrawIcons(false);
//        set.setHighlightLineWidth(1f);
//        set.setHighLightColor(DevLib.getApp().getResources().getColor(R.color.text_black));
//        set.setDrawValues(false);
//        set.setValueTextColor(DevLib.getApp().getResources().getColor(R.color.text_black));

    }

    public static void setMarkerView(MyCombinedChart chart) {
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(chart.getContext(), R.layout.mymarkerview);
        MyBottomMarkerView hMarkerView = new MyBottomMarkerView(chart.getContext(), R.layout.mymarkerview);
        chart.setMarker(leftMarkerView, hMarkerView);
    }

    public static void setMarkerView2(MyCombinedChart chart) {
        MyBottomMarkerView hMarkerView = new MyBottomMarkerView(chart.getContext(), R.layout.mymarkerview);
        chart.setMarker(null, hMarkerView);
    }

    public static void setChart(CombinedChart chart) {


        chart.setScaleEnabled(true);//启用图表缩放事件
        chart.setDrawBorders(true);//是否绘制边线
        chart.setBorderWidth(1);//边线宽度，单位dp
        chart.setDragEnabled(true);//启用图表拖拽事件
        chart.setScaleYEnabled(false);//启用Y轴上的缩放
        chart.setBorderColor(DevLib.getApp().getResources().getColor(R.color.stock_grid));//边线颜色
        chart.setDescription(null);//右下角对图表的描述信息
        chart.setMinOffset(0f);
        chart.setExtraOffsets(0f, 0f, 0f, 0f);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.getLegend().setEnabled(false);
        chart.getLegend().setDrawInside(true);


        XAxis xAxis = chart.getXAxis();
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(DevLib.getApp().getResources().getColor(R.color.stock_grid));
        xAxis.setGridLineWidth(1f);
        xAxis.setTextSize(DevLib.getApp().getResources().getDimension(R.dimen.default_size_20) / DisplayUtil.getDisplayMetrics().density);
        xAxis.setTextColor(DevLib.getApp().getResources().getColor(R.color.text_light_grey));
        xAxis.setAvoidFirstLastClipping(true);//设置首尾的值是否自动调整，避免被遮挡
        xAxis.setLabelCount(5, true);
        xAxis.setYOffset(0);


        YAxis axisRKline = chart.getAxisRight();
        axisRKline.setDrawGridLines(true);
        axisRKline.setDrawAxisLine(false);
        axisRKline.setDrawZeroLine(false);
        axisRKline.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisRKline.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRKline.setDrawLabels(true);
        axisRKline.setGridColor(DevLib.getApp().getResources().getColor(R.color.stock_grid));
        axisRKline.setGridLineWidth(1f);
        axisRKline.setTextColor(DevLib.getApp().getResources().getColor(R.color.text_light_grey));
        axisRKline.setLabelCount(5, true); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        axisRKline.setSpaceTop(0);//距离顶部留白


        YAxis axisRightKline = chart.getAxisLeft();
        axisRightKline.setDrawLabels(false);
        axisRightKline.setDrawGridLines(false);
        axisRightKline.setDrawAxisLine(false);
        axisRightKline.setLabelCount(5, true); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布

        chart.setDragDecelerationEnabled(true);
        chart.setDragDecelerationFrictionCoef(0.2f);
    }


    public static void setRealTimeChart(MyCombinedChart chart) {

        chart.setScaleEnabled(true);//启用图表缩放事件
        chart.setDrawBorders(true);//是否绘制边线
        chart.setBorderWidth(1);//边线宽度，单位dp
        chart.setDragEnabled(true);//启用图表拖拽事件
        chart.setScaleYEnabled(false);//启用Y轴上的缩放
        chart.setScaleXEnabled(false);//启用Y轴上的缩放
        chart.setBorderColor(DevLib.getApp().getResources().getColor(R.color.stock_grid));//边线颜色
        chart.setDescription(null);//右下角对图表的描述信息
        chart.setMinOffset(0f);
        chart.setExtraOffsets(0f, 0f, 0f, 0f);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.getLegend().setEnabled(false);
        setMarkerView(chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(DevLib.getApp().getResources().getColor(R.color.stock_grid));
        xAxis.setGridLineWidth(1f);
        xAxis.setTextSize(DevLib.getApp().getResources().getDimension(R.dimen.default_size_20) / DisplayUtil.getDisplayMetrics().density);
        xAxis.setTextColor(DevLib.getApp().getResources().getColor(R.color.text_light_grey));
        xAxis.setAvoidFirstLastClipping(true);//设置首尾的值是否自动调整，避免被遮挡
        xAxis.setLabelCount(5, true);

        YAxis axisRKline = chart.getAxisRight();
        axisRKline.setDrawGridLines(true);
        axisRKline.setDrawAxisLine(false);
        axisRKline.setDrawZeroLine(false);
        axisRKline.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisRKline.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRKline.setDrawLabels(true);
        axisRKline.setGridColor(DevLib.getApp().getResources().getColor(R.color.stock_grid));
        axisRKline.setGridLineWidth(1f);
        axisRKline.setTextColor(DevLib.getApp().getResources().getColor(R.color.text_light_grey));
        axisRKline.setLabelCount(5, true); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        axisRKline.setSpaceTop(10);//距离顶部留白


        YAxis axisRightKline = chart.getAxisLeft();
        axisRightKline.setDrawLabels(false);
        axisRightKline.setDrawGridLines(false);
        axisRightKline.setDrawAxisLine(false);
        axisRightKline.setLabelCount(5, true); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布

        chart.setDragDecelerationEnabled(true);
        chart.setDragDecelerationFrictionCoef(0.2f);
    }

    public static void setBarDataSet(BarDataSet set) {

        set.setHighlightEnabled(true);
        set.setDrawIcons(false);
        set.setDrawValues(false);
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        List<Integer> list = new ArrayList<>();
        list.add(DevLib.getApp().getResources().getColor(R.color.stock_up));
        list.add(DevLib.getApp().getResources().getColor(R.color.stock_down));
        set.setColors(list);
        set.setHighLightColor(DevLib.getApp().getResources().getColor(R.color.text_black));


//        set.setHighlightEnabled(true);
////        set.setDrawHorizontalHighlightIndicator(true);
//        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
////        set.setShadowWidth(1f);
//        set.setDrawIcons(false);
////        set.setDecreasingColor(DevLib.getApp().getResources().getColor(R.color.stock_down));//设置开盘价高于收盘价的颜色
////        set.setDecreasingPaintStyle(Paint.Style.FILL);
////        set.setIncreasingColor(DevLib.getApp().getResources().getColor(R.color.stock_up));//设置开盘价地狱收盘价的颜色
////        set.setIncreasingPaintStyle(Paint.Style.FILL);
////        set.setNeutralColor(DevLib.getApp().getResources().getColor(R.color.stock_grey));//设置开盘价等于收盘价的颜色
////        set.setShadowColorSameAsCandle(true);
////        set.setHighlightLineWidth(1f);
//        set.setHighLightColor(DevLib.getApp().getResources().getColor(R.color.text_black));
//        set.setDrawValues(false);
//        set.setValueTextColor(DevLib.getApp().getResources().getColor(R.color.text_black));
    }

    /**
     * Limits the maximum and minimum x range that can be visible by pinching and zooming. e.g. minRange=10, maxRange=100 the
     * smallest range to be displayed at once is 10, and no more than a range of 100 values can be viewed at once without
     * scrolling
     *
     * @param minXRange
     * @param maxXRange
     */
    public static void setVisibleXRange(CombinedChart chart, float minXRange, float maxXRange) {
        float minScale = chart.getXAxis().mAxisRange / minXRange;
        float maxScale = chart.getXAxis().mAxisRange / maxXRange;
        float scale = (minScale + maxScale) / 2;
        chart.getViewPortHandler();

        ViewPortHandler mViewPortHandler = chart.getViewPortHandler();
        mViewPortHandler.setMinMaxScaleX(minScale, maxScale);
        mViewPortHandler.getMatrixTouch().setScale(scale, 1);

    }

    public static void fitData(CombinedChart chart) {
        chart.setDragEnabled(true);//启用图表拖拽事件
        chart.setScaleEnabled(true);
        chart.highlightValue(null);
        setVisibleXRange(chart, 60, 20);
        chart.moveViewToX(chart.getCombinedData().getEntryCount());

    }

    public static void showHighline(CombinedChart chart, CombinedChart chart2) {
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.i("showHighline", "" + h.getX());
//                Highlight highlight = new Highlight(h.getX(), h.getY(),h.getXPx(),h.getYPx(), h.getDataIndex(),YAxis.AxisDependency.RIGHT);
//                Highlight highlight  = chart2.getHighlightByTouchPoint(h.getX(), 1);
                Highlight highlight = new Highlight(h.getX(), h.getY(), h.getXPx(), h.getYPx(), h.getDataSetIndex(), YAxis.AxisDependency.RIGHT);
                highlight.setDataIndex(h.getDataIndex());

                highlight.setDraw(h.getDrawX(), h.getDrawY());

                chart.highlightValue(h);

                Highlight highlight2 = new Highlight(h.getX(), h.getY(), h.getXPx(), h.getYPx(), h.getDataSetIndex(), YAxis.AxisDependency.RIGHT);
                highlight2.setDataIndex(h.getDataIndex());
                highlight2.setDraw(h.getDrawX(), 0);

                chart2.highlightValues(new Highlight[]{highlight2});
//                Highlight highlight2 = new Highlight(h.getX(), h.getY(), h.getXPx(), h.getYPx(), h.getDataSetIndex(), YAxis.AxisDependency.RIGHT);
//                highlight2.setDraw(h.getDrawX(), h.getDrawY());
//                highlight2.setDataIndex(h.getDataIndex());
//                float touchY2 = h.getTouchY() - mChartKline.getHeight() - mChartVolume.getHeight();
//                Highlight h2 = mChartCharts.getHighlightByTouchPoint(h.getXIndex(), touchY2);
//                highlight2.setTouchY(touchY2);
//                if (null == h2) {
//                    highlight2.setTouchYValue(0);
//                } else {
//                    highlight2.setTouchYValue(h2.getTouchYValue());
//                }


//                updateText(e.getXIndex());
            }


            @Override
            public void onNothingSelected() {
                chart.highlightValue(null);
                chart2.highlightValue(null);

            }
        });
    }
}
