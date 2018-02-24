package com.zxzx74147.stock.utils;

import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
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
            KLineBean entry = (KLineBean) data.get(index).getData();
            Date temp = null;
            try {
                temp = (TIME_PARSER.parse(entry.date));
                return TIME_HHMM.format(temp);
            } catch (ParseException e) {
                e.printStackTrace();
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
        set.setDrawHorizontalHighlightIndicator(false);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setShadowWidth(1f);
        set.setDrawIcons(false);
        set.setDrawValues(false);
        set.setDecreasingColor(DevLib.getApp().getResources().getColor(R.color.stock_down));//设置开盘价高于收盘价的颜色
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(DevLib.getApp().getResources().getColor(R.color.stock_up));//设置开盘价地狱收盘价的颜色
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setNeutralColor(DevLib.getApp().getResources().getColor(R.color.stock_grey));//设置开盘价等于收盘价的颜色
        set.setShadowColorSameAsCandle(true);
        set.setHighlightLineWidth(1f);
        set.setHighLightColor(DevLib.getApp().getResources().getColor(R.color.text_grey));
        set.setDrawValues(false);
        set.setValueTextColor(DevLib.getApp().getResources().getColor(R.color.text_black));
    }

    public static void setLineDataleSet(LineDataSet set, int color) {

        set.setHighLightColor(DevLib.getApp().getResources().getColor(R.color.text_grey));
        set.setDrawValues(false);
        set.setLineWidth(1f);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setDrawIcons(false);
        set.setColor(color);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        set.setHighlightEnabled(false);
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
        set.setHighLightAlpha(255);
        set.setHighLightColor(DevLib.getApp().getResources().getColor(R.color.text_grey));
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        List<Integer> list = new ArrayList<>();
        list.add(DevLib.getApp().getResources().getColor(R.color.stock_up));
        list.add(DevLib.getApp().getResources().getColor(R.color.stock_down));
        set.setColors(list);
    }
}
