
package com.zxzx74147.balance.data;

import java.io.Serializable;
import java.util.List;

public class DepositList implements Serializable
{

    public int num;
    public int hasMore;
    public int nextPage;
    public List<Deposit> deposit = null;
    private final static long serialVersionUID = -6248660443700361106L;

}
