/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Framework.Transfer.XML;

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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tony
 */
public class DebraServerTransfer<T extends Document> extends DebraTransfer<T> {
	public static String responseLabel = "DebraResponse";
	protected Element attributeRoot;
	protected String sendNodePath, receiveNodePath;

	public DebraServerTransfer(InputStream reqStream, OutputStream respStream) {
		this.inputStream = reqStream;
		this.outputStream = respStream;
		addRoot(DebraServerTransfer.responseLabel);
	}

	private void addRoot(String root) {
		if (attributeRoot == null) {
			attributeRoot = getSendDocument().addElement(root);
		}
		setNodeBySend("");
	}

	@Override
	public void addSendAttribute(String key, Object value) {
		List list = getSendDocument().getRootElement().selectNodes(sendNodePath);
		Iterator iter = list.iterator();
		if (iter.hasNext()) {
			Element element = (Element) iter.next();
			element.addAttribute(key, (String) value);
		}
	}

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

	@Override
	public void addReceiveAttribute(String key, Object value) {
		List list = getReceivedDocument().getRootElement().selectNodes(receiveNodePath);
		Iterator iter = list.iterator();
		if (iter.hasNext()) {
			Element element = (Element) iter.next();
			element.addAttribute(key, (String) value);
		}
	}

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

	@Override
	public void sendByObject(T document) throws IOException {
		sendByString(document.asXML());
	}

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

	@Override
	public String receiveToString() throws IOException {
		return receiveToString(inputStream);
	}

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
			Logger.getLogger(DebraClientTransfer.class.getName()).log(Level.INFO, "DebraServerTransfer receiveMessage= " + result);
			try {
				receiveDocument = (T) DocumentHelper.parseText(result);
				setNodeByReceived("");
			} catch (DocumentException ex) {
				Logger.getLogger(DebraClientTransfer.class.getName()).log(Level.OFF, "DebraServerTransfer receiveXML format error", ex);
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
				Logger.getLogger(DebraClientTransfer.class.getName()).log(Level.OFF, "DebraServerTransfer receiveXML format error", ex);
			}
			Logger.getLogger(DebraClientTransfer.class.getName()).log(Level.INFO, "DebraServerTransfer receiveMessage = " + message);
			return result;
		}
	}

	@Override
	protected void disconnect() {
		Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "DebraServerTransfer disconnect don't used");
		new UnsupportedOperationException("DebraServerTransfer disconnect don't used");
	}

	@Override
	public void release() {
		new UnsupportedOperationException("DebraServerTransfer release don't used");
	}

	@Override
	public void setNodeBySend(String nodePath) {
		sendNodePath = getSendDocument().getRootElement().getPath() + nodePath;
	}

	@Override
	public void setNodeByReceived(String nodePath) {
		receiveNodePath = getReceivedDocument().getRootElement().getPath() + nodePath;
	}

	@Override
	public String getReceivedString() {
		return getReceivedDocument().asXML();
	}

	@Override
	public T getSendDocument() {
		if (sendDocument == null) sendDocument = (T) DocumentHelper.createDocument();
		return sendDocument;
	}

	@Override
	public void setSendDocument(T sendDocument) {
		if (sendDocument != null) {
			this.sendDocument = sendDocument;
		} else {
			this.sendDocument = (T) DocumentHelper.createDocument();
		}
	}

	@Override
	public T getReceivedDocument() {
		if (receiveDocument == null) receiveDocument = (T) DocumentHelper.createDocument();
		return receiveDocument;
	}
}
