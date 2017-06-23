package com.fzh.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 操作Properties文件
 * @author fzh
 *
 */
public class PropertiesUtil {
	private static Properties properties = new Properties();
	static{
		try {
			properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("jdbc.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readUrl(String key){
		return (String)properties.get(key);
	}
}
