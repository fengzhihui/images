package com.fzh.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fzh.common.Const;
import com.fzh.entity.User;
import com.fzh.wx.util.StringUtil;

/**
 * 平台controller
 * @author fzh
 */
@Controller
public abstract class PlatformController {
	/**提示信息*/
	protected final String MESSAGE = "msg";
	/**分页信息*/
	protected final String PAGE = "page";
	/**每页10条*/
	protected final int PAGE_SIZE = 10;
	/**总条数*/
	protected final String TOTAL_COUNT = "totalCount";
	/**列表数据*/
	protected final String LIST = "list";
	
	/**返回视图路径**/
	protected abstract String getBasePath();
	
	/** 基于@ExceptionHandler异常处理 */  
    @ExceptionHandler
    public String exp(HttpServletRequest request, Exception ex) {
        request.setAttribute("ex", ex);
        ex.printStackTrace();
        // 根据不同错误转向不同页面  
        return "error_500";
    }
    
    /**
	 * 分页
	 * @param page
	 * @return
	 */
	protected int getPage(String page) {
		return Integer.parseInt(StringUtil.isBlank(page) ? "1" : page);
	}
	
	/**
	 * 分页起始行数
	 * @param page
	 * @return
	 */
	protected int getPageStart(int page) {
		return (page - 1) * PAGE_SIZE;
	}
	
	/**
	 * 获取工程真实路径
	 * @param request
	 * @return
	 */
	protected String getRealPath(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("/");
	}
	
	/**
	 * 获取用户登录信息
	 * @param request
	 * @return
	 */
	protected User getSessionUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute(Const.LOGIN_USER_ATTRIBUTE);
	}
	
	/**
	 * 保存用户登录信息
	 * @param request
	 * @param user
	 */
	protected void setSessionUser(HttpServletRequest request, User user) {
		request.getSession().setAttribute(Const.LOGIN_USER_ATTRIBUTE, user);
	}
	
	/**
	 * 获取用户公众号
	 * @param request
	 * @return
	 */
	protected String getWeiXinPublicNo(HttpServletRequest request) {
		User user = getSessionUser(request);
		return user != null ? user.getWxpublicno() : null;
	}
	
}
