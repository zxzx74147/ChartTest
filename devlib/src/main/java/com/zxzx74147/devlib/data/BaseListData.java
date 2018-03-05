package com.zxzx74147.devlib.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengxin on 16/8/21.
 */

public abstract class BaseListData<T> implements Serializable {
    public int num;   //列表个数
    public int hasMore;   //是否更多
    public int nextPage;   //下一页page号

    public abstract List<T> getListItems();
}
