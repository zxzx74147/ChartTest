package com.zxzx74147.devlib.encrypt;

import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.R;
import com.zxzx74147.devlib.utils.RsaUtil;

import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by zhengxin on 2018/3/16.
 */

public class RsaPare {

    public RSAPublicKey mPublicKey = null;
    public RSAPrivateKey mPrivateKey = null;

    private RsaPare(){

    }

    public boolean init(){
        try {
            InputStream publicKey = DevLib.getApp().getResources().openRawResource(R.raw.rsa_public_key);
            mPublicKey = RsaUtil.loadPublicKey(publicKey);
            InputStream privateKey = DevLib.getApp().getResources().openRawResource(R.raw.client_rsa_private_key);
            mPrivateKey = RsaUtil.loadPrivateKey(privateKey);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
