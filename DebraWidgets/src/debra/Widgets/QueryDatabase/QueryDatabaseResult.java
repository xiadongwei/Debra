/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Widgets.QueryDatabase;

import debra.Framework.DebraProperty;
import debra.Framework.FrameworkFactory;
import debra.Framework.Interface.DebraWidget;
import debra.Framework.Tools.DynamicBean;
import debra.Framework.Tools.PropertyMap;
import debra.Framework.Tools.StringUtils;
import debra.Framework.Transfer.DebraProtocolEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xiadongwei on 14-1-13.
 */
public class QueryDatabaseResult extends DebraWidget {
    private BorderPane bp;
    private TableView table;
    private ObservableList data;
    private String sql;

    //	private TableView resultTableView;
    @Override
    public Node getWidget() {
        if (bp != null) {
            return bp;
        }
        bp = new BorderPane();
        bp.setCenter(getTable());
        return bp;
    }

    private Node getTable() {
        BorderPane bpt = new BorderPane();
        table = new TableView();
        bpt.setCenter(table);
        return bpt;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public boolean refresh() {
        String type = "EXECUTE";
        if (sql.substring(0, 4).toUpperCase().equals("SHOW") || sql.substring(0, 6).toUpperCase().equals("SELECT")) {
            type = "QUERY";
        }
        getJSONDebraTransfer().addSendAttribute("SQL", sql);
        getJSONDebraTransfer().addSendAttribute("TYPE", type);
        setResponseClass(QueryDatabaseAction.class, DebraProtocolEnum.JSON);
        try {
            getJSONDebraTransfer().send();
            getJSONDebraTransfer().receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
        table.getItems().clear();
        table.getColumns().clear();
        table.getItems().forEach((Object obj) -> obj = null);
        table.getItems().forEach((Object obj) -> obj = null);
        if (!getJSONDebraTransfer().isThrow() && ((String) getJSONDebraTransfer().getSendAttribute("TYPE")).equalsIgnoreCase("QUERY")) {
            Map<String, Object> result = (Map) getJSONDebraTransfer().getReceivedDocument().get("RESULT");
            List column = (List) result.get("COLUMN");
            PropertyMap propertyMap = null;
            ArrayList<TableColumn> columnName = null;
            ArrayList<String> columnType = null;
            for (int i = 0; i < column.size(); i++) {
                Map<String, String> col = ((Map) column.get(i));
                String propertyName = col.keySet().iterator().next();
                String propertyType = col.get(propertyName);
                if (propertyMap == null) {
                    propertyMap = new PropertyMap();
                    columnName = new ArrayList();
                    columnType = new ArrayList();
                }
                propertyName = StringUtils.toLowerCaseFirstOne(propertyName);
                TableColumn tableColumn = new TableColumn(propertyName);
                propertyName = propertyMap.put(propertyName, propertyType);
                tableColumn.setCellValueFactory(
                        new PropertyValueFactory(propertyName)
                );
                columnName.add(tableColumn);
                columnType.add(propertyType);
            }
            if (propertyMap == null) {
                return false;
            }
            if (data == null) {
                data = FXCollections.observableArrayList();
            }
            data.clear();
            data.forEach((Object obj) -> obj = null);
            int count = (int) result.get("COUNT");
            if (count > 0) {
                List rows = (List) result.get("ROWS");
                for (int r = 0; r < rows.size(); r++) {
                    List<String> row = (List) rows.get(r);
                    DynamicBean bean = new DynamicBean(propertyMap);
                    for (int c = 0; c < row.size(); c++) {
                        bean.setValue(columnName.get(c).getText(), row.get(c), columnType.get(c));
                    }
                    Object object = bean.getObject();
                    data.add(object);
                }
            }
            table.setItems(data);
            for (TableColumn c : columnName) {
                table.getColumns().add(c);
            }
            String countStr = String.valueOf(count);
            FrameworkFactory.getInstance().setBottomMessage(DebraProperty.MESSAGE, " Result " + countStr + " records");
        } else if (getXMLDebraTransfer().isThrow()) {
            FrameworkFactory.getInstance().setBottomMessage(DebraProperty.ERROR, " Query throw");
        } else {
            FrameworkFactory.getInstance().setBottomMessage(DebraProperty.ERROR, " Query failed");
        }
        releaseDebraTransfer();
        return true;
    /* JSON
        if (!getJSONDebraTransfer().isThrow() && ((String) getJSONDebraTransfer().getSendAttribute("TYPE")).equalsIgnoreCase("QUERY")) {
		Map<String,Object> result = (Map) getJSONDebraTransfer().getReceivedDocument().get("RESULT");
		List column = (List) result.get("COLUMN");
		List rows = (List) result.get("ROWS");
		String count = String.valueOf((int) result.get("COUNT"));
		for (int i = 0; i < column.size(); i++) {
				Label l = new Label(((Map<String,String>) column.get(i)).keySet().iterator().next());
			gridPane.add(l, i, 0);
			}
		for (int r = 0; r < rows.size(); r++) {
			List row = (List) rows.get(r);
			for (int c = 0; c < row.size(); c++) {
				Label l = new Label((String) row.get(c));
				gridPane.add(l, c, r + 1);
				}
		}
		FrameworkFactory.getInstance().setBottomMessage(DebraProperty.MESSAGE, " Result " + count + " records");
	} else if (getXMLDebraTransfer().isThrow()) {
		FrameworkFactory.getInstance().setBottomMessage(DebraProperty.ERROR, " Query throw");
		} else {
		FrameworkFactory.getInstance().setBottomMessage(DebraProperty.ERROR, " Query failed");
		}
	release();
	return true;
		*/
	/* xml
		if (!getXMLDebraTransfer().isThrow() && ((String)getXMLDebraTransfer().getSendAttribute("TYPE")).equalsIgnoreCase("QUERY")){
		List<Element> table = getXMLDebraTransfer().getReceivedDocument().getRootElement().selectNodes("RESULT");
		for (Element tableE : table) {       //create Table Title
			List<Element> columns = tableE.selectNodes("COLUMN");
			int cl = 0;
			for (Element columnE : columns) {
				List<Element> column = columnE.elements();
				for (Element e : column) {
					Label l = new Label(e.getName());
					l.setTextFill(Color.BLUE);
					gridPane.add(l, cl, 0);
					cl++;
					}
				}
				List<Element> rows = tableE.selectNodes("R");
			int r = 1;
			for (Element rowsE : rows) {         //create Table Rows
				List<Element> column = rowsE.elements();
				int c = 0;
				for (Element e : column) {
					gridPane.add(new Text(e.getText()), c, r);
					c++;
					}
				r++;
				}
			}
		}else if (getXMLDebraTransfer().isThrow()) {
		Label l = new Label("failed");
			l.setTextFill(Color.RED);
			gridPane.add(l, 0, 0);
		}else {
		Label l = new Label("failed");
			l.setTextFill(Color.GREEN);
			gridPane.add(l, 0, 0);
		}
		release();
		return true;
		*/
    }

    @Override
    public void release() {
        super.release();
        if (table != null) {
            table.getItems().clear();
            table = null;
        }
        if (data != null) {
            data.clear();
            data = null;
        }
        bp = null;
    }

    @Override
    public MenuBar getMenuBar() {
        return null;
    }

    @Override
    public ToolBar getCustomToolBar() {
        return null;
    }
}
