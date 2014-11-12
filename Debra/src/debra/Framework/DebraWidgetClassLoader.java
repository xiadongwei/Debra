/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Framework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author tony
 */
public class DebraWidgetClassLoader extends java.net.URLClassLoader {

	private String filterPackageName = "debra.widgets";

	public DebraWidgetClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public DebraWidgetClassLoader(URL[] urls) {
		super(urls);
	}

	public DebraWidgetClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	public String getFilterPackageName() {
		return filterPackageName;
	}

	public void setFilterPackageName(String filterPackageName) {
		this.filterPackageName = filterPackageName;
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if (name.length() < filterPackageName.length() || !name.substring(0, 13).equalsIgnoreCase(filterPackageName)) {
			return super.loadClass(name);
		}
		try {
			URL[] myUrl = this.getURLs();
			StringBuilder fullURL = new StringBuilder(myUrl[0].getProtocol()).append("://").append(myUrl[0].getAuthority());
			fullURL.append(myUrl[0].getPath()).append("/").append(name.replaceAll("\\.", "/")).append(".class");
			java.net.URLConnection connection = new java.net.URL(fullURL.toString()).openConnection();
			InputStream input = connection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = input.read();
			while (data != -1) {
				buffer.write(data);
				data = input.read();
			}
			input.close();
			byte[] classData = buffer.toByteArray();
			return defineClass(name, classData, 0, classData.length);
		}catch (IOException| ClassFormatError e) {
			Logger.getLogger(DebraWidgetClassLoader.class.getName()).log(Level.OFF, "Can not load class", e);
		}
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		if (name.length() < filterPackageName.length() || !name.substring(0, 13).replaceAll("/", ".").replaceAll("\\\\", ".").equalsIgnoreCase(filterPackageName)) {
			return super.getResourceAsStream(name);
		}
		try {
			URL[] myUrl = this.getURLs();
			StringBuilder fullURL = new StringBuilder(myUrl[0].getProtocol()).append("://").append(myUrl[0].getAuthority());
			fullURL.append(myUrl[0].getPath()).append("/").append(name);
			URLConnection connection = new URL(fullURL.toString()).openConnection();
			return connection.getInputStream();
		} catch (IOException ex) {
			return null;
		}
	}

	@Override
	public URL getResource(String name) {
		if (name.length() < filterPackageName.length() || !name.substring(0, 13).replaceAll("/", ".").replaceAll("\\\\", ".").equalsIgnoreCase(filterPackageName)) {
			return super.getResource(name);
		}
		try {
			URL[] myUrl = this.getURLs();
			StringBuilder fullURL = new StringBuilder(myUrl[0].getProtocol()).append("://").append(myUrl[0].getAuthority());
			fullURL.append(myUrl[0].getPath()).append("/").append(name);
			return (new URL(fullURL.toString()));
		} catch (MalformedURLException ex) {
			return null;
		}
	}

}
