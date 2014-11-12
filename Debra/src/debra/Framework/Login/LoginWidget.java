/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Framework.Login;

import debra.Framework.DebraProperty;
import debra.Framework.FrameworkFactory;
import debra.Framework.Interface.DebraWidget;
import debra.Framework.Tools.MD5Util;
import debra.Framework.Transfer.DebraProtocolEnum;
import debra.Framework.Transfer.DebraTransfer;
import debra.Framework.WidgetStack;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author user
 */
public class LoginWidget extends DebraWidget implements EventHandler<ActionEvent> {
	private ScrollPane sp;
	private BorderPane bp;
	private AnchorPane ap;
	private Label[] labNode = new Label[3];
	private TextField[] textNode = new TextField[3];
	private Label labServerAddress;
	private Label labLoginName;
	private Label labPassword;
	private TextField serverAddress;
	private TextField loginName;
	private PasswordField password;
	private Button loginButton;

	public LoginWidget() {
		super();
	}

	public LoginWidget(String AserverAddress, String AloginName, String Apassword) {
		super();
		if (serverAddress == null) {
			serverAddress = new TextField();
			serverAddress.setText(AserverAddress);
		}
		textNode[0] = serverAddress;
		if (loginName == null) {
			loginName = new TextField();
			loginName.setText(AloginName);
		}
		textNode[1] = loginName;
		if (password == null) {
			password = new PasswordField();
			password.setText(Apassword);
		}
		textNode[2] = password;
		try {
			login();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Pane getWidget() {
		WidgetStack.getInstance().clear();
		DebraWidget.loginID = "0";
		DebraProperty.clearActionProperty();
		if (bp != null) {
			return bp;
		}
		bp = new BorderPane();
		sp = new ScrollPane();
		ap = new AnchorPane();
		bp.setCenter(sp);
		sp.setContent(ap);
		labServerAddress = new Label(widgetProperty.getLanguageValue("Debra_Context"));
		labNode[0] = labServerAddress;
		labLoginName = new Label(widgetProperty.getLanguageValue("Debra_LoginName"));
		labNode[1] = labLoginName;
		labPassword = new Label(widgetProperty.getLanguageValue("Debra_LoginPassword"));
		labNode[2] = labPassword;
		if (serverAddress == null) {
			serverAddress = new TextField();
		}
		serverAddress.setId("serverAddress");
		serverAddress.setPromptText("Please input server context");
		getEventHelper().addEventHandler(serverAddress,this);
		serverAddress.setText(DebraTransfer.ContextUrl);//("http://127.0.0.1:8080/DebraWeb/DebraServlet.do");
		textNode[0] = serverAddress;
		if (loginName == null) {
			loginName = new TextField();
		}
		loginName.setId("loginName");
		loginName.setPromptText("Please input login name");
		getEventHelper().addEventHandler(loginName,this);
		textNode[1] = loginName;
		if (password == null) {
			password = new PasswordField();
		}
		password.setId("password");
		password.setPromptText("Please input password");
		getEventHelper().addEventHandler(password,this);
		textNode[2] = password;
//            ((PasswordField)textNode[2]).setText("                    ");
		for (int i = 0, x = 60, y = 60; i < 3; y = y + 60, i++) {
			labNode[i].setLayoutX(x);
			labNode[i].setLayoutY(y);
			labNode[i].setMaxSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		}
		for (int i = 0, x = 165, y = 60; i < 3; y = y + 60, i++) {
			textNode[i].setLayoutX(x);
			textNode[i].setLayoutY(y);
			textNode[i].setMaxSize(TextField.USE_PREF_SIZE, TextField.USE_PREF_SIZE);
		}
		loginButton = new Button(widgetProperty.getLanguageValue("Debra_LoginButton"));
		loginButton.setId("loginButton");
		loginButton.setLayoutX(240);
		loginButton.setLayoutY(240);
		getEventHelper().addEventHandler(loginButton,this);
		ap.getChildren().addAll(labServerAddress, labLoginName, labPassword, serverAddress, loginName, password, loginButton);
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

	@Override
	public void handle(ActionEvent t) {
		try {
			login();
		} catch (IOException ex) {
			FrameworkFactory.getInstance().setBottomMessage(DebraProperty.ERROR, "Can't connect server");
			Logger.getLogger(LoginWidget.class.getName()).log(Level.WARNING, "Can't connect Server", ex);
		} finally {
			release();
		}
	}

	private void login() throws IOException {
		for (int i = 2; i > 0; i--) {
			if (((TextField) textNode[i]).getText().equals("")) {
				textNode[i].requestFocus();
			}
		}
		if (!loginName.getText().equals("") && !password.getText().equals("") && !serverAddress.getText().equals("")) {
			this.setDebraTransfer(new debra.Framework.Transfer.XML.DebraClientTransfer(debra.Framework.Transfer.XML.DebraClientTransfer.urlToConnection(serverAddress.getText())).setIDAndClassURL(loginID, getClassURL()));
			this.setResponseClass(LoginAction.class, DebraProtocolEnum.XML);
			getXMLDebraTransfer().addSendAttribute("UserName", loginName.getText().trim());
			DebraWidget.loginName = loginName.getText().trim();
			if (bp == null) {
				DebraWidget.passwordMD5 = password.getText().trim();
			} else {
				DebraWidget.passwordMD5 = MD5Util.MD5(password.getText().trim());
			}
			getXMLDebraTransfer().addSendAttribute("UserPass", DebraWidget.passwordMD5);
			getXMLDebraTransfer().send();
			String responseXML = getXMLDebraTransfer().receiveToString();
			DebraWidget.loginID = (String) getXMLDebraTransfer().getReceiveAttribute("UserID");
			if (getXMLDebraTransfer().isThrow() || responseXML == null || responseXML.equals("") || FrameworkFactory.loginID == null || DebraWidget.loginID.equals("0")) {
				FrameworkFactory.getInstance().setBottomMessage(DebraProperty.ERROR, "Can not find " + loginName.getText() + " or password is invalid");
			} else {
				widgetProperty.initActionProperty(responseXML);
				FrameworkFactory.getInstance().setBottomMessage(DebraProperty.MESSAGE, loginName.getText() + " is logined");
				FrameworkFactory.getInstance().initCenterBar();
				FrameworkFactory.getInstance().getActionField().requestFocus();
			}
		}
	}
}
