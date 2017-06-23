package com.fzh.wx.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import com.fzh.common.WXConst;
import com.fzh.wx.pojo.jssdkpojo.Ticket;
import com.fzh.wx.pojo.menupojo.AccessToken;
import com.fzh.wx.pojo.menupojo.Menu;
import net.sf.json.JSONObject;

/**
 * 公众平台通用接口工具类
 * @author fzh
 *
 */
public class WeixinUtil {

	/**
	 * 发起https请求并获取结果
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式(GET/POST)
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 * java.lang.ClassCastException: sun.net.www.protocol.http.HttpURLConnection cannot be cast to javax.net.ssl.HttpsURLConnection
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			
			// 设置请求方式(GET/POST)
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式,防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			Logger.getAnonymousLogger().info("Weixin server connection timed out.");
		} catch (Exception e) {
			Logger.getAnonymousLogger().info("信任管理器请求时..."+e);
		}
		return jsonObject;
	}

	/**
	 * 获取access_token
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;

		String requestUrl = String.format(WXConst.ACCESS_TOKEN_URL, appid, appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (Exception e) {
				accessToken = null;
				// 获取token失败
				Logger.getAnonymousLogger().info("获取token失败 errcode:{} errmsg:{}"+ jsonObject.getInt("errcode")+ jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}
	
	/**
	 * 获取jsapi_ticket（jsapi_ticket的有效期为7200秒）
	 * @param token
	 * @return
	 */
	public static Ticket getTicket(String token){
		Ticket ticket = null;
		String requestUrl = WXConst.JSAPI_TICKET_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				ticket = new Ticket();
				ticket.setTicket(jsonObject.getString("ticket"));
				ticket.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (Exception e) {
				ticket = null;
				// 获取token失败
				Logger.getAnonymousLogger().info("获取ticket失败 errcode:{} errmsg:{}"+ jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return ticket;
	}	
	
	/**
	 * 创建菜单
	 * @param menu 菜单实例
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;

		// 拼装创建菜单的url
		String url = WXConst.MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();
		// 调用接口创建菜单
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				Logger.getAnonymousLogger().info("创建菜单失败 errcode:{} errmsg:{}"+jsonObject.getInt("errcode")+jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	/**
	 * 创建菜单
	 * @param jsonMenu
	 * @param accessToken
	 */
	public static int createMenu(String jsonMenu, String accessToken) {
		System.out.println("jsonMenu = " + jsonMenu);
		int result = 0;
		// 拼装创建菜单的url
		String url = WXConst.MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
		// 调用接口创建菜单
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				Logger.getAnonymousLogger().info("创建菜单失败 errcode:{} errmsg:{} "+jsonObject.getInt("errcode")+jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	/**
	 * 创建二维码
	 * @param appid
	 * @param secret
	 */
	public static void createQrcode(String appid,String secret){
		String ticket = "";
		//QR_SCENE为临时,QR_LIMIT_SCENE为永久
		String json = "{\"expire_seconds\": 1800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 123}}}";
		try {
			AccessToken at = getAccessToken("wxf8f7e3df26af1fe5", "76ff3e3996bb0cffeb201b7d6e71181b");
			if(at != null){
				String token = at.getToken();
				System.out.println("token = "+token);
				JSONObject jsonObject = httpRequest(WXConst.CREATE_QRCODE_TOKEN_URL.replaceAll("TOKEN", token), "POST", json);
				if(jsonObject != null){
					ticket = jsonObject.getString("ticket");
					System.out.println("ticket= " + ticket);
					System.out.println("查看二维码URL：" + WXConst.SHOW_QRCODE_URL + ticket);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 网页授权
	 * @param appid
	 * @param redirectURI
	 */
	public static void oauth2(String appid,String redirectURI){
		try {
			redirectURI = URLEncoder.encode(redirectURI, "utf-8");
			System.out.println("redirectURI = " + redirectURI);
			System.out.println("url = " + String.format(WXConst.OAUTH2_AUTHORIZE_URL, appid, redirectURI));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 拉取用户信息
	 * @param code
	 * @param appid
	 * @param secret
	 * @param scope
	 */
	public static JSONObject getUserInfo(String code,String appid,String secret,String scope){
		String openid = "", access_token = "";
		//得到access_token和openid
		JSONObject jsonObject = httpRequest(WXConst.OAUTH2_ACCESS_TOKEN_URL.replaceAll("CODE", code).replaceAll("APPID", appid).replaceAll("SECRET", secret), "GET", null);
		if(null != jsonObject){
			try {
				openid = jsonObject.getString("openid");
				access_token = jsonObject.getString("access_token");
				//获取用户详细信息
				if("snsapi_userinfo".equals(scope)){
					jsonObject = httpRequest(String.format(WXConst.USERINFO_URL, access_token, openid), "GET", null);
					if(null != jsonObject){
						//拉取用户信息
						String nickname = "", sex = "", city = "", province = "", country = "", headimgurl = "", userinfo = "";
						sex = jsonObject.getString("sex");
						city = jsonObject.getString("city");
						openid = jsonObject.getString("openid");
						country = jsonObject.getString("country");
						province = jsonObject.getString("province");
						nickname = jsonObject.getString("nickname");
						headimgurl = jsonObject.getString("headimgurl");
						userinfo = "性别：" + sex + "<br>昵称：" + nickname + "<br>地址：" +country+province+city + "<br><img width='50px' height='50px' src=\"" + headimgurl + "\"/>";
						Logger.getAnonymousLogger().info("userinfo = "+userinfo);
					}
				}
				System.out.println("openid = "+openid);
				System.out.println("access_token = "+access_token);
			} catch (Exception e) {
				access_token = null;
				// 获取token失败
				Logger.getAnonymousLogger().info("获取token失败 errcode:{} errmsg:{}"+ jsonObject.getInt("errcode")+ jsonObject.getString("errmsg"));
			}
		}
		return jsonObject;
	}
	
	public static void main(String[] args) {
		System.out.println(SignUtil.decode("6262343230626137643937656632663566393165613863333537323734343765"));
		String appid = "wx20a7cbae6fcda553", appsecret = "f07ef65caf1225cac17937002568aaf5", url = "http://www.ba3ba.com/fzh/fzhihui.jsp";
		//String token = getAccessToken(appid, appsecret).getToken();
		//System.out.println("token = " + token);
		//System.out.println("ticket = " + getTicket(token).getTicket());
		oauth2(appid, url);
		//[{id:0,pid:0,open:true,name:"自定义菜单列表",url:"",content:"",type:0},{id:101,pid:0,open:true,name:"品牌传说",url:"",content:"",type:1},{id:102,pid:0,open:true,name:"小U商城",url:"",content:"",type:1},{id:103,pid:0,open:true,name:"为您服务",url:"",content:"",type:1},{id:104,pid:101,name:"品牌档案",url:"http://weixin.udis.cn/index.php?g=Wap&m=Index&a=content&token=fxurgw1413472487&id=393&wecha_id=opuHVjvyWtKodKEU8Na1pwW5BfXQ",content:"",type:3},{id:105,pid:101,name:"小U捷报",url:"http://weixin.udis.cn/index.php?g=Wap&m=Index&a=content&token=fxurgw1413472487&id=399&wecha_id=opuHVjoLNZt5DUjvfQJXe3OwddsA",content:"",type:3},{id:106,pid:101,name:"品牌案例",url:"http://weixin.udis.cn/index.php?g=Wap&m=Index&a=content&token=fxurgw1413472487&id=395&wecha_id=opuHVjvyWtKodKEU8Na1pwW5BfXQ",content:"",type:3},{id:107,pid:102,name:"新商城效果演示",url:"http://www.udis.cn/templates/green/new/1.html",content:"",type:3},{id:108,pid:102,name:"产品视频",url:"http://www.udis.cn/templates/green/video/v2.html",content:"",type:3},{id:109,pid:102,name:"了解产品",url:"http://weixin.udis.cn/index.php?g=Wap&m=Index&a=content&token=fxurgw1413472487&id=396&wecha_id=opuHVjvyWtKodKEU8Na1pwW5BfXQ",content:"",type:3},{id:110,pid:102,name:"快速购买",url:"http://weixin.udis.cn/index.php?&g=Wap&m=Store&a=cats&token=fxurgw1413472487&wecha_id=opuHVjvyWtKodKEU8Na1pwW5BfXQ",content:"",type:3},{id:111,pid:102,name:"最新活动",url:"http://weixin.udis.cn/index.php?&g=Wap&m=Store&a=cats&token=fxurgw1413472487&wecha_id=opuHVjvyWtKodKEU8Na1pwW5BfXQ",content:"",type:3},{id:112,pid:103,name:"售后服务",url:"",content:"好",type:1},{id:113,pid:103,name:"订单维权",url:"",content:"若您对我们的服务质量或产品有任何问题，觉得小u还有可以改进的地方，可联系我们的售后热线：400-820-8331 客服小鲜肉们随时待命！",type:1},{id:114,pid:103,name:"智能客服小U",url:"",content:"如果亲对小U的产品有什么疑惑的,欢迎直接给小U留言或者在线提问;小u知无不言，言无不尽！不过如果你还想最后问小u一个问题，那不用问了，答案是：爱！",type:1},{id:115,pid:103,name:"飞鸽传书",url:"http://weixin.udis.cn/index.php?g=Wap&m=Index&a=content&token=fxurgw1413472487&id=397&wecha_id=opuHVjvyWtKodKEU8Na1pwW5BfXQ",content:"",type:3}]
	}
}