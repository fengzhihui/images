package com.fzh.wx.controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fzh.wx.util.SignUtil;

/**
 * 获取后台参数
 * @author fzh
 *
 */
public class GetParamServlet extends HttpServlet {
	private static final long serialVersionUID = 1811988303408152205L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String url = request.getParameter("url");
		String appid = request.getParameter("appid");
		String callback = request.getParameter("jsoncallback");
		try {
//			if("ticket".equals(request.getParameter("method"))){
//				if (url.contains("b.haleqi.com") || url.contains("exmpc")
//						|| url.contains("maitalk") || url.contains("54vhwy")
//						|| url.contains("yrj007.duapp.com")
//						|| url.contains("itwx01.duapp.com")
//						|| url.contains("xiyuri.com")
//						|| url.contains("gameover")
//						|| url.contains("zhihui2014")
//						|| url.contains("fzh2014") || url.contains("shishanglife")) {}
//			}
			response.getWriter().write(callback+ "(json)".replace("json", SignUtil.getSignature(appid, url)));
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.getWriter().close();
	}
	
}
