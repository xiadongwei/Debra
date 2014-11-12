/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Framework.Transfer.JSON;

import debra.Framework.Transfer.DebraTransfer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tony
 */
public class DebraServerTransfer<T extends JSONObject> extends DebraTransfer<T> {
	public DebraServerTransfer(InputStream reqStream, OutputStream respStream) {
		this.inputStream = reqStream;
		this.outputStream = respStream;
	}

	@Override
	public void addSendAttribute(String key, Object value) {
		getSendDocument().put(key, value);
	}

	@Override
	public Object getSendAttribute(String key) {
		return getSendDocument().get(key);
	}

	@Override
	public void addReceiveAttribute(String key, Object value) {
		getReceivedDocument().put(key, value);
	}

	@Override
	public Object getReceiveAttribute(String key) {
		return getReceivedDocument().get(key);
	}

	@Override
	public void setNodeBySend(String nodePath) {
		Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "JSON model don't used");
		new UnsupportedOperationException("JSON model don't used");
	}

	@Override
	public void setNodeByReceived(String nodePath) {
		Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "JSON model don't used");
		new UnsupportedOperationException("JSON model don't used");

	}

	@Override
	public void sendByObject(JSONObject document) throws IOException {
		sendByString(document.toJSONString());
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
			receiveDocument = ((T) JSONObject.parseObject(result));
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
			receiveDocument = (T) JSONObject.parseObject(result);
			Logger.getLogger(DebraServerTransfer.class.getName()).log(Level.INFO, "DebraServerTransfer receiveMessage = " + message);
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
	public String getReceivedString() {
		return getReceivedDocument().toJSONString();
	}

	@Override
	public T getSendDocument() {
		if (sendDocument == null) sendDocument = (T) new JSONObject();
		return sendDocument;
	}

	@Override
	public T getReceivedDocument() {
		if (receiveDocument == null) receiveDocument = (T) new JSONObject();
		return receiveDocument;
	}

	@Override
	public void setSendDocument(T sendDocument) {
		if (sendDocument != null) {
			this.sendDocument = sendDocument;
		} else {
			this.sendDocument = (T) new JSONObject();
		}
	}
}
