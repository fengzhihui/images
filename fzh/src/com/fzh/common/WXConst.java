package com.fzh.common;

/**
 * 常量公共类
 * @author fzh
 *
 */
public class WXConst {
	/**
	 * 菜单创建接口地址 限100次/天(POST)
	 */
	public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	/**
	 * 获取access_token的接口地址(GET) 限200次/天
	 */
	public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
	/**
	 * 获取jsapi_ticket的接口地址(GET)
	 */
	public final static String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	/**
	 * 创建二维码access_token接口地址(POST)
	 */
	public final static String CREATE_QRCODE_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
	/**
	 * 显示二维码地址
	 */
	public final static String SHOW_QRCODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
	/**
	 * 通过code换取网页授权access_token接口(GET)
	 */
	public final static String OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=CODE&grant_type=authorization_code";
	/**
	 * 网页授权地址(scope为snsapi_base和snsapi_userinfo)
	 */
	public final static String OAUTH2_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	/**
	 * 获取用户信息接口
	 */
	public final static String USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

}
