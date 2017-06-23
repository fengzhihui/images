package com.fzh.wx.tenpay.util;

import java.util.Arrays;
import com.fzh.wx.util.SignUtil;
import com.fzh.wx.util.WeixinUtil;

public class WXUtil {
	
	/**
	 * 参数按字典序排序
	 * @param params
	 * @return
	 */
	public static String sortParams(Object... params){
		Arrays.sort(params);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < params.length; i++) {
			content.append(params[i]).append(i==params.length-1?"=%s":"=%s&");
		}
		return content.toString();
	}
	
	/**
	 * 获取随机字串（16位）
	 * @return
	 */
	public static String getNonceStr(){
		String nonce_str = "";
	    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    int maxPos = chars.length();
	    for (int i = 0; i < 16; i++){
	       nonce_str += chars.charAt((int) Math.floor(Math.random() * maxPos));
	    }
	    return nonce_str;
	}

	/**
	 * 获取时间戳
	 * @return
	 */
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
	
}
