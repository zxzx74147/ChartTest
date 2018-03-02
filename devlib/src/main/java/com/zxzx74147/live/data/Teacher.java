
package com.zxzx74147.live.data;

import java.io.Serializable;
import java.util.List;

public class Teacher implements Serializable
{

    public int teacherId;
    public String name;
    public String portraitUrl;
    public int loveNum;
    public String detail;
    public List<Tag> tags = null;
    private final static long serialVersionUID = -3067263098062254995L;

}
