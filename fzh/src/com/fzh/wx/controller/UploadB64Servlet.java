package com.fzh.wx.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sun.misc.BASE64Decoder;

public class UploadB64Servlet extends HttpServlet {
	private static final long serialVersionUID = 6899718324661394129L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		request.setCharacterEncoding("UTF-8");
		String json = "";
		String callback = request.getParameter("jsoncallback");
		String base64 = request.getParameter("base64");
		BASE64Decoder decoder = new BASE64Decoder();
		System.out.println(base64);
        try{
            //Base64解码
            byte[] b = decoder.decodeBuffer(base64.replace("data:image/png;base64,", ""));
            for(int i=0;i<b.length;++i){
                if(b[i]<0){//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片
            String imgFilePath = "d://222.jpg";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            json = "{\"src\":\"imgurl\"}".replace("imgurl", imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            //request.getRequestDispatcher("/2048.html").forward(request, response);
            //response.sendRedirect("file:///C:/Users/Administrator/Desktop/t.people.com.cn/t.people.com.cn/default.html");
        }catch(Exception e){
        	e.printStackTrace();
        }
	
//		response.getWriter().write(callback+"(json)".replace("json", json));
//		response.getWriter().close();
	}
	

}
