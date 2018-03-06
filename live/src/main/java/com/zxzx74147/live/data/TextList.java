
package com.zxzx74147.live.data;

import com.zxzx74147.devlib.data.BaseListData;

import java.io.Serializable;
import java.util.List;

public class TextList extends BaseListData<Text> implements Serializable
{

    public int nextTextId;
    public List<Text> text = null;
    private final static long serialVersionUID = -4936385251723593416L;

    @Override
    public List<Text> getListItems() {
        return text;
    }
}
