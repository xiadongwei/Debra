/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Framework.Login;

import debra.Framework.Interface.DebraAction;
import org.dom4j.Element;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author user
 */
public class LoginAction extends DebraAction {
	private String userName;
	private String userPass;

	@Override
	public void analyseParameter() {
		userName = ((String) getXMLDebraTransfer().getReceiveAttribute("UserName")).trim();
		userPass = ((String) getXMLDebraTransfer().getReceiveAttribute("UserPass")).trim();
	}

	@Override
	public void doAction(Connection conn) throws SQLException {
		String sql = "select distinct t_purviews.ActionID as ActionID, t_purviews.ActionCLS as ActionCLS, t_users.id as uid from t_role_purviews " +
		    "left join t_purviews " +
		    "on t_role_purviews.PurviewID=t_purviews.id " +
		    "left join t_roles " +
		    "on t_roles.id = t_role_purviews.RoleID " +
		    "left join t_user_roles " +
		    "on t_roles.id = t_user_roles.RoleID " +
		    "left join t_users " +
		    "on t_users.id=t_user_roles.UserID " +
		    "where t_users.UserName = '" + userName + "' and t_users.password ='" + userPass + "'";
		PreparedStatement statement = null;
		ResultSet rs = null;
		statement = conn.prepareStatement(sql);
		rs = statement.executeQuery();
		Element actions = null;
		String id = null;
		if (rs.next()) {
			if (actions == null)
				actions = getXMLDebraTransfer().getSendDocument().getRootElement().addElement("Actions");
			if (id == null) {
				id = rs.getString("uid");
				getXMLDebraTransfer().addSendAttribute("UserID", id);
			}
			Element action = actions.addElement("Action");
			action.addAttribute("id", rs.getString("ActionID"));
			action.addAttribute("cls", rs.getString("ActionCLS"));
			while (rs.next()) {
				action = actions.addElement("Action");
				action.addAttribute("id", rs.getString("ActionID"));
				action.addAttribute("cls", rs.getString("ActionCLS"));
			}
		} else {
			getXMLDebraTransfer().setThrow();
		}
	}
}
