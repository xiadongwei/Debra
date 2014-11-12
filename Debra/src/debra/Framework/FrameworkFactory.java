/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework;

import debra.Framework.CheckPurview.CheckPurviewAction;
import debra.Framework.Interface.DebraWidget;
import debra.Framework.Login.LoginWidget;
import debra.Framework.Transfer.DebraProtocolEnum;
import debra.Framework.Transfer.DebraTransfer;
import debra.Framework.Transfer.JSON.DebraClientTransfer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.concurrent.Task;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author user
 */
public class FrameworkFactory extends DebraWidget {
	public final static String VERSION = "0.2.5";
	public final static String LOGIN_CMD = "login";
	public final static String FRAMEWORK_NAME = "Debra GUI";
	public static FrameworkFactory rootPaneFactory;
	public static DebraWidget debraWidget;
	public static DebraProperty debraProperty;
	private BorderPane rootPane;
	private MenuBar mb;
	private Menu fileMenu,helpMenu;
	private MenuItem refreshMItem,closeMItem,aboutMItem;
	private VBox topPane;
	private HBox bottomPane;
	private ToolBar tb;
	private TextField actionField;
	private Button actionButton;
	private Button backButton;
	private Node mainPane;
	private AnchorPane background;
	private Image backImage, messageImage, alertImage, errorImage, processImage;
	private ImageView backImageView, msgImageView;
	private Label bottomLabel;
	private Task refreshMessageTask;
	private ProgressIndicator progressInd;
	final String greenBottomCss = "-fx-border-color:rgba(105,176,195,1);"
		+ "-fx-border-radius: 5;"
		+ "-fx-background-color:olivedrab;";
	final String yellowBottomCss = "-fx-border-color:rgba(105,176,195,1);"
		+ "-fx-border-radius: 5;"
		+ "-fx-background-color:goldenrod;";
	final String redBottomCss = "-fx-border-color:rgba(105,176,195,1);"
		+ "-fx-border-radius: 5;"
		+ "-fx-background-color:crimson;";
	final String defaultBottomCss = "-fx-border-color:rgba(105,176,195,1);"
		+ "-fx-border-radius: 5;"
		+ "-fx-background-color:rgb(70, 70, 70);";
	public static FrameworkFactory getInstance() {
		if (rootPaneFactory == null) {
			rootPaneFactory = new FrameworkFactory();
			debraProperty = DebraProperty.newInstance().setDefaultLanguage("English");
			DebraTransfer.ContextUrl = debraProperty.getContextURL();
			LocalActionBuilder.build();
			try {
				debraProperty.initLanguageMap();
			} catch (org.dom4j.DocumentException ex) {
				Logger.getLogger(DebraProperty.class.getName()).log(Level.WARNING, "FrameworkFactory getInstance() XML format err", ex);
			}
		}
		return rootPaneFactory;
	}

	public Pane initRootPane() {
		if (rootPane != null) {
			return rootPane;
		}
		rootPane = new BorderPane();
		initTopBar();
		initCenterBar();
		initBottomBar();
		debraWidget = this;
		return rootPane;
	}

	public void initTopBar() {

//            setBannerBar(getBannerBar());
		setMenuBar(getMenuBar());
		setToolBar(getToolBar());
		setCustomToolBar(getCustomToolBar());
	}

	@Override
	public MenuBar getMenuBar() {
		if (mb == null) {
			mb = new MenuBar();
//      add File Menu
			fileMenu = new Menu(debraProperty.getLanguageValue("Debra_Menu_File"));
			refreshMItem = new MenuItem(debraProperty.getLanguageValue("Debra_MenuItem_Refresh"));
			refreshMItem.setId(DebraProperty.DEBRA_REFRESH);
			refreshMItem.addEventHandler(ActionEvent.ACTION,DebraEvent.getInstance());
			fileMenu.getItems().add(refreshMItem);
			closeMItem = new MenuItem(debraProperty.getLanguageValue("Debra_MenuItem_Close"));
			closeMItem.setId(DebraProperty.DEBRA_CLOSE);
			closeMItem.addEventHandler(ActionEvent.ACTION, DebraEvent.getInstance());
			fileMenu.getItems().add(closeMItem);
			mb.getMenus().add(fileMenu);

//      add Help Menu
			helpMenu = new Menu(debraProperty.getLanguageValue("Debra_Menu_Help"));
			aboutMItem = new MenuItem(debraProperty.getLanguageValue("Debra_MenuItem_About"));
			aboutMItem.setId(DebraProperty.DEBRA_ABOUT);
			aboutMItem.addEventHandler(ActionEvent.ACTION, DebraEvent.getInstance());
			helpMenu.getItems().add(aboutMItem);
			mb.getMenus().add(helpMenu);
		}
		return mb;
	}

