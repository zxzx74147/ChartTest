package com.zxzx74147.profile.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.kvstore.KVStore;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.busstation.StockBusStation;
import com.zxzx74147.devlib.umeng.UmengAction;
import com.zxzx74147.devlib.umeng.UmengAgent;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.data.ComVoucher;
import com.zxzx74147.profile.databinding.LayoutComVoucherBinding;

import java.util.HashSet;

/**
 * Created by zhengxin on 2018/3/27.
 */

public class ComVoucherUtil {

    private static HashSet<String> mReadTable = new HashSet<>();
    private static final String KEY_COM_VOUCHER = "KEY_COM_VOUCHER3";
    static{
        mReadTable = KVStore.get(KEY_COM_VOUCHER,HashSet.class);
        if(mReadTable==null){
            mReadTable = new HashSet<>();
        }
    }

    public static boolean isRead(String id){
        if(mReadTable.contains(id)){
            return true;
        }
        return false;
    }

    public static void markRead(String id){
        mReadTable.add(id);
        KVStore.put(KEY_COM_VOUCHER,mReadTable);
    }

    private static boolean useVoucher = false;

    public static void showComVoucher(Context context, ComVoucher voucher){
        if(isRead(voucher.voucherId)){
            return;
        }
        useVoucher = false;
        UmengAgent.onEvent(UmengAction.ALUmengPageCommonVoucher);
        markRead(voucher.voucherId);
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutComVoucherBinding binding = DataBindingUtil.inflate(LayoutInflater.from(DevLib.getApp()), R.layout.layout_com_voucher, null, false);
        dialog.setContentView(binding.getRoot());
        RxView.clicks(binding.close).subscribe(v -> {
            dialog.dismiss();
        });
        RxView.clicks(binding.justUse).subscribe(v -> {
            useVoucher = true;
            dialog.dismiss();
            UmengAgent.onEvent(UmengAction.ALUmengPageVoucherUse);
            StockBusStation.startStockTrade(context, AccountManager.sharedInstance().getUserUni().goodsTypeList.goodType.get(0), 0);
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(!useVoucher){
                    UmengAgent.onEvent(UmengAction.ALUmengPageVoucherClose);
                }
            }
        });
        binding.setVoucher(voucher);
        dialog.show();
    }
}
