/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework;

import debra.Debra;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by xiadongwei on 14-1-26.
 */
public class HelperWindow extends Stage {
	public static HelperWindow selfStage;
	TextArea helperText;

	public HelperWindow() {
		BorderPane helperPane = new BorderPane();
		helperText = new TextArea();
		helperText.setEditable(false);
		helperPane.setCenter(helperText);
		Scene helperScene = new Scene(helperPane, 450, 250);
		this.initOwner(Debra.primaryStage);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(true);
		this.setScene(helperScene);
	}

	public static HelperWindow getInstance(String message) {
		if (selfStage == null) {
			selfStage = new HelperWindow();
		}
		selfStage.setMessage(message);
		selfStage.show();
		return selfStage;
	}

	public void setMessage(String message) {
		helperText.setText(message);
	}
}
