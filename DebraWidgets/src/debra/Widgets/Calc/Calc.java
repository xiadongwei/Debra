/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Widgets.Calc;

import debra.Framework.Interface.DebraWidget;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author user
 */
public class Calc extends DebraWidget {
    private BorderPane bp;

    @Override
    public Pane getWidget() {
        if (bp != null) {
            return bp;
        }
        Pane pane = null;
        bp = new BorderPane();
        ScrollPane sp = new ScrollPane();
        try {
            FXMLLoader fxl = new FXMLLoader(getClass().getResource("Calc.fxml"));
            fxl.setController(new CalcController());
            pane = fxl.load();
        } catch (IOException ex) {
            Logger.getLogger(Calc.class.getName()).log(Level.SEVERE, null, ex);
        }
        sp.setContent(pane);
        bp.setCenter(sp);
        return bp;
    }

    @Override
    public boolean refresh() {
        return true;
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
