package com.zxzx74147.devlib.wxapi;

import android.content.Context;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.utils.ToastUtil;

/**
 * Created by zhengxin on 2018/3/13.
 */

public class WxApiHandler {
    public static IWXAPI mIWXAPI = null;
    private static CommonCallback<SendAuth.Resp> mCallback = null;
    public static void createApi(Context context){
        mIWXAPI = WXAPIFactory.createWXAPI(context,context.getString(R.string.wx_appid),true);
        mIWXAPI.registerApp(context.getString(R.string.wx_appid));
    }

    public static void doLogin(Context context, CommonCallback<SendAuth.Resp> callback){
        if (!mIWXAPI.isWXAppInstalled()) {
            ToastUtil.showToast(context,"您还未安装微信客户端");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "mayi_mine";
        mIWXAPI.sendReq(req);
        mCallback = callback;
    }

    public static void dealLogin(SendAuth.Resp rsp){
        if(mCallback!=null){
            mCallback.callback(rsp);
        }
        mCallback = null;
    }
}
