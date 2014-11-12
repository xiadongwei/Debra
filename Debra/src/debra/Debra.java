/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra;

import debra.Framework.DebraEvent;
import debra.Framework.FrameworkFactory;
import debra.Framework.Login.LoginWidget;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Tony Dw. Xia
 */
public class Debra extends Application {
	/**
	 * @param args the command line arguments
	 */
	public Pane rootPane;     
	public static Scene primaryScene;
	public static Stage primaryStage;
	public static TextField actionField;
	public static Button actionButton, backButton;

	public static void main(String[] args) {
		Debra debra = new Debra();
		debra.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

//        	this.primaryStage.initStyle(StageStyle.UNDECORATED);
		rootPane = FrameworkFactory.getInstance().initRootPane(); //Building main panel about menu's and toolbar
		initAction(FrameworkFactory.getInstance()); //initialize ActionField's action
		if (this.getParameters().getUnnamed().size() > 0) {
			String serverAddress = this.getParameters().getUnnamed().get(0);
			String loginName = this.getParameters().getUnnamed().get(1);
			String password = this.getParameters().getUnnamed().get(2);
			String action = this.getParameters().getUnnamed().get(3);
			new LoginWidget(serverAddress, loginName, password);
			FrameworkFactory.getInstance().actionProcess(action);
		} else FrameworkFactory.getInstance().loadWidget(new LoginWidget());
		primaryScene = new Scene(rootPane, 1024, 768);
		primaryScene.getStylesheets().add(getClass().getResource("Framework/Resources/DebraFramework.css").toExternalForm());
		primaryStage.setTitle(FrameworkFactory.getInstance().getWidgetName()); //Setup windows caption
		primaryStage.setScene(primaryScene);
		primaryStage.show();
	}

	private void initAction(FrameworkFactory instance) {
		actionField = instance.getActionField();
		actionButton = instance.getActionButton();
		backButton = instance.getBackButton();
		actionField.setOnAction(DebraEvent.getInstance());
		actionButton.setOnAction(DebraEvent.getInstance());
		backButton.setOnAction(DebraEvent.getInstance());
	}

}
