/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Widgets.CoreFramework.PurviewMaintenance;

import debra.Framework.Interface.DebraAction;
import debra.Widgets.ProtocalEnum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by xiadongwei on 14/10/27.
 */
public class MainPurviewMaintenaceAction extends DebraAction {
    private static String SQL = "select * from t_purviews where Removed=0";
    private ProtocalEnum type;
    private String whereSql;

    @Override
    public void analyseParameter() {
        type = ProtocalEnum.valueOf((String) getJSONDebraTransfer().getReceiveAttribute("TYPE"));
        whereSql = ((String) getJSONDebraTransfer().getReceiveAttribute("WHERESQL"));
        System.out.println(SQL + whereSql);
    }

    @Override
    public void doAction(Connection connection) throws SQLException {
        if (type.equals(ProtocalEnum.SEARCH)) {
            getDBUtil().setTableName("RESULT");
            getDBUtil().queryResult(SQL + whereSql);
        }
    }
}
