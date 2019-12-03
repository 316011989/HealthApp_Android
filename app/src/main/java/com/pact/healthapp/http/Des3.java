package com.pact.healthapp.http;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 重新定义DES加密，统一android、ios、java平台加密解密
 * <p>
 * Title: Des3.java
 * </p>
 * 
 * @date 2014-6-6 上午10:39:12
 */
public class Des3 {

	/**
	 * 密钥
	 */
	public final static String defaultKey = "agnbemgeiandeai!feian45#w3";
	private final static String KeyVer = "default";
	/**
	 * 向量
	 */
	private final static String iv = "01234567";
	/**
	 * 加解密统一使用的编码方式
	 */
	private final static String encoding = "UTF-8";

	/**
	 * 3DES加密
	 * 
	 * @param plainText
	 *            普通文本
	 * @return
	 * @throws Exception
	 */
	public static String encode(String plainText) throws Exception {
		return encode(plainText, defaultKey);
	}

	/**
	 * 加密
	 * 
	 * @param plainText
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encode(String plainText, String key) {
		// System.out.println("encode key:"+key);
		// System.out.println("encode key:"+plainText);
		if ("".equals(key)) {
			key = defaultKey;
		}
		try {
			Key deskey = null;
			DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
			SecretKeyFactory keyfactory = SecretKeyFactory
					.getInstance("desede");
			deskey = keyfactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
			byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
			return Base64.encode(encryptData);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 3DES解密
	 * 
	 * @param encryptText
	 *            加密文本
	 * @return
	 * @throws Exception
	 */
	public static String decode(String encryptText) {
		return decode(encryptText, defaultKey);
	}

	/**
	 * 解密
	 * 
	 * @param encryptText
	 * @param key
	 * @return
	 * @throws DecodeException
	 * @throws Exception
	 */
	public static String decode(String encryptText, String key) {
		// System.out.println("decode key:"+key);
		// System.out.println("decode key:"+encryptText);
		if ("".equals(key)) {
			key = defaultKey;
		}
		Key deskey = null;
		try {
			DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
			SecretKeyFactory keyfactory = SecretKeyFactory
					.getInstance("desede");
			deskey = keyfactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
			byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));
			return new String(decryptData, encoding);
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		Des3 des3 = new Des3();
		String encode = des3.encode("123456");
		System.out.println("加密------------->" + encode);
		String decode = des3
				.decode("RlK6fSFWQt0rOu06V8xv50e1uNjH81HoFpltN41cupcNnKzYcuHXxnpe6Kdo Iv+zr3wkXn9I9Q7sCCpH3VzTpGFCy39Li2jNSB+4pYXF+a4=");
		System.out.println("解密------------->" + decode);
	}
}
