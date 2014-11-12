/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework.DBUtil.JSON;

import debra.Framework.DBUtil.DBUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiadongwei on 14-1-24.
 */
public class DBHelper extends DBUtil {
	@Override
	public boolean queryResult(String sql) throws SQLException {
		boolean result = true;
		int rowCount = 0;
		sql = sql.trim();
		HashMap table = null;
		HashMap<String, String> title = null;
		List<HashMap> column = null;
		List<List> rows = null;
		List<String> row = null;
		statement = conn.prepareStatement(sql);
		rs = statement.executeQuery();
		conn.commit();
		rsmd = rs.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			title = new HashMap<>();
			title.put(rsmd.getColumnLabel(i), rsmd.getColumnClassName(i));
			if (column == null) column = new ArrayList();
			column.add(i - 1, title);
		}
		if (rs.next()) {
			row = new ArrayList();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				row.add(i - 1, rs.getString(i));
			}
			if (rows == null) rows = new ArrayList();
			rows.add(row);
			rowCount++;
			while (rs.next()) {
				row = new ArrayList();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					row.add(i - 1, rs.getString(i));
				}
				rows.add(row);
				rowCount++;
			}
		} else {
			result = false;
//			getJSONDebraTransfer().setThrow();
		}
		rs.close();
		statement.close();
		if (table == null) table = new HashMap();
		table.put("COUNT", rowCount);
		table.put("COLUMN", column);
		table.put("ROWS", rows);
		getJSONDebraTransfer().getSendDocument().put(tableName, table);
		return result;
	}

}
