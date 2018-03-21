package com.github.mikephil.charting.mychart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.font.FontBinder;
import com.zxzx74147.stock.R;

import java.util.List;

/**
 * Created by loro on 2017/2/8.
 */
public class MyCombinedChart extends CombinedChart {
    private MyLeftMarkerView myMarkerViewLeft;
    //    private MyHMarkerView myMarkerViewH;
    private MyBottomMarkerView myBottomMarkerView;
    private Paint mPaint = new Paint();
    private int top = getResources().getDimensionPixelSize(R.dimen.default_gap_20);
    private static String FORMAT_LABEL = DevLib.getApp().getString(R.string.format_label);
    private IAxisValueFormatter mLabelFormater = null;
//    private DataParse minuteHelper;

    private boolean mDrawMarkerViews = true;

    public MyCombinedChart(Context context) {
        super(context);
    }

    public MyCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setMarker(MyLeftMarkerView markerLeft, MyBottomMarkerView markerBottom) {
        this.myMarkerViewLeft = markerLeft;
        this.myBottomMarkerView = markerBottom;
        mPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.default_size_20));
        mPaint.setTypeface(FontBinder.DIN);
//        this.minuteHelper = minuteHelper;
    }

    public void setAxisValueFormatter(IAxisValueFormatter formatter) {
        mLabelFormater = formatter;
    }


