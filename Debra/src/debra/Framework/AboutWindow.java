/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Framework;

import debra.Debra;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author user
 */
public class AboutWindow extends Stage {
	public AboutWindow() {
		super(StageStyle.UTILITY);
		AnchorPane aboutPane = new AnchorPane();
		ImageView debraLogo = new ImageView(new Image(getClass().getResourceAsStream("Resources/DebraLogo.jpg")));
		debraLogo.setLayoutX(14.0d);
		debraLogo.setLayoutY(14.0d);
		debraLogo.setFitHeight(169.5d);
		debraLogo.setFitWidth(113.0d);
		VBox vb = new VBox();
		vb.setLayoutX(138.0d);
		vb.setLayoutY(23.0d);
		Label productName = new Label("Product Name : Debra Worker");
		Label version = new Label("Version : " + FrameworkFactory.VERSION);
		Label releaseDate = new Label("Release Date : Dec 25 2012");
		Label createBy = new Label("Create By : Tony Dw Xia");
		vb.getChildren().addAll(productName, version, releaseDate, createBy);
		Button closeButton = new Button(FrameworkFactory.debraProperty.getLanguageValue("Debra_MenuItem_Close"));
		AnchorPane.setRightAnchor(closeButton, 14.0d);
		AnchorPane.setBottomAnchor(closeButton, 14.0d);
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				((Stage) ((Button) t.getSource()).getParent().getScene().getWindow()).close();
			}
		});
		aboutPane.getChildren().addAll(debraLogo, vb, closeButton);
		Scene aboutScene = new Scene(aboutPane, 350, 200);
		this.initOwner(Debra.primaryStage);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);
		this.setScene(aboutScene);
	}
}
