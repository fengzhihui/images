package com.fzh.controller.weixin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fzh.controller.PlatformController;
import com.fzh.service.MenuInfoService;
import com.fzh.service.MessageInfoService;
import com.fzh.wx.util.StringUtil;

@Controller
@RequestMapping("/messageinfo")
public class MessageInfoController extends PlatformController {
	@Autowired
	private MenuInfoService menuInfoService;
	@Autowired
	private MessageInfoService messageInfoService;
	
	/**
	 * 关键字统计
	 * @return
	 */
	@RequestMapping("/calcKeyword")
	public ModelAndView calcKeyword(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(getBasePath() + "keylist");
		String date = request.getParameter("date");
		String keyword = request.getParameter("keyword");
		if(StringUtil.isBlank(date)) date = "1";
		if(StringUtil.isBlank(keyword)) keyword = "";
		mav.addObject("date", date);
		mav.addObject("keyword", keyword);
		mav.addObject(LIST, messageInfoService.findByKeyword(date, keyword, getWeiXinPublicNo(request)));
		return mav;
	}
	
	/**
	 * 用户交互消息统计
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/calcUserMsg")
	public ModelAndView calcUserMsg(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView(getBasePath() + "msglist");
		String date = request.getParameter("date");
		if(StringUtil.isBlank(date)) date = "1";
		mav.addObject("date", date);
		mav.addObject("data", messageInfoService.findUserMsg(date, getWeiXinPublicNo(request)));
		return mav;
	}
	
	/**
	 * 自定义菜单消息统计
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/calcMenuMsg")
	public ModelAndView calcMenuMsg(HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView(getBasePath() + "menulist");
		String date = request.getParameter("date");
		if(StringUtil.isBlank(date)) date = "1";
		String wxno = getWeiXinPublicNo(request);
		List<String> pids = menuInfoService.findParentMenuList(wxno);
		if(pids != null){
			mav.addObject(LIST, messageInfoService.findMenuMsg(date, wxno, pids));
		}
		mav.addObject("date", date);
		return mav;
	}

	@Override
	protected String getBasePath() {
		return "/messageinfo/";
	}
	
}
