package com.fzh.controller.weixin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.fzh.controller.PlatformController;
import com.fzh.entity.JssdkConfig;
import com.fzh.entity.User;
import com.fzh.entity.WeixinPubnoList;
import com.fzh.service.JssdkConfigService;
import com.fzh.service.UserService;
import com.fzh.service.WeiXinPubNoListService;

@Controller
@RequestMapping("/wxpubno")
public class WxPubNoController extends PlatformController {
	@Autowired
	private UserService userService;
	@Autowired
	private JssdkConfigService jssdkConfigService;
	@Autowired
	private WeiXinPubNoListService weiXinPubNoListService;

	@RequestMapping("/JSSDKUI")
	public ModelAndView JSSDKUI(){
		return new ModelAndView("/jssdk/jssdkConfig");
	}
	
	@RequestMapping("/oauthUI")
	public ModelAndView oauthUI(){
		return new ModelAndView("/oauth/oauthConfig");
	}
	
	/**
	 * 配置接口页面
	 * @return
	 */
	@RequestMapping("/addWxnoUI")
	public ModelAndView addWxnoUI(HttpServletRequest request){
		User user = getSessionUser(request);
		ModelAndView mav = new ModelAndView("/addwxno");
		if(user != null){
			WeixinPubnoList wxpnlist = weiXinPubNoListService.findByOperator(user.getUsername());
			mav.addObject("wxpnlist", wxpnlist);
		}
		return mav;
	}
	
	/**
	 * 保存公众号信息，把公众号信息保存到用户表
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/saveWxno")
	public @ResponseBody String saveWxno(HttpServletRequest request) throws Exception {
		String token = request.getParameter("token");
		String wxname = request.getParameter("wxname");
		String wxpubno = request.getParameter("wxpubno");
		User user = getSessionUser(request);
		//保存公众号信息
		WeixinPubnoList weiXinPubNoList = weiXinPubNoListService.save(wxpubno, wxname, token, user.getUsername());
		//把公众号信息保存到用户表
		User usr = userService.findByUsername(user.getUsername());
		if(usr != null){
			usr.setWxpublicno(wxpubno);
			userService.updateUser(usr);
		}
		return weiXinPubNoList.getInterfaceUrl();
	}
	
	/**
	 * 保存菜单授权信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/authorizeMenu")
	public ModelAndView authorizeMenu(HttpServletRequest request, RedirectAttributes attr){
		ModelAndView mav = new ModelAndView("redirect:/menu/wxtreeUI");
		WeixinPubnoList wxpnlist = weiXinPubNoListService.findByWxNo(getWeiXinPublicNo(request));
		if(wxpnlist != null){
			wxpnlist.setAppid(request.getParameter("appid"));
			wxpnlist.setAppsecret(request.getParameter("secret"));
			weiXinPubNoListService.update(wxpnlist);
			attr.addFlashAttribute("ok", "保存成功，请重新发布菜单！");
		}
		return mav;
	}
	
	/**
	 * 保存JSSDK配置
	 * @param jssdkConfig
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveJSSDK")
	public @ResponseBody JSONObject saveJSSDK(JssdkConfig jssdkConfig) throws Exception {
		JSONObject json = new JSONObject();
		json.put("data", "no");
		if(jssdkConfig == null) return json;
		int suc = jssdkConfigService.save(jssdkConfig);
		if(suc > 0) {
			json.put("data", "ok");
		}
		return json;
	}

	@Override
	protected String getBasePath() {
		return null;
	}
}
