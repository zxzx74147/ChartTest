package com.zxzx74147.devlib.interfaces;

import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.stock.data.Position;

import io.reactivex.Observable;

/**
 * Created by zhengxin on 2018/3/4.
 */

public interface CommonListRequestCallback<T> {
     Observable<? extends IBaseListDataHolder<T>> getObserverble(BaseListData<T> listdata);
}
