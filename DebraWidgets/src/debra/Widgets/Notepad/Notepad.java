/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Widgets.Notepad;

import debra.Debra;
import debra.Framework.DebraEvent;
import debra.Framework.DebraProperty;
import debra.Framework.Interface.DebraWidget;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * @author user
 */
public class Notepad extends DebraWidget implements EventHandler<ActionEvent> {
    private HTMLEditor textPad;
    private String NOTEPAD_SAVE = "Notepad_Save";
    private BorderPane bp;
    private String widgetName = "Notepad";
    private MenuBar mb;

    @Override
    public Pane getWidget() {
        if (bp != null) {
            return bp;
        }
        bp = new BorderPane();
        bp.setCenter(getTextPad());
        return bp;
    }

    @Override
    public boolean refresh() {
        return true;
    }

    public HTMLEditor getTextPad() {
        if (textPad == null) {
            textPad = new HTMLEditor();
        }
        return textPad;
    }

    @Override
    public MenuBar getMenuBar() {
        if (mb == null) {
            mb = new MenuBar();
            MenuItem saveItem = new MenuItem(widgetProperty.getLanguageValue("Debra_MenuItem_Save"));
            saveItem.setId(NOTEPAD_SAVE);
            saveItem.setOnAction(this);
            MenuItem closeItem = new MenuItem(widgetProperty.getLanguageValue("Debra_MenuItem_Close"));
            closeItem.setOnAction(DebraEvent.getInstance());
            closeItem.setId(DebraProperty.DEBRA_CLOSE);
            MenuItem aboutItem = new MenuItem(widgetProperty.getLanguageValue("Debra_MenuItem_About"));
            aboutItem.setOnAction(DebraEvent.getInstance());
            aboutItem.setId(DebraProperty.DEBRA_ABOUT);
            Menu fileMenu = new Menu(widgetProperty.getLanguageValue("Debra_Menu_File"));
            fileMenu.getItems().addAll(saveItem, closeItem);
            Menu helpMenu = new Menu(widgetProperty.getLanguageValue("Debra_Menu_Help"));
            helpMenu.getItems().addAll(aboutItem);
            mb.getMenus().addAll(fileMenu, helpMenu);
        }
        return mb;
    }

    @Override
    public ToolBar getCustomToolBar() {
        /*ToolBar tb = new ToolBar();
        Button saveButton = new Button(widgetProperty.getLanguageValue("Debra_MenuItem_Save"));
		saveButton.setId(NOTEPAD_SAVE);
		saveButton.setOnAction(this);
		tb.getItems().add(saveButton);
		return tb;*/
        return null;
    }

    @Override
    public void handle(ActionEvent t) {
        if (t.getSource() instanceof Styleable) {
            if (((Styleable) (t.getSource())).getId().equals(NOTEPAD_SAVE)) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showSaveDialog(Debra.primaryStage);
            }
        }
    }

}
