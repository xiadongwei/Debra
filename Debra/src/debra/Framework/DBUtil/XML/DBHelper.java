/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework.DBUtil.XML;

import debra.Framework.DBUtil.DBUtil;
import org.dom4j.Element;

import java.sql.SQLException;

/**
 * Created by xiadongwei on 14-1-11.
 */
public class DBHelper extends DBUtil {
	@Override
	public boolean queryResult(String sql) throws SQLException {
		boolean result = true;
		int rowCount = 0;
		sql = sql.trim();
		statement = conn.prepareStatement(sql);
		rs = statement.executeQuery();
		conn.commit();
		rsmd = rs.getMetaData();
		Element table = getXMLDebraTransfer().getSendDocument().getRootElement().addElement(tableName);
		Element title = table.addElement("COLUMN");
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			Element column = title.addElement(rsmd.getColumnLabel(i));
			column.addAttribute("TYPE", rsmd.getColumnClassName(i));
			column.addText("C" + String.valueOf(i));
		}
		if (rs.next()) {
			Element record = table.addElement("R");
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				Element column = record.addElement("C" + i);
				column.addText(rs.getString(i));
			}
			rowCount++;
			while (rs.next()) {
				record = table.addElement("R");
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					Element column = record.addElement("C" + i);
					column.addText(rs.getString(i));
				}
				rowCount++;
			}
		} else {
			result = false;
			getXMLDebraTransfer().setThrow();
		}
		table.addAttribute("COUNT", String.valueOf(rowCount));
		rs.close();
		statement.close();
		return result;
	}

}
