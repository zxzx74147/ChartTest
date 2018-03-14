package com.zxzx74147.live.msg;

import android.databinding.BindingAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.zxzx74147.devlib.utils.ColorUtil;
import com.zxzx74147.live.R;
import com.zxzx74147.live.data.Msg;

/**
 * Created by zhengxin on 2018/3/10.
 */

public class MsgSpanUtil {

    @BindingAdapter({"msg_content"})
    public static void loadImage(TextView view, Msg msg) {
        Spannable span = getSpanString(msg);
        view.setText(span);
    }

    public static Spannable getSpanString(Msg msg) {
        switch (msg.type) {
            case Msg.TYPE_TEXT:
                return getTextSpan(msg);
            case Msg.TYPE_PROFIT:
                return getProfitSpan(msg);
            case Msg.TYPE_SYSTEM:
                return getSysSpan(msg);
        }
        return new SpannableString(msg.content);
    }

    private static Spannable getTextSpan(Msg msg) {
        String nickName = msg.nickName + ":";
        SpannableString result = new SpannableString(nickName + msg.content);
        result.setSpan(new ForegroundColorSpan(ColorUtil.getColor(R.color.text_light_grey)), 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        result.setSpan(new ForegroundColorSpan(ColorUtil.getColor(R.color.white)),  nickName.length(), result.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return result;
    }

    private static Spannable getSysSpan(Msg msg) {
        if (msg.content == null) {
            return new SpannableString("");
        }
        SpannableString result = new SpannableString(msg.nickName+msg.content);
        result.setSpan(new ForegroundColorSpan(ColorUtil.getColor(R.color.text_light_grey)), 0, result.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return result;
    }

    private static Spannable getProfitSpan(Msg msg) {
        if (msg.content == null) {
            return new SpannableString("");
        }
        String s1 = "盈利播报 "+msg.nickName+"盈利";
        String s2 = String.valueOf(msg.profitNum)+"元";
        SpannableString result = new SpannableString(s1+s2);
        result.setSpan(new ForegroundColorSpan(ColorUtil.getColor(R.color.yello)), 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        result.setSpan(new ForegroundColorSpan(ColorUtil.getColor(R.color.red)), s1.length(),s1.length()+s2.length(),  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return result;
    }


}
