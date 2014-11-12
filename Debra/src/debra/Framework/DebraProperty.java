/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author user
 */
public class DebraProperty {
	public final static String DEBRA_CLOSE = "Debra_Menu_Close"; //Close MenuItem's setID this values
	public final static String DEBRA_REFRESH = "Debra_Menu_Refresh"; //Refresh MenuItem's setID this values
	public final static String DEBRA_ABOUT = "Debra_Menu_About"; //About MenuItem's setID this values
	public final static String DEBRA_ACTION_FIELD = "Debra_Action_Field"; //ActionField's setID this values
	public final static String DEBRA_ACTION_BUTTON = "Debra_Action_Button"; //ActionButton setID this values
	public final static String DEBRA_BOTTOM_LABEL = "Debra_Bottom_Label";//BottomLabel setID this values
	public final static String DEBRA_BACK_BUTTON = "Debra_Back_Button";//BackButton's setID this values
	public final static int MESSAGE = 1;
	public final static int ALERT = 2;
	public final static int ERROR = 3;
	public final static int PROCESS = 4;
	public static DebraProperty debraProperty;
	public static HashMap<String, DebraActionsProperty> actionsMap = new HashMap();
	public static String debraLanguage = "English";
	public final String RESOURCE_FILE = "DebraResource.xml"; //Action setup File
public HashMap<String, String> languageMap = new HashMap();

	public static DebraProperty newInstance() {
		debraProperty = new DebraProperty();
		return debraProperty;
	}

	public static void initActionProperty(String xml) {
		clearActionProperty();
		try {
			SAXReader reader = new SAXReader();
			Document document = DocumentHelper.parseText(xml);
			Node root = document.selectSingleNode("/DebraResponse/Actions");
			List list = root.selectNodes("Action");
			for (Object o : list) {
				Element e = (Element) o;
				DebraActionsProperty dap = new DebraActionsProperty();
				dap.setActionClass(e.attributeValue("cls"));
				actionsMap.put(e.attributeValue("id"), dap);
			}
		} catch (DocumentException ex) {
			Logger.getLogger(DebraProperty.class.getName()).log(Level.OFF, "Can not open XML", ex);
		}
	}

	public static void clearActionProperty() {
		actionsMap.clear();
	}

	public static String getActionClass(String id) {
		return actionsMap.get(id) != null ? actionsMap.get(id).getActionClass() : null;
	}

	public String getContextURL() {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(getClass().getResourceAsStream(RESOURCE_FILE));
			Node root = document.selectSingleNode("/DebraResource");
			List list = root.selectNodes("ContextURL");
			for (Object o : list) {
				Element e = (Element) o;
				return e.attributeValue("value");
			}
		} catch (DocumentException ex) {
			Logger.getLogger(DebraProperty.class.getName()).log(Level.WARNING, "DebraResource.xml can not find \"ContextURL\"", ex);
		}
		return "";
	}

	public void initLanguageMap() throws DocumentException {
		initLanguageMap(getClass().getResourceAsStream(RESOURCE_FILE));
	}

	public void initLanguageMap(InputStream debraPropertyFile) throws DocumentException {
		languageMap.clear();
		SAXReader reader = new SAXReader();
		Document document = reader.read(debraPropertyFile);
		Node root = document.selectSingleNode("/DebraResource/LanguageMap/" + debraLanguage);
// 读取DebraResource中的默认语言配置
//            Node root = document.selectSingleNode("/DebraResource/LanguageMap");

//          list = root.selectNodes("Language");
//
//            for (Object o : list) {
//
//                Element e = (Element) o;
//
//                debraLanguage = e.attributeValue("value");
//            }
		List list = root.selectNodes("Map");
		for (Object o : list) {
			Element e = (Element) o;
			languageMap.put(e.attributeValue("id"), e.attributeValue("value"));
		}
	}

	public DebraProperty setDefaultLanguage(String language) {
		if (language != null && !language.equals("")) {
			debraLanguage = language;
		}
		return this;
	}

	public String getDefaultLanguage() {
		if (debraLanguage == null) {
			return "English";
		}
		return debraLanguage;
	}

	public String getLanguageValue(String id) {
		return (languageMap.get(id) != null)
		    ? languageMap.get(id)
		    : "NoSet";
	}

	static class DebraActionsProperty {
		private String actionClass;

		public String getActionClass() {
			return actionClass;
		}

		public void setActionClass(String aClass) {
			actionClass = aClass;
		}

		@Override
		public String toString() {
			return actionClass;
		}
	}
}