	/*
		public void setBannerBar(ToolBar bannerBar){
		if (bannerBar!=null){
				if (topPane == null) {
				topPane = new VBox();
				rootPane.setTop(topPane);
				}
				if (topPane.getChildren().size() > 0) {
				topPane.getChildren().remove(0);
				}
				topPane.getChildren().add(0, bannerBar);
			}else {
				setBannerBar(getBannerBar());
			}
		}
		 */
	public void setMenuBar(MenuBar menuBar) {
		if (menuBar != null) {
			if (topPane == null) {
				topPane = new VBox();
				rootPane.setTop(topPane);
			}
			if (topPane.getChildren().size() > 0) {
				topPane.getChildren().remove(0);
			}
			topPane.getChildren().add(0, menuBar);
		} else {   //if widget getMenuBar == null
			setMenuBar(getMenuBar());
		}
	}

	public ToolBar getToolBar() {
		if (tb == null) {
			tb = new ToolBar();
		} else {
			return tb;
		}
		actionField = getActionField();
		actionButton = getActionButton();
		backButton = getBackButton();
		tb.getItems().add(backButton);
		tb.getItems().add(actionField);
		tb.getItems().add(actionButton);
		return tb;
	}

	private void setToolBar(ToolBar toolBar) {
		if (toolBar != null) {
			if (topPane == null) {
				topPane = new VBox();
				rootPane.setTop(topPane);
			}
			if (topPane.getChildren().size() > 1) {
				topPane.getChildren().remove(1);
			}
			topPane.getChildren().add(1, toolBar);
		} else {
			setToolBar(getToolBar());
		}
	}

	/*
		public ToolBar getBannerBar(){
			 if (bb == null) {
			bb =  new ToolBar();
			} else {
			return bb;
			}
			bb.setId("mainBannerBar");
		return bb;
		}*/
	public TextField getActionField() {
		if (actionField == null) {
			actionField = new TextField();
			actionField.setId(DebraProperty.DEBRA_ACTION_FIELD);
		}
		return actionField;
	}

	public Button getActionButton() {
		if (actionButton == null) {
			actionButton = new Button();
			Tooltip tooltip = new Tooltip();
			tooltip.setText(debraProperty.getLanguageValue("Debra_ToolBar_ActionButton"));
			actionButton.setTooltip(tooltip);
			actionButton.setId(DebraProperty.DEBRA_ACTION_BUTTON);
			ImageView actionButtonImg = new ImageView(new Image(getClass().getResourceAsStream("Resources/ActionButton.png")));
			actionButtonImg.setFitHeight(22);
			actionButtonImg.setFitWidth(22);
			actionButton.setGraphic(actionButtonImg);
		}
		return actionButton;
	}

	public void initCenterBar() {
		setMainPane(getBackground());
	}

	private void initBottomBar() {
		rootPane.setBottom(getBottomPane());
		setBottomMessage(debraProperty.MESSAGE, this.getWidgetName() + debraProperty.getLanguageValue("Debra_Widget_Loaded_Message"));
	}

	private Pane getBottomPane() {
		if (bottomPane == null) {
			bottomPane = new HBox();
			bottomPane.getChildren().add(getMessageLabel());
			bottomPane.setStyle(defaultBottomCss);
		}
		return bottomPane;
	}

