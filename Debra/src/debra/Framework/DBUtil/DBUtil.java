/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework.DBUtil;

import debra.Framework.Transfer.DebraTransfer;
import debra.Framework.Transfer.JSON.JSONObject;
import org.dom4j.Document;

import java.sql.*;

/**
 * Created by xiadongwei on 14-1-24.
 */
public abstract class DBUtil implements Cloneable {
	protected String tableName;
	protected Connection conn;
	protected String sql;
	protected PreparedStatement statement;
	protected ResultSet rs;
	protected ResultSetMetaData rsmd;
	private DebraTransfer debraTransfer;

	public DBUtil() {
	}

	public DBUtil(Connection conn, DebraTransfer debraTransfer) {
		setConnection(conn);
		setDebraTransfer(debraTransfer);
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	public void setDebraTransfer(DebraTransfer debraTransfer) {
		this.debraTransfer = debraTransfer;
	}

	public DebraTransfer<Document> getXMLDebraTransfer() {
		return debraTransfer;
	}

	public DebraTransfer<JSONObject> getJSONDebraTransfer() {
		return debraTransfer;
	}

	public void setSql(String sql) {
		if (sql != null && !"".equals(sql))
			this.sql = sql.trim();
		else this.sql = "";
	}

	public void setTableName(String tableName) {
		if (tableName != null && !"".equals(tableName))
			this.tableName = tableName.trim().toUpperCase();
		else this.tableName = "";
	}

	public boolean queryResult() throws SQLException {
		return queryResult(sql);
	}

	public boolean executeSql() throws SQLException {
		return executeSql(sql);
	}

	abstract public boolean queryResult(String sql) throws SQLException;

	public boolean executeSql(String sql) throws SQLException {
		boolean b = false;
		sql = sql.trim();
		statement = conn.prepareStatement(sql);
		b = statement.execute();
		conn.commit();
		if (!b) getXMLDebraTransfer().addSendAttribute("throw", "true");
		return b;
	}

	public long getNumber(String sql) throws SQLException {
		sql = sql.trim();
		long rowCount = 0;
		statement = conn.prepareStatement(sql);
		rs = statement.executeQuery();
		conn.commit();
		if (rs.next()) {
			rowCount = rs.getLong(1);
		}
		rs.close();
		statement.close();
		return rowCount;
	}

	@Override
	public DBUtil clone() {
		try {
			return (DBUtil) super.clone();
		} catch (CloneNotSupportedException ex) {
			return null;
		}
	}
}
