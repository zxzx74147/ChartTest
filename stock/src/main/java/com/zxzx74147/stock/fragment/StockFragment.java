package com.zxzx74147.stock.fragment;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.base.BaseFragment;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.KLineData;
import com.zxzx74147.stock.viewmodel.StockViewModel;

/**
 */
public class StockFragment extends BaseDialogFragment {

    private StockViewModel mViewModel = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stock, container, false);
        return v;
    }

    private void initData(){
        mViewModel = ViewModelProviders.of(StockFragment.this).get(StockViewModel.class);
//        mViewModel.getKLineData()
        mViewModel.getKLineData().observe(StockFragment.this, new Observer<KLineData>() {
            @Override
            public void onChanged(@Nullable KLineData kLineData) {

            }
        });
    }



}
