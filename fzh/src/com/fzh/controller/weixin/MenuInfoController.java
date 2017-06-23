package com.fzh.controller.weixin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fzh.controller.PlatformController;
import com.fzh.entity.MenuInfo;
import com.fzh.entity.User;
import com.fzh.service.MenuInfoService;
import com.fzh.service.MenuKeySetService;

@Controller
@RequestMapping("/menu")
public class MenuInfoController extends PlatformController {
	@Autowired
	private MenuInfoService menuInfoService;
	@Autowired
	private MenuKeySetService menuKeySetService;
	
	/**
	 * 自定义菜单页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/wxtreeUI")
	public ModelAndView menuInfoUI(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView(getBasePath() + "wxtree");
		MenuInfo menuInfo = menuInfoService.doFindByWxNo(getWeiXinPublicNo(request));
		if (menuInfo != null) {
			mav.addObject("treedata", menuInfo.getVchar1().replaceAll("\r\n", "\\\\r\\\\n"));
		} else {
			mav.addObject("treedata", "[{id:0,pid:0,name:'自定义菜单列表',url:'',content:'',type:0}]");
		}
		return mav;
	}
	
	/**
	 * 保存菜单
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveMenu")
	public ModelAndView saveMenu(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("redirect:/menu/wxtreeUI");
		String str = request.getParameter("str");
		String treedata = request.getParameter("treedata");
		User user = getSessionUser(request);
		menuInfoService.saveMenu(user.getWxpublicno(), user.getUsername(), treedata, str);
		return mav;
	}
	
	/**
	 * 发布菜单
	 * @param request
	 * @param jsondata
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/createMenu")
	public ModelAndView createMenu(HttpServletRequest request,String jsondata) throws Exception {
		ModelAndView mav = new ModelAndView();
		String result = menuInfoService.createMenu(getWeiXinPublicNo(request), jsondata);
		if("un_authorize".equals(result)){
			mav.setViewName(getBasePath() + "authorize");
		} else {
			mav.setViewName(getBasePath() + "wxtree");
			mav.addObject("rs", result);
		}
		return mav;
	}
	
	/**
	 * 查询图文
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/findImgTxtHtml}")
	public @ResponseBody String findImgTxtHtml(HttpServletRequest request) throws Exception {
		String menuId = request.getParameter("menuId");
		if(menuId != null) {
			MenuInfo menuInfo = menuInfoService.doFindByMenuId(menuId, getWeiXinPublicNo(request));
			if(menuInfo != null) return menuInfo.getVchar3();
		}
		return null;
	}
	
	/**
	 * 删除全部节点
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteAllNodes")
	public @ResponseBody int deleteAllNodes(HttpServletRequest request) throws Exception {
		menuInfoService.deleteByWxNo(getWeiXinPublicNo(request));
		menuKeySetService.deleteByWxNo(getWeiXinPublicNo(request));
		return 1;
	}

	@Override
	protected String getBasePath() {
		return "/menu/menuinfo/";
	}
	
}
