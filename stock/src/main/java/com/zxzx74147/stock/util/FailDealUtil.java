package com.zxzx74147.stock.util;

import android.content.Context;

import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.fragment.CommonFragmentDialog;
import com.zxzx74147.devlib.modules.busstation.BalanceBusStation;
import com.zxzx74147.devlib.modules.busstation.ProfileBusStation;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.stock.data.Fail;

/**
 * Created by zhengxin on 2018/3/5.
 */

public class FailDealUtil {


    public static boolean dealFail(Context context,Fail failed){
        if(failed==null){
            return false;
        }
        if(failed.errno== Fail.FAIL_PASS_OUT_OF_TIME){
            ProfileBusStation.startTradeLogin(context);
            return true;
        }
        else if(failed.errno== Fail.FAIL_PASS_MONEY_NOT_ENOUGTH){
            DialogItem dialogItem = new DialogItem();
            dialogItem.title = failed.title;
            dialogItem.content =failed.advice;
            dialogItem.obj = failed;
            CommonFragmentDialog fragmentDialog = CommonFragmentDialog.newInstance(new IntentData<>(dialogItem));
            ZXFragmentJumpHelper.startFragment(context, fragmentDialog, new CommonCallback() {
                @Override
                public void callback(Object item) {
                    //TODO
                    if(item!=null){
                        BalanceBusStation.startRecharge(context);
                    }

                }
            });
            return true;
        }
        DialogItem dialogItem = new DialogItem();
        dialogItem.title = failed.title;
        dialogItem.content =failed.advice;
        CommonFragmentDialog fragmentDialog = CommonFragmentDialog.newInstance(new IntentData<>(dialogItem));
        ZXFragmentJumpHelper.startFragment(context, fragmentDialog, new CommonCallback() {
            @Override
            public void callback(Object item) {
                //TODO

            }
        });
        return true;
    }
}
