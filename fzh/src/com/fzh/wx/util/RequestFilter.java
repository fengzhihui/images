package com.fzh.wx.util;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestFilter implements Filter {
	//创建线程
	public static ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<HttpServletRequest>();
	public static ThreadLocal<HttpServletResponse> threadLocalResponse = new ThreadLocal<HttpServletResponse>();
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		threadLocalRequest.set((HttpServletRequest) request);
		threadLocalResponse.set((HttpServletResponse) response);
		filter.doFilter(request, response);
	}
	public void destroy() {}
	public void init(FilterConfig filter) throws ServletException {
		System.out.println("RequestFilter init...");
	}
}