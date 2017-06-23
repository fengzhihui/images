package com.fzh.util.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public class TjrBase64 {

	public static String encodeBase64URLSafeString(String v) {
		return Base64.encodeBase64URLSafeString(getBytesUtf8(v));
	}

	public static String encodeBase64URLSafeString(byte[] v) {
		return Base64.encodeBase64URLSafeString(v);
	}

	public static String decodeBase64String(String v) {
		return StringUtils.newStringUtf8(Base64.decodeBase64(v));
	}

	public static byte[] decodeBase64(String v) {
		return Base64.decodeBase64(v);
	}

	public static byte[] getBytesUtf8(String v) {
		return StringUtils.getBytesUtf8(v);
	}

	public static String newStringUtf8(byte[] v) {
		return StringUtils.newStringUtf8(v);
	}

	public static void main(String[] args) {
		String e = encodeBase64URLSafeString("136we仍5");
		System.out.println(e);
		String d = decodeBase64String(e);
		System.out.println(d);
	}
}
