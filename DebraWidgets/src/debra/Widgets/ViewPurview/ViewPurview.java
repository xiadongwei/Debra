/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Widgets.ViewPurview;

import debra.Framework.DebraProperty;
import debra.Framework.FrameworkFactory;
import debra.Framework.Interface.DebraWidget;
import debra.Framework.Transfer.DebraProtocolEnum;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.dom4j.Element;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tony
 */
public class ViewPurview extends DebraWidget {
    private ScrollPane sp;
    private BorderPane bp;
    private TreeView treeView;
    private CheckBoxTreeItem treeItem1;
    private AnchorPane mainPane;

    @Override
    public Node getWidget() {
        if (bp != null) {
            return bp;
        }
        bp = new BorderPane();
        bp.setLeft(getTreeList());
        bp.setCenter(getMainPane());
        return bp;
    }

    @Override
    public boolean refresh() {
        String name = null;
        String text = null;
//		treeView.getSelectionModel().selectAll();
        Iterator<CheckBoxTreeItem> l = treeView.getSelectionModel().getSelectedItems().iterator();
        System.out.println("select " + treeView.getSelectionModel().getSelectedItem());
        String result = "List:";
        CheckBoxTreeItem tItem = null;
        while (l.hasNext()) {
            tItem = l.next();
//			if (tItem.isSelected()){
            result = result + "  " + tItem.getValue();
//			}
        }
        System.out.println(result);
        try {
            setResponseClass(ListUsersTreeAction.class, DebraProtocolEnum.XML);
            getXMLDebraTransfer().send();
            getXMLDebraTransfer().receive();
            if (treeItem1 == null) {

            }
            treeItem1.getChildren().clear();
            List<Element> table = getXMLDebraTransfer().getReceivedDocument().getRootElement().selectNodes("TABLES");
            for (Element tableE : table) {
                List<Element> rows = tableE.selectNodes("R");
                for (Element rowsE : rows) {
                    List<Element> column = rowsE.elements();
                    for (Element e : column) {
                        name = e.getName();
                        text = e.getText();
                        treeItem1.getChildren().add(new CheckBoxTreeItem(text));
                    }
                }
            }
        } catch (IOException ex) {
            FrameworkFactory.getInstance().setBottomMessage(DebraProperty.ERROR, "Can't connect server");
            Logger.getLogger(ViewPurview.class.getName()).log(Level.OFF, "Can't connect Server", ex);
        } finally {
            release();
        }


        return true;
    }

    public Node getTreeList() {
        if (treeView == null) {
            treeView = new TreeView();
            treeView.setPickOnBounds(true);
            treeView.setShowRoot(true);
            treeView.setEditable(true);
            treeView.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
            treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
        if (treeItem1 == null) {
            treeItem1 = new CheckBoxTreeItem();
            treeItem1.setValue(this.widgetProperty.getLanguageValue("ViewPurview_Root"));
            treeView.setRoot(treeItem1);
            treeItem1.setExpanded(true);
        }
        return treeView;
    }

    public Node getMainPane() {

        return null;
    }

    @Override
    public MenuBar getMenuBar() {
        return null;
    }

    @Override
    public ToolBar getCustomToolBar() {
//        ToolBar tb = new ToolBar();
//        Button compileButton = new Button(this.widgetProperty.getLanguageValue("Debra_Compile"));
//        compileButton.setOnAction(null);
//        tb.getItems().add(compileButton);
        return null;
    }
}
