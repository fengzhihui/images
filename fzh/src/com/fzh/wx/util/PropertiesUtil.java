package com.fzh.wx.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 操作Properties文件
 * @author fzh
 *
 */
public class PropertiesUtil {
	private static Properties properties = new Properties();
	/**
	 * 从缓存中加载文件，如果文件有更改会自动加载出来
	 */
	static{
		try {
			properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("jdbc.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据key获取value
	 * @param key
	 * @return
	 */
	public static String getValue(String key){
		return (String)properties.get(key);
	}
}
