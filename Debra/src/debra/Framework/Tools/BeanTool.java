/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Framework.Tools;

import debra.Framework.Interface.DebraDao;

import java.lang.reflect.Method;

/**
 * @author user
 */
public class BeanTool {

	private DebraDao bean;

	public BeanTool(Object bean) {
		this.bean = (DebraDao) bean;
	}

	public String getElementName() {
		return bean.getDaoName();
	}

	public Method[] getGetMethods() {
		Method[] method = bean.getClass().getMethods();
		Method[] resultMethod = new Method[method.length];
		for (int i = 0, j = 0; i < method.length; i++) {
			if (method[i].getName().substring(0, 3).toLowerCase().equals("get")
			    && !method[i].getName().equals("getBeanName")
			    && !method[i].getName().equals("getColumnCount")
			    && !method[i].getName().equals("getClass")) {
				resultMethod[j++] = method[i];
			}
		}
		return resultMethod;
	}

	public Method[] getSetMethods() {
		Method[] method = bean.getClass().getMethods();
		Method[] resultMethod = new Method[method.length];
		for (int i = 0, j = 0; i < method.length; i++) {
			if (method[i].getName().substring(0, 3).toLowerCase().equals("set")
			    && !method[i].getName().equals("setBeanName")
			    && !method[i].getName().equals("setColumnCount")) {
				resultMethod[j++] = method[i];
			}
		}
		return resultMethod;
	}
}
