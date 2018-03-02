package com.zxzx74147.devlib.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhengxin on 2018/2/21.
 */

public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int spaceX;
    private int spaceY;
    private int colNum;

    public GridSpaceItemDecoration(int colNum,int spaceX, int spaceY) {
        this.spaceX = spaceX;
        this.spaceY = spaceY;
        this.colNum = colNum;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildLayoutPosition(view) %colNum== colNum-1) {
        }else{
            outRect.right = spaceX;
        }
        outRect.bottom = spaceY;
//        if(parent.getChildLayoutPosition(view) != 0) {
////            outRect.left = spaceX / 2;
////            outRect.right = spaceX / 2;
////            outRect.top = spaceY / 2;
////            outRect.bottom = spaceY / 2;
//
//            outRect.left = spaceX ;
//            outRect.top = spaceY;
//        }
    }
}