//    public void setMarker(MyLeftMarkerView markerLeft, MyHMarkerView markerH, DataParse minuteHelper) {
//        this.myMarkerViewLeft = markerLeft;
////        this.myMarkerViewH = markerH;
////        this.minuteHelper = minuteHelper;
//    }
//
//    public void setMarker(MyLeftMarkerView markerLeft, MyBottomMarkerView markerBottom, DataParse minuteHelper) {
//        this.myMarkerViewLeft = markerLeft;
//        this.myBottomMarkerView = markerBottom;
////        this.minuteHelper = minuteHelper;
//    }
//
//    public void setMarker(MyLeftMarkerView markerLeft, MyBottomMarkerView markerBottom, MyHMarkerView markerH, DataParse minuteHelper) {
//        this.myMarkerViewLeft = markerLeft;
//        this.myBottomMarkerView = markerBottom;
////        this.myMarkerViewH = markerH;
////        this.minuteHelper = minuteHelper;
//    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        if (!mDrawMarkerViews )
            return;
        if(mIndicesToHighlight==null||mIndicesToHighlight.length==0) {
            float maxX = getHighestVisibleX();
            List<BarLineScatterCandleBubbleData> alldata = mData.getAllData();
            for (BarLineScatterCandleBubbleData bubbleData : alldata) {
                int length = 15;
                if (bubbleData instanceof LineData) {

                    for (Object setObj : bubbleData.getDataSets()) {
                        LineDataSet set = (LineDataSet) setObj;
//                            Log.e("Draw","index"+mIndicesToHighlight[i].getDataIndex());
//                        int index = (int) (set.getValues().size() - (mData.getXMax() - mIndicesToHighlight[i].getX())) - 1;
//                        if (index < 0) {
//                            continue;
//                        }
                        if(maxX>set.getValues().size()){
                            continue;
                        }
                        Entry eD = set.getValues().get((int) maxX);
                        mPaint.setColor(set.getColor());
                        if (eD != null && !TextUtils.isEmpty(set.getLabel())) {
                            String context = String.format(FORMAT_LABEL, set.getLabel(), eD.getY());
                            canvas.drawText(context, mViewPortHandler.contentLeft() + length, mViewPortHandler.contentTop() + top, mPaint);
                            length += mPaint.measureText(context) + 10;
                        }
                    }
                }
            }
        }else {

            for (int i = 0; i < mIndicesToHighlight.length; i++) {
                Highlight highlight = mIndicesToHighlight[i];
                int xIndex = mIndicesToHighlight[i].getDataSetIndex();
                int dataSetIndex = mIndicesToHighlight[i].getDataSetIndex();
                float deltaX = mXAxis != null
                        ? mXAxis.mAxisRange
                        : ((mData == null ? 0.f : mData.getXMax()) - 1.f);
                if (xIndex <= deltaX && xIndex <= deltaX * mAnimator.getPhaseX()) {
                    Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
                    List<BarLineScatterCandleBubbleData> alldata = mData.getAllData();
                    for (BarLineScatterCandleBubbleData bubbleData : alldata) {
                        int length = 15;
                        if (bubbleData instanceof LineData) {

                            for (Object setObj : bubbleData.getDataSets()) {
                                LineDataSet set = (LineDataSet) setObj;
//                            Log.e("Draw","index"+mIndicesToHighlight[i].getDataIndex());
                                int index = (int) (set.getValues().size() - (mData.getXMax() - mIndicesToHighlight[i].getX())) - 1;
                                if (index < 0) {
                                    continue;
                                }
                                Entry eD = set.getValues().get(index);
                                mPaint.setColor(set.getColor());
                                if (eD != null && !TextUtils.isEmpty(set.getLabel())) {
                                    String context = String.format(FORMAT_LABEL, set.getLabel(), eD.getY());
                                    canvas.drawText(context, mViewPortHandler.contentLeft() + length, mViewPortHandler.contentTop() + top, mPaint);
                                    length += mPaint.measureText(context) + 10;
                                }
                            }
                        }
                    }
                    // make sure entry not null
                    if (e == null || ((int) e.getX()) != mIndicesToHighlight[i].getX())
                        continue;
                    float[] pos = getMarkerPosition(highlight);
                    // check bounds
                    if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                        continue;

//                if (null != myMarkerViewH) {
//                    myMarkerViewH.refreshContent(e, mIndicesToHighlight[i]);
//                    int width = (int) mViewPortHandler.contentWidth();
//                    myMarkerViewH.setTvWidth(width);
//                    myMarkerViewH.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//                    myMarkerViewH.layout(0, 0, width,
//                            myMarkerViewH.getMeasuredHeight());
//                    myMarkerViewH.draw(canvas, mViewPortHandler.contentLeft(), mIndicesToHighlight[i].getTouchY() - myMarkerViewH.getHeight() / 2);
//                }

                    if (null != myMarkerViewLeft) {
                        //修改标记值

                        float yValForHighlight = mIndicesToHighlight[i].getTouchYValue();
                        myMarkerViewLeft.setData(yValForHighlight);

                        myMarkerViewLeft.refreshContent(e, mIndicesToHighlight[i]);

                        myMarkerViewLeft.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                        myMarkerViewLeft.layout(0, 0, myMarkerViewLeft.getMeasuredWidth(),
                                myMarkerViewLeft.getMeasuredHeight());
                        myMarkerViewLeft.draw(canvas, mViewPortHandler.contentRight() - myMarkerViewLeft.getWidth(), mIndicesToHighlight[i].getDrawY() - myMarkerViewLeft.getHeight() / 2);
//                    myMarkerViewLeft.draw(canvas, mViewPortHandler.contentLeft(), mIndicesToHighlight[i].getTouchY() - myMarkerViewLeft.getHeight() / 2);

                    }

                    if (null != myBottomMarkerView) {
                        String time = getXAxis().getValueFormatter().getFormattedValue(mIndicesToHighlight[i].getX(), getXAxis());
                        if (mLabelFormater != null) {
                            time = mLabelFormater.getFormattedValue(mIndicesToHighlight[i].getX(), getXAxis());
                        }
//                    String time= getXAxis().getValueFormatter().getFormattedValue(mIndicesToHighlight[i].getX(),getXAxis());
//                    String time  = getXAxis().getFormattedLabel((int) mIndicesToHighlight[i].getX());
//                    String time = minuteHelper.getKLineDatas().get(mIndicesToHighlight[i].getXIndex()).date;
                        myBottomMarkerView.setData(time);
                        myBottomMarkerView.refreshContent(e, mIndicesToHighlight[i]);

                        myBottomMarkerView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                        myBottomMarkerView.layout(0, 0, myBottomMarkerView.getMeasuredWidth(),
                                myBottomMarkerView.getMeasuredHeight());

                        myBottomMarkerView.draw(canvas, pos[0] - myBottomMarkerView.getWidth() / 2, mViewPortHandler.contentBottom());
                    }


                }
            }
        }
    }
}
