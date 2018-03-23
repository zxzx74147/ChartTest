package com.zxzx74147.devlib.widget;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

/**
 * Created by zhengxin on 2018/3/23.
 */

public class ChineseFilter implements InputFilter {
    private String regEx = "[\\u4e00-\\u9fa5]{1,10}[\\u4e00-\\u9fa5]";
    private Pattern pattern = Pattern.compile(regEx);

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (!pattern.matcher(source).matches()) {
            return "";//表示条件不符,不输入将要输入的字符串,所以在这里可以做很多事
        }
        return null;
    }
}
