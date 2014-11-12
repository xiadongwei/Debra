
/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework.Interface;

import debra.Framework.DBUtil.DBUtil;
import debra.Framework.Transfer.DebraTransfer;
import debra.Framework.Transfer.JSON.JSONObject;
import org.dom4j.Document;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author user
 */
public abstract class DebraAction implements Cloneable {
	protected String userID;
	protected DebraTransfer debraTransfer;
	protected DBUtil dbUtil;

	public DebraAction() {
	}

	public void setDebraTransfer(DebraTransfer debraTransfer) {
		if (debraTransfer != null) {
			this.debraTransfer = debraTransfer;
		}
	}

	private DebraTransfer getDebraTransfer() {
		return debraTransfer;
	}

	public DebraTransfer<Document> getXMLDebraTransfer() {
		return getDebraTransfer();
	}

	public DebraTransfer<JSONObject> getJSONDebraTransfer() {
		return getDebraTransfer();
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserID() {
		return userID;
	}

	public abstract void analyseParameter();

	public void setDBUtil(DBUtil dbUtil) {
		this.dbUtil = dbUtil;
	}

	public DBUtil getDBUtil() {
		return dbUtil;
	}

	@Override
	public DebraAction clone() {
		try {
			return (DebraAction) super.clone();
		} catch (CloneNotSupportedException ex) {
			return null;
		}
	}

	public abstract void doAction(Connection conn) throws SQLException;

	public String getResponse() {
		return debraTransfer.getReceivedString();
	}
}
