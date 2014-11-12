/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Framework.Transfer.XML;

import debra.Framework.Transfer.DebraProtocolEnum;
import debra.Framework.Transfer.DebraTransfer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tony Dw. Xia
 *         实现XML格式的客户端通讯管理
 *         负责DebraTransfer的接口实现
 */
public class DebraClientTransfer<T extends Document> extends DebraTransfer<T> {
	protected Element attributeRoot;
	public static String requestLabel = "DebraRequest";
	protected String sendNodePath, receiveNodePath;
	private String loginID, classURL;

	/**
	 * 构造DebraClientTransfer
	 *
	 * @param urlConnection 初始化HTTPURLConnection对象,可使用DebraTransfer.urlToConnection(String contextUrl)生成
	 */
	public DebraClientTransfer(HttpURLConnection urlConnection) {
		super();
		setConnection(urlConnection);
	}

	/**
	 * 默认构造构造DebraClientTransfer
	 * 请确认已经使用了setConnection(HttpURLConnection urlConnection)进行了连接的初始化
	 *
	 * @throws IOException
	 */
	public DebraClientTransfer() throws IOException {
		super();
		if (ContextUrl != null && !"".equals(ContextUrl)) {
			setConnection(DebraClientTransfer.urlToConnection(ContextUrl));
		}
	}

	/**
	 * 设置DebraClientTransfer的HttpURLConnection连接
	 * 此设置将完成DebraTransfer.ContextUrl的设置
	 *
	 * @param urlConnection 初始化HTTPURLConnection对象,可使用DebraTransfer.urlToConnection(String contextUrl)生成
	 */
	public void setConnection(HttpURLConnection urlConnection) {
		this.urlConnection = urlConnection;
		if (urlConnection != null) {
			urlConnection.setRequestProperty("PROTOCAL", DebraProtocolEnum.XML.name());
		}
	}

	/**
	 * 设置loginID以及装载类的URL位置
	 *
	 * @param loginID  用户登陆ID
	 * @param classURL 装载累URl路径
	 * @return
	 */
	public DebraTransfer setIDAndClassURL(String loginID, String classURL) {
		this.loginID = loginID;
		this.classURL = classURL;
		this.attributeRoot = null;
		if (sendDocument == null) getSendDocument();
		return this;
	}

	/**
	 * 实现DebraTransfer接口，实现org.dom4j.Document发送对象键值的设置，如XML=<DebraRequest key＝value />
	 *
	 * @param key
	 * @param value
	 */
	@Override
	public void addSendAttribute(String key, Object value) {
		List list = getSendDocument().getRootElement().selectNodes(sendNodePath);
		Iterator iter = list.iterator();
		if (iter.hasNext()) {
			Element element = (Element) iter.next();
			element.addAttribute(key, (String) value);
		}
	}

	/**
	 * 实现DebraTransfer接口，从org.dom4j.Document发送对象获取键的值，如XML=<DebraRequest key＝value />
	 *
	 * @param key
	 */
	@Override
	public Object getSendAttribute(String key) {
		String result = null;
		if (key == null || key.equals("") || sendDocument == null) {
			return "";
		}
		Element root = getSendDocument().getRootElement();
		List list = root.selectNodes(sendNodePath);
		Iterator iter = list.iterator();
		if (iter.hasNext()) {
			Element element = (Element) iter.next();
			result = element.attributeValue(key);
		}
		return result;
	}

	/**
	 * 实现DebraTransfer接口，实现org.dom4j.Document接收对象键值的设置，如XML=<DebraRequest key＝value />
	 *
	 * @param key
	 * @param value
	 */
	@Override
	public void addReceiveAttribute(String key, Object value) {
		List list = getReceivedDocument().getRootElement().selectNodes(receiveNodePath);
		Iterator iter = list.iterator();
		if (iter.hasNext()) {
			Element element = (Element) iter.next();
			element.addAttribute(key, (String) value);
		}
	}

	/**
	 * 实现DebraTransfer接口，从org.dom4j.Document接收对象获取键的值，如XML=<DebraRequest key＝value />
	 *
	 * @param key
	 */
	@Override
	public Object getReceiveAttribute(String key) {
		String result = null;
		if (key == null || key.equals("") || receiveDocument == null) {
			return "";
		}
		Element root = getReceivedDocument().getRootElement();
		List list = root.selectNodes(receiveNodePath);
		Iterator iter = list.iterator();
		if (iter.hasNext()) {
			Element element = (Element) iter.next();
			result = element.attributeValue(key);
		}
		return result;
	}

	/**
	 * 实现DebraTransfer接口,发送org.dom4j.Document对象内容
	 *
	 * @param document
	 * @throws IOException
	 */
	@Override
	public void sendByObject(T document) throws IOException {
		sendByString(document.asXML());
	}