	public Label getMessageLabel() {
		if (bottomLabel == null) {
			bottomLabel = new Label("");
			bottomLabel.setStyle("-fx-text-fill: rgb(186,201,201);");
			bottomLabel.setId(DebraProperty.DEBRA_BOTTOM_LABEL);
			bottomLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					if (mouseEvent.getClickCount() == 2) {
						HelperWindow.getInstance(bottomLabel.getText());
					}
				}
			});
		}
		return bottomLabel;
	}
	public void setBottomMessage(int type, String msg) {
		if (msgImageView == null) {
			msgImageView = new ImageView();
		}
		if (msg == null) {
			msg = "Processing";
		}
		if (type == DebraProperty.PROCESS) {
			if (processImage == null) {
				processImage = new Image(getClass().getResourceAsStream("Resources/Process.gif"));
			}
			msgImageView.setImage(processImage);
			bottomLabel.setGraphic(msgImageView);
			getBottomPane().setStyle(defaultBottomCss);
		} else if (type == DebraProperty.MESSAGE) {
			if (messageImage == null) {
				messageImage = new Image(getClass().getResourceAsStream("Resources/Message.png"));
			}
			msgImageView.setImage(messageImage);
			bottomLabel.setGraphic(msgImageView);
			getBottomPane().setStyle(greenBottomCss);
			Logger.getLogger(this.getClass().getName()).log(Level.INFO, msg);
		} else if (type == DebraProperty.ALERT) {
			if (alertImage == null) {
				alertImage = new Image(getClass().getResourceAsStream("Resources/Alert.png"));
			}
			msgImageView.setImage(alertImage);
			bottomLabel.setGraphic(msgImageView);
			getBottomPane().setStyle(yellowBottomCss);
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, msg);
		} else if (type == DebraProperty.ERROR) {
			if (errorImage == null) {
				errorImage = new Image(getClass().getResourceAsStream("Resources/Error.png"));
			}
			msgImageView.setImage(errorImage);
			bottomLabel.setGraphic(msgImageView);
			getBottomPane().setStyle(redBottomCss);
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, msg);
		}


		Platform.runLater(() -> {
			refreshMessageTask = new RefreshMessageThread();
			msgImageView.fitHeightProperty().unbind();
			msgImageView.fitHeightProperty().bind(refreshMessageTask.workDoneProperty());
			msgImageView.fitWidthProperty().unbind();
			msgImageView.fitWidthProperty().bind(refreshMessageTask.workDoneProperty());
			Thread refreshMessageThread = new Thread(refreshMessageTask);
			refreshMessageThread.setName("refreshMessageThread");
			refreshMessageThread.start();
		});
		bottomLabel.setText(msg);
	}

	public Node getMainPane() {
		return mainPane;
	}

	public void setMainPane(Node pane) {
		this.mainPane = pane;
		rootPane.setCenter(this.mainPane);
	}

	public Pane getBackground() {
		if (backImageView == null) {
			if (backImage == null) {
				backImage = new Image(getClass().getResourceAsStream("Resources/DebraWorker.png"));
			}
			backImageView = new ImageView(backImage);
		}
		if (background == null) {
			background = new AnchorPane();
			AnchorPane.setRightAnchor(backImageView, 30.0d);
			AnchorPane.setTopAnchor(backImageView, 20.0d);
			background.getChildren().add(backImageView);
			background.getStyleClass().add("background");
		}
		return background;
	}

	public Button getBackButton() {
		if (backButton == null) {
			backButton = new Button();
			Tooltip tooltip = new Tooltip();
			tooltip.setText(debraProperty.getLanguageValue("Debra_ToolBar_BackButton"));
			backButton.setTooltip(tooltip);
			backButton.setId(DebraProperty.DEBRA_BACK_BUTTON);
			ImageView backButtonImg = new ImageView(new Image(getClass().getResourceAsStream("Resources/BackButton.png")));
			backButtonImg.setFitHeight(22);
			backButtonImg.setFitWidth(22);
			backButton.setGraphic(backButtonImg);
		}
		return backButton;
	}

	@Override
	public Pane getWidget() {
		return getBackground();
	}

	@Override
	public boolean refresh() {
		return true;
	}

	@Override
	public ToolBar getCustomToolBar() {
		return null;
	}

	public void setCustomToolBar(ToolBar customToolBar) {
		if (customToolBar != null) {
			if (topPane == null) {
				topPane = new VBox();
				rootPane.setTop(topPane);
			}
			if (topPane.getChildren().size() > 2) {
				topPane.getChildren().remove(2);
			}
			topPane.getChildren().add(2, customToolBar);
		} else {  //if widget geCustomToolBar == null
			if (topPane.getChildren().size() > 2) {
				topPane.getChildren().remove(2);
			}
		}
	}

	@Override
	public String getWidgetName() {
		return FRAMEWORK_NAME;
	}

	public void actionBack() {
		popWidget();
		System.gc();
	}

	private DebraWidget popWidget() {
		DebraWidget dw = WidgetStack.getInstance().pop();
		if (dw != null) {
			FrameworkFactory.debraWidget.release();
			FrameworkFactory.debraWidget = dw;
			FrameworkFactory.getInstance().loadWidget(dw);
		}
		return dw;
	}

	public void actionProcess(String cmd) {
		DebraWidget dWidget = null;
		if (cmd.substring(0, 2).equalsIgnoreCase("/o")) {
			cmd = cmd.substring(2).trim();
			try {
				Runtime.getRuntime().exec("java -jar Debra.jar " + DebraClientTransfer.ContextUrl + " " + DebraWidget.loginName + " " + DebraWidget.passwordMD5 + " " + cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			dWidget = FrameworkFactory.getInstance().loadClass(cmd);
			if (dWidget != null) {
				WidgetStack.getInstance().clear();
				FrameworkFactory.debraWidget = FrameworkFactory.getInstance();
				pushWidget(dWidget);
				if (dWidget.getClass().getName().equalsIgnoreCase(LoginWidget.class.getName())) {   //if LoginWidget
					WidgetStack.getInstance().clear();
					FrameworkFactory.debraWidget = FrameworkFactory.getInstance();
				}
			} else {
				if (DebraWidget.loginID.equals("0")) {
					FrameworkFactory.getInstance().setBottomMessage(debraProperty.ERROR, debraProperty.getLanguageValue("Debra_Widget_NotLogin_Message"));
				}
			}
		}
	}

	public void actionProcess(DebraWidget dWidget) {
		pushWidget(dWidget);
	}
	private void pushWidget(DebraWidget dWidget) {
		WidgetStack.getInstance().push(FrameworkFactory.debraWidget);
		FrameworkFactory.getInstance().loadWidget(dWidget);
		FrameworkFactory.debraWidget = dWidget;
	}

	public DebraWidget loadClass(String cmd) {
		DebraWidget dWidget = null;
		FrameworkFactory.getInstance().setBottomMessage(debraProperty.PROCESS, "Loading " + cmd);
		try {
			if (DebraProperty.getActionClass(cmd) != null) {
				setResponseClass(CheckPurviewAction.class, DebraProtocolEnum.JSON);
				getJSONDebraTransfer().addSendAttribute("ActionCLS", DebraProperty.getActionClass(cmd));
				getJSONDebraTransfer().send();
				getJSONDebraTransfer().receive();
				String actionURL = (String) getJSONDebraTransfer().getReceiveAttribute("ActionURL");
				boolean isThrow = getJSONDebraTransfer().isThrow();
				releaseDebraTransfer();
				if (!isThrow && actionURL != null && !actionURL.equals("")) {
					URL cmdURL = new URL(actionURL);
					DebraWidgetClassLoader debraCL = new DebraWidgetClassLoader(new URL[]{cmdURL}, Thread.currentThread().getContextClassLoader());
					Class debraWidgetClass = debraCL.loadClass(DebraProperty.getActionClass(cmd));
					if (debraWidgetClass != null) {
						dWidget = (DebraWidget) (debraWidgetClass.newInstance());
						dWidget.setClassURL(actionURL);
					}else {
						throw new ClassNotFoundException();
					}
				} else {
					FrameworkFactory.getInstance().setBottomMessage(debraProperty.ERROR, debraProperty.getLanguageValue("Debra_Widget_NoRight_Message") + cmd);
				}
			} else if (LocalActionBuilder.localActionMap.get(cmd) != null) {  //本地装载
				Class debraWidgetClass = Class.forName(LocalActionBuilder.localActionMap.get(cmd));
				dWidget = (DebraWidget) (debraWidgetClass.newInstance());
			} else {
				FrameworkFactory.getInstance().setBottomMessage(debraProperty.ERROR, debraProperty.getLanguageValue("Debra_Widget_NoLoad_Message") + cmd);
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException ex) {
			FrameworkFactory.getInstance().setBottomMessage(debraProperty.ERROR, debraProperty.getLanguageValue("Debra_Widget_NoLoad_Message") + cmd);
			Logger.getLogger(FrameworkFactory.class.getName()).log(Level.INFO, debraWidget.getWidgetName() + debraProperty.getLanguageValue("Debra_Widget_Loaded_Message"));
		}
		return dWidget;
	}

	public void loadWidget(DebraWidget debraWidget) {
		FrameworkFactory.getInstance().setMainPane(debraWidget.getWidget());
		FrameworkFactory.getInstance().setCustomToolBar(debraWidget.getCustomToolBar());
		FrameworkFactory.getInstance().setMenuBar(debraWidget.getMenuBar());
		FrameworkFactory.getInstance().setBottomMessage(debraProperty.MESSAGE, debraWidget.getWidgetName() + debraProperty.getLanguageValue("Debra_Widget_Loaded_Message"));
		if (debraWidget.refresh()) {
			Logger.getLogger(FrameworkFactory.class.getName()).log(Level.INFO, debraWidget.getWidgetName() + debraProperty.getLanguageValue("Debra_Widget_Loaded_Message"));
		} else {
			FrameworkFactory.getInstance().setBottomMessage(debraProperty.ERROR, debraProperty.getLanguageValue("Debra_Widget_NoLoad_Message"));
		}
	}

	static class LocalActionBuilder {
		private static HashMap<String, String> localActionMap = new HashMap<String, String>();

		public static HashMap build() {
			localActionMap.put(FrameworkFactory.LOGIN_CMD, LoginWidget.class.getName());
			return localActionMap;
		}
	}
	class RefreshMessageThread extends Task {
		@Override
		protected Object call() throws Exception {
			try {
				for (int i = 24; i <= 34; i = i + 10) {
					updateProgress(i, Integer.MAX_VALUE);
					Thread.sleep(20);
				}
				Thread.sleep(130);
				for (int i = 34; i >= 24; i = i - 5) {
					updateProgress(i, Integer.MAX_VALUE);
					Thread.sleep(30);
				}
			} catch (InterruptedException ex) {
				Logger.getLogger(FrameworkFactory.class.getName()).log(Level.SEVERE, null, ex);
			}
			Platform.runLater(() -> {
				getBottomPane().setStyle(defaultBottomCss);
			});

				return null;
			}
		}
}
