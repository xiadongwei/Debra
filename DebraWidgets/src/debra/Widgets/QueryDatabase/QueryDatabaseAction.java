/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Widgets.QueryDatabase;

import debra.Framework.Interface.DebraAction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by xiadongwei on 14-1-13.
 */
public class QueryDatabaseAction extends DebraAction {
    private String sql;
    private String type;

    @Override
    public void analyseParameter() {
//		sql = ((String)getXMLDebraTransfer().getReceiveAttribute("SQL")).trim();
//		type = ((String)getXMLDebraTransfer().getReceiveAttribute("TYPE")).trim();

        sql = ((String) getJSONDebraTransfer().getReceiveAttribute("SQL")).trim();
        type = ((String) getJSONDebraTransfer().getReceiveAttribute("TYPE")).trim();
    }

    @Override
    public void doAction(Connection connection) throws SQLException {

        if ("QUERY".equals(type)) {
            getDBUtil().setTableName("RESULT");
            getDBUtil().queryResult(sql);
        }
        if ("EXECUTE".equals(type)) {
            getDBUtil().executeSql(sql);
        }

    }
}
