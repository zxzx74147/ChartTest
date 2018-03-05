
package com.zxzx74147.devlib.data;

import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.R;

import java.io.Serializable;

public class DialogItem implements Serializable
{

    public String title;
    public String content;
    public String cancel = DevLib.getApp().getResources().getString(R.string.cancel);
    public String ok= DevLib.getApp().getResources().getString(R.string.ok);
    public Object obj;

}
