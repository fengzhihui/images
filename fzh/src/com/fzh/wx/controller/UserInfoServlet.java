package com.fzh.wx.controller;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import com.fzh.wx.util.SignUtil;
import com.fzh.wx.util.WeixinUtil;

/**
 * 网页授权获取用户信息
 * @author fzh
 *
 */
public class UserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 340730705454153328L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");//text/plain(无格式正文) text/jsonp application/jsonp 这样浏览器会提供下载
		resp.setCharacterEncoding("UTF-8");
		try {
			String code = req.getParameter("code"), appid = req.getParameter("appid"), secret = req.getParameter("secret");
			/*//测试号
			appid = "wx8b8dc1a67050ebde";
			secret = "ca46fc30999aa4f3b32817c30961a7eb";
			//票易所
			appid = "wx20a7cbae6fcda553";
			secret = "bb420ba7d97ef2f5f91ea8c35727447e";*/
			/**
			 * 加密secret
			 */
			if("encrypt".equals(req.getParameter("method"))){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("secret", SignUtil.encode(secret));
				resp.getWriter().write(jsonObject.toString());
			}
			if(code==null || appid==null || secret==null)return;
			Logger.getAnonymousLogger().info("code = "+code);
			/**
			 * 获取用户信息
			 */
			if("userinfo".equals(req.getParameter("method"))){
				//解密
				if("jsonp".equals(req.getParameter("type"))) secret = SignUtil.decode(secret);
				JSONObject jsonObject = WeixinUtil.getUserInfo(code, appid, secret, req.getParameter("scope"));
				//跨域请求返回数据
				if("jsonp".equals(req.getParameter("type"))){
					String callback = req.getParameter("jsoncallback");
					resp.getWriter().write(callback + "("+jsonObject+")");
				}else{
					resp.getWriter().write(jsonObject.toString());
				}
			}
			
			resp.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
