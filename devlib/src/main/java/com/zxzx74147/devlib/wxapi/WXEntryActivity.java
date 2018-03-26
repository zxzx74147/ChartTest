package com.zxzx74147.devlib.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.utils.ToastUtil;

/**
 * Created by zhengxin on 2018/3/13.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = WXEntryActivity.class.getSimpleName();

    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //如果没回调onResp，八成是这句没有写
        WxApiHandler.mIWXAPI.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        Log.i(TAG,resp.errStr+"");
        Log.i(TAG,"错误码 : " + resp.errCode + "");
        switch (resp.errCode) {

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                if (RETURN_MSG_TYPE_SHARE == resp.getType()) ToastUtil.showToast(this,"分享验证失败");
                else ToastUtil.showToast(this,"登录验证失败");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == resp.getType()) ToastUtil.showToast(this,"分享失败");
                else ToastUtil.showToast(this,"登录失败");
                finish();
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        Log.i(TAG,"code = " + code);
                        WxApiHandler.dealLogin((SendAuth.Resp) resp);
                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                        finish();
                        break;

                    case RETURN_MSG_TYPE_SHARE:
                        ToastUtil.showToast(this,"微信分享成功");
                        finish();
                        break;
                }
                break;
        }

    }

}
