package com.fzh.wx.pojo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class InfoList extends HttpServlet {

	public List<Info> getInfoList(){
		List<Info> list = new ArrayList<Info>();
	    for(int i=1;i<5;i++){
	    	Info info = new Info();
	    	info.setAddr("地址"+i);
	    	info.setDesc("促销"+i);
	    	info.setName("公司"+i);
	    	info.setTel("电话"+i);
	    	list.add(info);
	    }
	    return list;
	}
	
	public String getJson(){
		StringBuffer str = new StringBuffer();
		for(int i=1;i<5;i++){
			str.append("北京华阳奥通*");
			str.append("400-889-1815*");
			str.append("顾家庄桥北300号*");
			str.append("秋季活动*");
		}
		return str.toString();
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");  
        response.setCharacterEncoding("UTF-8"); 
		PrintWriter out = response.getWriter();
		out.print(getJson());
		out.close();
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doGet(request, response);
	}
	
}
