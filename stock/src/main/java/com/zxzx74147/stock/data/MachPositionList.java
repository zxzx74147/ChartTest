package com.zxzx74147.stock.data;

import com.zxzx74147.devlib.data.UniApiData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengxin on 2018/2/28.
 */

public class MachPositionList  implements Serializable {
    public int num;   //
    public int hasMore;   //
    public int nextPage;   //
    public List<MachPosition> machPosition;


}
