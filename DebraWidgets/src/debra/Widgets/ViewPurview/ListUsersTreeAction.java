/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Widgets.ViewPurview;

import debra.Framework.Interface.DebraAction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by xiadongwei on 14-1-6.
 */
public class ListUsersTreeAction extends DebraAction {
    @Override
    public void analyseParameter() {
    }

    @Override
    public void doAction(Connection connection) throws SQLException {
        getDBUtil().setTableName("TABLES");
//		long count = getDbToXML().getNumber("select count(id) from t_app_assert");
//		Logger.getLogger(ViewPurview.class.getName()).log(Level.INFO, "count = " + count);
//		getDbToXML().queryXMLResult("select id,sap_id,sn_id,adpt_class1,adpt_class2,adpt_class3,purch_date,adpt_health,adpt_type,org_comp,org_dept,owner,locus,cpu,memory,disk,remark,changed_Reason,status,update_by,update_date,action_type,deposit,price,supplier,quantity from t_app_assert limit 1," + 50);
        getDBUtil().queryResult("show tables");
//		getDbToXML().setTableName("TUSERS");
//		long count = getDbToXML().getNumber("select count(id from t_users");
//		getDbToXML().queryXMLResult("select UserName from t_users");
    }
}
