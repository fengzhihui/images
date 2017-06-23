package com.fzh.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fzh.entity.User;
import com.fzh.service.UserService;
import com.fzh.service.WeiXinPubNoListService;
import com.fzh.wx.util.StringUtil;

@Controller
public class LoginController extends PlatformController {
	@Autowired
	private UserService userService;
	@Autowired
	private WeiXinPubNoListService weiXinPubNoListService;
	
	/**
	 * 登录
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request) throws Exception{
		String password = request.getParameter("pwd");
		String username = request.getParameter("username");
		ModelAndView mav = new ModelAndView(getBasePath());
		mav.addObject(MESSAGE, "用户名或密码有误！");
		if(!StringUtil.isBlank(username) && !StringUtil.isBlank(password)){
			User user = userService.findByUsernameAndPwd(username, password);
			if(user != null){
				setSessionUser(request, user);
				mav.setViewName("addwxno");
				mav.addObject("wxpnlist", weiXinPubNoListService.findByOperator(username));
			}
		}
		return mav;
	}
	
	/**
	 * 退出
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request){
		ModelAndView mav = new ModelAndView(getBasePath());
		HttpSession session = request.getSession();
		Enumeration<String> enumeration = session.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String sessionName = enumeration.nextElement();
			session.removeAttribute(sessionName);
			session.invalidate();
		}
		return mav;
	}

	@Override
	protected String getBasePath() {
		return "/login";
	}
	
    
}
