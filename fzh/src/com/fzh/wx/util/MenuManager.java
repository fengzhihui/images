package com.fzh.wx.util;

import java.util.logging.Logger;
import com.fzh.wx.pojo.menupojo.AccessToken;

/**
 * 菜单管理器类
 * @author fzh
 */
public class MenuManager {
	/**
	 * 创建菜单测试
	 * @param json
	 */
	public static void createMenu(String json){
		//f_zhihui wx026c598dabeff144 f07ef65caf1225cac17937002568aaf5
		AccessToken at = WeixinUtil.getAccessToken("wx897214b41f475c11", "b94720854f4c74f8a26a1a789c3d9f12");
		int result = WeixinUtil.createMenu(json, at.getToken());
		// 判断菜单创建结果
		if (0 == result)
			Logger.getAnonymousLogger().info("菜单创建成功!" + at.getToken());
		else
			Logger.getAnonymousLogger().info("菜单创建失败,错误码: " + result);
	}
	
}