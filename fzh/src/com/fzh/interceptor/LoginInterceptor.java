package com.fzh.interceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fzh.common.Const;
import com.fzh.common.LoginValidate;

/**
 * 登录拦截器
 * @author fzh
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println("请求拦截...");
		//spring mvc拦截请求
		if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
			LoginValidate loginValidate = ((HandlerMethod) handler).getMethodAnnotation(LoginValidate.class);
            //没有声明则默认需要登录
            if(loginValidate == null || loginValidate.value()){
            	//未登录
            	if(request.getSession().getAttribute(Const.LOGIN_USER_ATTRIBUTE) == null){
            		//获得请求路径的URI
            		if(!request.getRequestURI().contains("login")){
            			response.sendRedirect(request.getContextPath() + "/login");
            			return false;
            		}
            		
            		//AJAX请求（响应头会有x-requested-with）
            		String requestType = request.getHeader("X-Requested-With");  
                    if(requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")){
                    	System.out.println("AJAX拦截...");
                    	ServletOutputStream out = response.getOutputStream();
                        out.print("unlogin");//AJAX返回： error(e)->e.responseText
                        out.flush();
                        out.close();
                        return false;
                    }
            		
    			}
            }
        }
		return true;
	}
	
}