/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Widgets.CoreFramework.PurviewMaintenance;

import debra.Framework.DebraProperty;
import debra.Framework.FrameworkFactory;
import debra.Framework.Interface.DebraWidget;
import debra.Framework.Tools.DynamicBean;
import debra.Framework.Tools.PropertyMap;
import debra.Framework.Tools.StringUtils;
import debra.Framework.Transfer.DebraProtocolEnum;
import debra.Widgets.ProtocalEnum;
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
 * Created by xiadongwei on 14/10/26.
 */
public class SearchPurview extends DebraWidget {

    private BorderPane bp;
    private TableView table;
    private ObservableList data;
    private String whereSql = "";

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

    @Override
    public boolean refresh() {
        this.getJSONDebraTransfer().addSendAttribute("TYPE", ProtocalEnum.SEARCH);
        this.getJSONDebraTransfer().addSendAttribute("WHERESQL", whereSql);
        this.setResponseClass(MainPurviewMaintenaceAction.class, DebraProtocolEnum.JSON);
        try {
            this.getJSONDebraTransfer().send();
            this.getJSONDebraTransfer().receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
        table.getItems().clear();
        table.getColumns().clear();
        if (!getJSONDebraTransfer().isThrow() && (getJSONDebraTransfer().getSendAttribute("TYPE")).equals(ProtocalEnum.SEARCH)) {
            Map<String, Object> result = (Map<String, Object>) getJSONDebraTransfer().getReceivedDocument().get("RESULT");
            List<Map<String, String>> column = (List<Map<String, String>>) result.get("COLUMN");
            PropertyMap propertyMap = null;
            ArrayList<TableColumn> columnName = null;
            ArrayList<String> columnType = null;
            for (Map<String, String> col : column) {
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
            int count = (int) result.get("COUNT");
            if (count > 0) {
                List<List<String>> rows = (List) result.get("ROWS");
                for (List<String> row : rows) {
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
    }

    public void setWhereSql(String whereSql) {
        this.whereSql = whereSql;
    }

    @Override
    public MenuBar getMenuBar() {
        return null;
    }

    @Override
    public ToolBar getCustomToolBar() {
        return null;
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
}
