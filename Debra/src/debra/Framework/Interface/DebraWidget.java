/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework.Interface;

import debra.Framework.DebraProperty;
import debra.Framework.FrameworkFactory;
import debra.Framework.Tools.EventHelper;
import debra.Framework.Transfer.DebraProtocolEnum;
import debra.Framework.Transfer.DebraTransfer;
import debra.Framework.Transfer.JSON.JSONObject;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import org.dom4j.Document;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author user
 */

public abstract class DebraWidget {
	public static String loginID = "0";
	public static String loginName;
	public static String passwordMD5;
	protected DebraProtocolEnum protocol = DebraProtocolEnum.JSON;
	protected DebraProperty widgetProperty;
	protected String classURL;
	private EventHelper eventHelper = null;
	private DebraTransfer debraTransfer;

	public DebraWidget() {
		try {
			widgetProperty = DebraProperty.newInstance();
			InputStream resourceFile = this.getClass().getResourceAsStream(widgetProperty.RESOURCE_FILE);
			if (resourceFile != null) widgetProperty.initLanguageMap(resourceFile);
		} catch (org.dom4j.DocumentException e) {
			Logger.getLogger(DebraWidget.class.getName()).log(Level.OFF, "Can not load " + this.getClass().getName() + " DebraResource.xml", e);
		}
	}

	public DebraTransfer<Document> getXMLDebraTransfer() {
		setProtocol(DebraProtocolEnum.XML);
		return getDebraTransfer();
	}

	public DebraTransfer<JSONObject> getJSONDebraTransfer() {
		setProtocol(DebraProtocolEnum.JSON);
		return getDebraTransfer();
	}

	public DebraTransfer newDebraTransfer(DebraProtocolEnum protocol) {
		try {
			if (debraTransfer != null) debraTransfer.release();
			this.protocol = protocol;
			if (protocol.equals(DebraProtocolEnum.XML)) {
				debraTransfer = new debra.Framework.Transfer.XML.DebraClientTransfer<Document>().setIDAndClassURL(loginID, getClassURL());
			} else {
				debraTransfer = new debra.Framework.Transfer.JSON.DebraClientTransfer<JSONObject>().setIDAndClassURL(loginID, getClassURL());
			}
		} catch (IOException e) {
			FrameworkFactory.getInstance().setBottomMessage(DebraProperty.ERROR, "Can't connect server");
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't connect Server", e);
		} finally {
			return debraTransfer;
		}
	}

	private DebraTransfer getDebraTransfer() {
		if (debraTransfer == null) {
			debraTransfer = newDebraTransfer(protocol);
		} else if (debraTransfer.getConnection() == null && debraTransfer.isRelease()) {
			debraTransfer = newDebraTransfer(protocol);
		}
		return debraTransfer;
	}

	public void setDebraTransfer(DebraTransfer debraTransfer) {
		this.debraTransfer = debraTransfer;
	}

	private DebraProtocolEnum getProtocol() {
		return protocol;
	}

	private void setProtocol(DebraProtocolEnum protocol) {
		this.protocol = protocol;
	}

	protected void setResponseClass(Class respClass, DebraProtocolEnum protocol) {
		if (protocol.equals(DebraProtocolEnum.XML)) {
			getXMLDebraTransfer().addSendAttribute("ResponseCLS", respClass.getName().trim());
		} else getJSONDebraTransfer().addSendAttribute("ResponseCLS", respClass.getName().trim());
	}
	protected EventHelper getEventHelper() {
		if (eventHelper == null) {
			eventHelper = new EventHelper();
		}
		return eventHelper;
	}
	public String getClassURL() {
		if (classURL == null) {
			classURL = "";
		}
		return classURL;
	}

	public void setClassURL(String classURL) {
		this.classURL = classURL;
	}

	public abstract javafx.scene.Node getWidget();

	public abstract boolean refresh();

	public abstract MenuBar getMenuBar();

	public abstract ToolBar getCustomToolBar();

	public String getWidgetName() {
		return getClass().getName();
	}

	public void releaseEventHelper(){
		if (eventHelper != null) {
			eventHelper.release();
			eventHelper=null;
		}
	}

	public void releaseDebraTransfer(){
		if (debraTransfer != null) {
			debraTransfer.release();
			debraTransfer=null;
		}
	}
	public void release(){
		releaseDebraTransfer();
		releaseEventHelper();
	}
}