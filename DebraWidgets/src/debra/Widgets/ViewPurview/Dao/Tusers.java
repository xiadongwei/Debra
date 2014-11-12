/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Widgets.ViewPurview.Dao;

import debra.Framework.Interface.DebraDao;

/**
 * Created by xiadongwei on 14-1-11.
 */
public class Tusers extends DebraDao {
    private String userName = "";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if (null != userName) this.userName = userName;
    }
}
