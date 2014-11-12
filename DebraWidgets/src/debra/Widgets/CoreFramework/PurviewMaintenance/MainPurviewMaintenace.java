/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Widgets.CoreFramework.PurviewMaintenance;

import debra.Framework.FrameworkFactory;
import debra.Framework.Interface.DebraWidget;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by xiadongwei on 14/10/22.
 */
public class MainPurviewMaintenace extends DebraWidget implements EventHandler<ActionEvent> {
    private BorderPane bp;
    private Button addButton;
    private Button searchButton;
    private Button editButton;
    private ToolBar tb;
    private MainPurviewMaintenaceController mc;

    @Override
    public boolean refresh() {
        return true;
    }

    public Button getAddButton() {
        if (addButton == null) {
            addButton = new Button("Add");
        }
        return addButton;
    }

    public void setAddButton(Button addButton) {
        this.addButton = addButton;
    }

    public Button getSearchButton() {
        if (searchButton == null) {
            searchButton = new Button("Search");
        }
        return searchButton;
    }

    public void setSearchButton(Button searchButton) {
        this.searchButton = searchButton;
    }

    public Button getEditButton() {
        if (editButton == null) {
            editButton = new Button("Edit");
        }
        return editButton;
    }

    public void setEditButton(Button editButton) {
        this.editButton = editButton;
    }

    @Override
    public Node getWidget() {
        if (bp != null) {
            return bp;
        }
        Pane pane = null;
        bp = new BorderPane();
        ScrollPane sp = new ScrollPane();
        try {
            String lang = widgetProperty.getDefaultLanguage();
            FXMLLoader fxl = new FXMLLoader(getClass().getResource(lang + "_MainPurviewMaintenace.fxml"));
            mc = new MainPurviewMaintenaceController();
            fxl.setController(mc);
            pane = fxl.load();
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        sp.setContent(pane);
        bp.setCenter(sp);
        return bp;
    }

    @Override
    public MenuBar getMenuBar() {
        return null;
    }

    @Override
    public ToolBar getCustomToolBar() {
        if (tb == null) {
            tb = new ToolBar();
            getSearchButton().setId("SearchButton");
            getEventHelper().addEventHandler(getSearchButton(), ActionEvent.ACTION, this);
            getAddButton().setId("AddButton");
            getEventHelper().addEventHandler(getAddButton(), ActionEvent.ACTION, this);
            getEditButton().setId("EditButton");
            getEventHelper().addEventHandler(getEditButton(), ActionEvent.ACTION, this);
            tb.getItems().add(getSearchButton());
            tb.getItems().add(getAddButton());
            tb.getItems().add(getEditButton());
        }
        return tb;
    }

    @Override
    public void handle(ActionEvent event) {

        if (event.getSource() instanceof Button) {
            Button button = (Button) event.getSource();
            switch (button.getId()) {
                case "SearchButton":
                    Search();
                    break;
                case "AddButton":
                    System.out.println("AddButton");
                    break;
                case "EditButton":
                    System.out.println("EditButton");
            }
        }
    }

    public void Search() {
        String actionID = mc.actionIDTextField.getText();
        String actionCLS = mc.actionCLSTextField.getText();
        String actionURL = mc.actionURLTextField.getText();
        String comment = mc.commentTextField.getText();
        StringBuilder bString = new StringBuilder("");

        if (!"".equals(actionID)) {
            bString.append(" and ").append("actionID like '").append(actionID).append("'");
        }
        if (!"".equals(actionCLS)) {
            bString.append(" and ").append("actionCLS like '").append(actionCLS).append("'");
        }
        if (!"".equals(actionURL)) {
            bString.append(" and ").append("actionURL like '").append(actionURL).append("'");
        }
        if (!"".equals(comment)) {
            bString.append(" and ").append("comment like '").append(comment).append("'");
        }
        String whereSql = bString.toString();

        SearchPurview searchPurview = new SearchPurview();
        searchPurview.setWhereSql(whereSql);
        searchPurview.setClassURL(this.getClassURL());

        FrameworkFactory.getInstance().actionProcess(searchPurview);

        this.releaseDebraTransfer();

    }

}
