package com.github.mikephil.charting.mychart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.zxzx74147.stock.R;

import java.text.DecimalFormat;

/**
 * Created by loro on 2017/2/8.
 */
public class MyLeftMarkerView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    private TextView markerTv;
    private float num;
    private DecimalFormat mFormat;
    public MyLeftMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        mFormat=new DecimalFormat("#0.0");
        markerTv = (TextView) findViewById(R.id.marker_tv);
        markerTv.setTextSize(10);
    }

    public void setData(float num){

        this.num=num;
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        markerTv.setText(mFormat.format(e.getY()));
    }

    MPPointF offset = new MPPointF(0,0);
    @Override
    public MPPointF getOffset() {
        return offset;
    }
}
