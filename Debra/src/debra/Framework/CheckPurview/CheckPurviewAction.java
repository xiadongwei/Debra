/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework.CheckPurview;

import debra.Framework.Interface.DebraAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by xiadongwei on 14-2-9.
 */
public class CheckPurviewAction extends DebraAction {
	private String actionCLS;

	@Override
	public void analyseParameter() {
		actionCLS = (String) getJSONDebraTransfer().getReceiveAttribute("ActionCLS");
	}

	@Override
	public void doAction(Connection conn) throws SQLException {
		String sql = "select distinct  t_purviews.ActionURL as ActionURL from t_role_purviews  " +
			"left join t_purviews  " +
			"on t_role_purviews.PurviewID=t_purviews.id  " +
			"left join t_roles  " +
			"on t_roles.id = t_role_purviews.RoleID  " +
			"left join t_user_roles  " +
			"on t_roles.id = t_user_roles.RoleID " +
			"where t_user_roles.UserID = " + this.getUserID() + " and t_purviews.ActionCLS = '" + actionCLS + "'";
		PreparedStatement statement = null;
		ResultSet rs = null;
		statement = conn.prepareStatement(sql);
		rs = statement.executeQuery();
		if (rs.next()) {
			getJSONDebraTransfer().addSendAttribute("ActionURL", rs.getString("ActionURL"));
		} else {
			getJSONDebraTransfer().addSendAttribute("ActionURL", "");
			getJSONDebraTransfer().setThrow();
		}
	}
}
