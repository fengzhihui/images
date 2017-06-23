package com.fzh.wx.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fzh.wx.util.DBManager;

public class SaveGame extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String openid = request.getParameter("openid");
		String[] info = DBManager.getLifeGame(openid, request.getParameter("score"));
		String score = DBManager.getScore(openid);
		response.setContentType("text");
		response.getWriter().write(info[1]);
		if(score.length() == 0){
			DBManager.saveLifeGame(openid, request.getParameter("score"));
		} else {
			if(Integer.parseInt(request.getParameter("score")) > Integer.parseInt(score)){
				DBManager.updateLifeGame(openid, request.getParameter("score"));
			}
		}
		response.getWriter().close();
	}

}