	/**
	 * 实现DebraTransfer接口,发送XML格式的字符串内容
	 *
	 * @param message
	 * @throws IOException
	 */
	@Override
	public void sendByString(String message) throws IOException {
		if (DebraTransfer.CIPHER_MODE) {
			byte[] buffer = new byte[0];
			try {
				if (deskey == null)
					deskey = new SecretKeySpec(key, Algorithm);
				if (encrypeCipher == null) {
					encrypeCipher = Cipher.getInstance(Algorithm);
					encrypeCipher.init(Cipher.ENCRYPT_MODE, deskey);
				}
				buffer = message.getBytes("UTF-8");
				buffer = encrypeCipher.doFinal(buffer);
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			}
			outputStream = getConnection().getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			bos.write(buffer);
			bos.flush();
			bos.close();
			outputStream.close();
		} else {
			OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
			osw.write(message);
			osw.flush();
			osw.close();
		}
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, "DebraTransfer sendMessage = " + message);
	}

	/**
	 * 实现DebraTransfer接口,接收XML格式的字符串
	 *
	 * @return 接收的XML格式字符串
	 * @throws IOException
	 */
	@Override
	public String receiveToString() throws IOException {
		return receiveToString(getConnection().getInputStream());
	}

	/**
	 * 实现DebraTransfer接口,从InputStream接收XML格式的字符串
	 *
	 * @param inputStream
	 * @return 接收的JSON格式字符串
	 * @throws IOException
	 */
	@Override
	public String receiveToString(InputStream inputStream) throws IOException {
		if (DebraTransfer.CIPHER_MODE) {
			byte[] readBuffer = new byte[1024];
			byte[] resultBuffer = new byte[0];
			byte[] tempBuffer = resultBuffer;
			try {
				if (deskey == null)
					deskey = new SecretKeySpec(key, Algorithm);
				if (decryptCipher == null) {
					decryptCipher = Cipher.getInstance(Algorithm);
					decryptCipher.init(Cipher.DECRYPT_MODE, deskey);
				}
				BufferedInputStream bis = new BufferedInputStream(inputStream);
				int size = 0;
				int newBegin = 0;
				while ((size = bis.read(readBuffer)) != -1) {
					resultBuffer = new byte[size + newBegin];
					System.arraycopy(readBuffer, 0, resultBuffer, newBegin, size);
					System.arraycopy(tempBuffer, 0, resultBuffer, 0, tempBuffer.length);
					tempBuffer = resultBuffer;
					newBegin += size;
				}
				bis.close();
				inputStream.close();
				resultBuffer = decryptCipher.doFinal(resultBuffer);
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			}
			String result = new String(resultBuffer, "UTF-8");
			Logger.getLogger(DebraClientTransfer.class.getName()).log(Level.INFO, "DebraClientTransfer receiveMessage= " + result);
			try {
				receiveDocument = (T) DocumentHelper.parseText(result);
				setNodeByReceived("");
			} catch (DocumentException ex) {
				Logger.getLogger(DebraClientTransfer.class.getName()).log(Level.OFF, "DebraClientTransfer receiveXML format error", ex);
			}
			return result;
		} else {
			StringBuilder message = new StringBuilder();
			String line = null;
			InputStreamReader isr = new InputStreamReader(inputStream);
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				message.append(line);
			}
			isr.close();
			String result = String.valueOf(message);
			try {
				receiveDocument = (T) DocumentHelper.parseText(result);
				setNodeByReceived("");
			} catch (DocumentException ex) {
				Logger.getLogger(DebraClientTransfer.class.getName()).log(Level.OFF, "DebraClientTransfer receiveXML format error", ex);
			}
			Logger.getLogger(DebraClientTransfer.class.getName()).log(Level.INFO, "DebraClientTransfer receiveMessage = " + message);
			return result;
		}
	}

	/**
	 * 设置sendDocument XML信息初始化信息。包含LoginID、ActionURL
	 *
	 * @param root
	 */
	public void addRoot(String root) {
		if (attributeRoot == null && root != null) {
			if (sendDocument == null) attributeRoot = getSendDocument().addElement(root);
			else attributeRoot = sendDocument.addElement(root);
			setNodeBySend("");
			addSendAttribute("LoginID", loginID);
			addSendAttribute("ActionURL", classURL);
		}
	}

	/**
	 * 实现DebraTransfer接口,设置XML发送对象XPath
	 *
	 * @param nodePath
	 */
	@Override
	public void setNodeBySend(String nodePath) {
		sendNodePath = getSendDocument().getRootElement().getPath() + nodePath;
	}

	/**
	 * 实现DebraTransfer接口,设置XML接收对象XPath
	 *
	 * @param nodePath
	 */
	@Override
	public void setNodeByReceived(String nodePath) {
		receiveNodePath = getReceivedDocument().getRootElement().getPath() + nodePath;
	}

	/**
	 * 获取receivedDocument已接收XML格式的字符串
	 *
	 * @return
	 */
	@Override
	public String getReceivedString() {
		return getReceivedDocument().asXML();
	}

	/**
	 * 实现DebraTransfer接口,获取发送org.dom4j.Document对象
	 *
	 * @return
	 */
	@Override
	public T getSendDocument() {
		if (getConnection() == null && ContextUrl != null && !"".equals(ContextUrl)) {
			try {
				setConnection(DebraClientTransfer.urlToConnection(ContextUrl));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (sendDocument == null) {
			sendDocument = (T) DocumentHelper.createDocument();
			if (attributeRoot == null)
				addRoot(DebraClientTransfer.requestLabel);
		} else {
			if (attributeRoot == null)
				addRoot(DebraClientTransfer.requestLabel);
		}
		return sendDocument;
	}

	/**
	 * 实现DebraTransfer接口,获取接收org.dom4j.Document对象
	 *
	 * @return
	 */
	@Override
	public T getReceivedDocument() {
		if (receiveDocument == null) receiveDocument = (T) DocumentHelper.createDocument();
		return receiveDocument;
	}

	/**
	 * 实现DebraTransfer接口,设置org.dom4j.Document发送对象
	 *
	 * @param sendDocument
	 */
	@Override
	public void setSendDocument(T sendDocument) {
		this.sendDocument = sendDocument;
	}

	/**
	 * 实现DebraTransfer接口,断开HttpURLConnection连接
	 */
	@Override
	protected void disconnect() {
		if (getConnection() != null) getConnection().disconnect();
		setConnection(null);
	}

	/**
	 * 释放sendDocument，receiveDocument 资源
	 */
	@Override
	public void release() {
		disconnect();
		sendDocument = null;
		receiveDocument = null;
		attributeRoot = null;
	}
}
