/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework.Transfer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author tony
 */

public abstract class DebraTransfer<T> {
	public static boolean CIPHER_MODE = true;  //定义是否加密传输，服务器端以及客户端需要相同设置
	public static String ContextUrl = null;
	protected static String Algorithm = "DESede";//定义 加密算法,可用DES,DESede,Blowfish
	public HttpURLConnection urlConnection;
	protected InputStream inputStream;
	protected OutputStream outputStream;
	protected T sendDocument, receiveDocument; //定义模型对象，JSON模式采用Jackson JSON，XML模式采用DOM4J
	protected byte[] key = {
	    (byte) 0x11, (byte) 0x22, (byte) 0x4F, (byte) 0x58,
	    (byte) 0x88, (byte) 0x10, (byte) 0x40, (byte) 0x38,
	    (byte) 0x28, (byte) 0x25, (byte) 0x79, (byte) 0x51,
	    (byte) 0xCB, (byte) 0xDD, (byte) 0x55, (byte) 0x66,
	    (byte) 0x77, (byte) 0x29, (byte) 0x74, (byte) 0x98,
	    (byte) 0x30, (byte) 0x40, (byte) 0x36, (byte) 0xE2
	};
	protected Cipher encrypeCipher, decryptCipher;
	protected SecretKey deskey;

//	static {
//		Security.addProvider(new com.sun.crypto.provider.SunJCE());
//
//	}

	/**
	 * 获取HttpURLConnection对象，并发起连接
	 *
	 * @param contextUrl
	 * @return HttpURLConnection对象
	 * @throws IOException
	 */
	public static HttpURLConnection urlToConnection(String contextUrl) throws IOException {
		HttpURLConnection urlConnection;
		DebraTransfer.ContextUrl = contextUrl;
		if (contextUrl != null) {
			URL url = new URL(contextUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setUseCaches(false);
			return urlConnection;
		}
		return null;
	}

	/**
	 * 获取已设置的HttpURLConnection
	 *
	 * @return
	 */
	public HttpURLConnection getConnection() {
		return urlConnection;
	}

	/**
	 * 设置发送对象的键值对
	 *
	 * @param key
	 * @param value
	 */
	public abstract void addSendAttribute(String key, Object value);

	/**
	 * 获取发送对象的键值对
	 *
	 * @param key
	 * @return
	 */
	public abstract Object getSendAttribute(String key);

	/**
	 * 设置接收对象的键值对
	 *
	 * @param key
	 * @param value
	 */
	public abstract void addReceiveAttribute(String key, Object value);

	/**
	 * 获取接收对象的键值对
	 *
	 * @param key
	 * @return
	 */
	public abstract Object getReceiveAttribute(String key);

	/**
	 * 设置发送对象XPath路径
	 *
	 * @param nodePath
	 */
	public abstract void setNodeBySend(String nodePath);

	/**
	 * 设置接收对象XPath路径
	 *
	 * @param nodePath
	 */
	public abstract void setNodeByReceived(String nodePath);

	/**
	 * 发送数据
	 *
	 * @throws IOException
	 */
	public void send() throws IOException {
		sendByObject(getSendDocument());
	}

	/**
	 * 发送指定对象的数据
	 *
	 * @param document
	 * @throws IOException
	 */
	public abstract void sendByObject(T document) throws IOException;

	/**
	 * 发送字符串数据
	 *
	 * @param message
	 * @throws IOException
	 */
	public abstract void sendByString(String message) throws IOException;

	/**
	 * 接收数据到对象
	 *
	 * @return
	 * @throws IOException
	 */
	public T receive() throws IOException {
		receiveToString();
		disconnect();
		return getReceivedDocument();
	}

	/**
	 * 接收数据到默认对象
	 *
	 * @return
	 * @throws IOException
	 */
	public abstract String receiveToString() throws IOException;

	/**
	 * 接收指定流对象的数据
	 *
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	public abstract String receiveToString(InputStream inStream) throws IOException;

	/**
	 * 获取已接收的字符串数据
	 *
	 * @return
	 */
	public abstract String getReceivedString();

	/**
	 * 获取发送对象
	 *
	 * @return
	 */
	public abstract T getSendDocument();

	/**
	 * 设置发送对象
	 *
	 * @param sendDocument
	 */
	public abstract void setSendDocument(T sendDocument);

	/**
	 * 获取接收对象
	 *
	 * @return
	 */
	public abstract T getReceivedDocument();

	/**
	 * 判断接受对象中是否存在异常标记
	 *
	 * @return
	 */
	public boolean isThrow() {
		String s = ((String) getReceiveAttribute("throw"));
		if (s == null) s = "";
		if ("true".equalsIgnoreCase(s)) return true;
		return false;
	}

	/**
	 * 在发送对象中加入异常标记
	 */
	public void setThrow() {
		addSendAttribute("throw", "true");
	}

	/**
	 * 断开HttpURLConnection连接
	 */
	protected abstract void disconnect();

	/**
	 * 释放资源
	 */
	public abstract void release();

	/**
	 * 判断Receive 是否已经申请过
	 * @return
	 */
	public boolean isRelease(){
		if(receiveDocument==null){
			return true;
		}
		return false;
	}
}
