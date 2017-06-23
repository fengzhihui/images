package com.fzh.wx.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fzh.wx.service.MovieService;

public class CrzGuesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		List<Map<String, String>> list = null;
		long second = new Date().getTime();
		if("m".equals(request.getParameter("m"))){
			list = MovieService.getAllZHDYMovies();
			request.getSession().getServletContext().setAttribute("mvlist", list);
		}else{
			list = (List<Map<String, String>>) request.getSession().getServletContext().getAttribute("mvlist");
		}
		response.getWriter().write(new Date().getTime() - second + "毫秒\n\n" + list.toString());
		response.getWriter().close();
		
	}

}
