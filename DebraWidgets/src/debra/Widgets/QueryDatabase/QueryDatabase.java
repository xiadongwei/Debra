/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Widgets.QueryDatabase;

import debra.Framework.DebraProperty;
import debra.Framework.FrameworkFactory;
import debra.Framework.Interface.DebraWidget;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by xiadongwei on 14-1-13.
 */
public class QueryDatabase extends DebraWidget implements Initializable, EventHandler<ActionEvent> {
    @FXML
    private TextArea sqlTextArea;
    @FXML
    private Label queryLabel;
    private Button runButton;
    private Tooltip runButtonTooltip;
    private ToolBar tb;
    private BorderPane bp;

    @Override
    public Node getWidget() {
        if (bp != null) {
            return bp;
        }
        Pane pane = null;
        bp = new BorderPane();
        ScrollPane sp = new ScrollPane();
        try {
            FXMLLoader fxl = new FXMLLoader(getClass().getResource("QueryDatabase.fxml"));
            fxl.setController(this);
            pane = fxl.load();
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        sp.setContent(pane);
        bp.setCenter(sp);
        return bp;
    }

    @Override
    public boolean refresh() {
        queryLabel.setText(widgetProperty.getLanguageValue("QueryDatabase_Label_SQLQuery"));
        runButtonTooltip.setText(widgetProperty.getLanguageValue("QueryDatabase_ToolBar_Run"));
        return true;
    }

    @Override
    public MenuBar getMenuBar() {
        return null;
    }

    @Override
    public ToolBar getCustomToolBar() {
        if (tb == null) {
            tb = new ToolBar();
            runButton = new Button();
            runButtonTooltip = new Tooltip();
            runButtonTooltip.setText(widgetProperty.getLanguageValue("QueryDatabase_ToolBar_Run"));
            runButton.setTooltip(runButtonTooltip);
            ImageView runButtonImg = new ImageView(new Image(getClass().getResourceAsStream("Resources/run.png")));
            runButtonImg.setFitHeight(22);
            runButtonImg.setFitWidth(22);
            runButton.setGraphic(runButtonImg);
            getEventHelper().addEventHandler(runButton, this);
            tb.getItems().add(runButton);
        }
        return tb;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public void handle(ActionEvent t) {
        FrameworkFactory.getInstance().setBottomMessage(DebraProperty.PROCESS, "Processing");

        if (t.getSource() instanceof Button && !sqlTextArea.getText().trim().equals("")) {
            try {
                QueryDatabaseResult queryDatabaseResult = new QueryDatabaseResult();
                queryDatabaseResult.setClassURL(getClassURL());
                queryDatabaseResult.setSql(sqlTextArea.getText().trim());
                FrameworkFactory.getInstance().actionProcess(queryDatabaseResult);
            } catch (Exception e) {
                FrameworkFactory.getInstance().setBottomMessage(DebraProperty.ERROR, " SQl Failed");
                e.printStackTrace();
            } finally {
                this.releaseDebraTransfer();
            }
        }
        /* xml
        if (t.getSource() instanceof Button && !sqlTextArea.getText().trim().equals("")) {
		String sql = sqlTextArea.getText().trim();
		String type = "EXECUTE";
		if (sql.substring(0, 6).toUpperCase().equals("SELECT")||sql.substring(0, 4).toUpperCase().equals("SHOW")) type = "QUERY";
		getXMLDebraTransfer().addSendAttribute("SQL", sqlTextArea.getText().trim());
		getXMLDebraTransfer().addSendAttribute("TYPE", type);
		setResponseClass(QueryDatabaseAction.class, DebraProtocolEnum.XML);
		try {
				getXMLDebraTransfer().send();
			getXMLDebraTransfer().receive();
			QueryDatabaseResult queryDatabaseResult = new QueryDatabaseResult();
			queryDatabaseResult.setDebraTransfer(getXMLDebraTransfer());
			FrameworkFactory.getInstance().pushWidget(queryDatabaseResult);
		} catch (IOException e) {
				e.printStackTrace();
			}finally {
				release();
			}
	}
		*/
    }
}
