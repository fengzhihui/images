package com.fzh.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class TjrAES {
	private static final String KEY = "fengzhihuiAesKey";//不能改
	private static final String AES = "AES";

	public static String encrypt(String value) throws Exception {
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(TjrBase64.getBytesUtf8(KEY), AES);
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(TjrBase64.getBytesUtf8(value));
			return TjrBase64.encodeBase64URLSafeString(encrypted);
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static String decrypt(String encrypt) throws Exception {
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(TjrBase64.getBytesUtf8(KEY), AES);
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] original = cipher.doFinal(TjrBase64.decodeBase64(encrypt));
			return TjrBase64.newStringUtf8(original);
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static void main(String[] args) throws Exception {
		String encrypt = encrypt("fzh789456");//123456 eaGJIxj8yJt7j50YvilMuA
		System.out.println(encrypt);
		System.out.println(decrypt(encrypt));
	}
}
