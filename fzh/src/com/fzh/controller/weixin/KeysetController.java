package com.fzh.controller.weixin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fzh.controller.PlatformController;
import com.fzh.entity.Keyset;
import com.fzh.entity.User;
import com.fzh.service.KeysetService;

@Controller
@RequestMapping("/keyset")
public class KeysetController extends PlatformController {
	@Autowired
	private KeysetService keySetService;

	@RequestMapping("/addUI")
	public ModelAndView addUI() throws Exception {
		return new ModelAndView(getBasePath() + "addkeyword");
	}
	
	@RequestMapping("/updateUI/{keyServiceNo}")
	public ModelAndView updateUI(@PathVariable("keyServiceNo")Long keyServiceNo) throws Exception {
		ModelAndView mav = new ModelAndView(getBasePath() + "answer");
		mav.addObject("keyset", keySetService.findByPK(keyServiceNo));
		return mav;
	}
	
	/**
	 * 修改关键词回复
	 * @param keyset
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateKeyset")
	public @ResponseBody boolean updateKeyset(Keyset keyset) throws Exception {
		if(keyset == null || keyset.getKeyServiceNo() == null) return false;
		int suc = keySetService.update(keyset);
		return suc > 0 ? true : false;
	}
	
	/**
	 * 保存关键词回复
	 * @param request
	 * @param keyset
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveKeyset")
	public @ResponseBody boolean saveKeyset(HttpServletRequest request, Keyset keyset) throws Exception {
		if(keyset == null) return false;
		User user = getSessionUser(request);
		return keySetService.save(keyset, user);
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public ModelAndView keysetList(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView(getBasePath() + "klist");
		User user = getSessionUser(request);
		int page = getPage(request.getParameter(PAGE));
		List<Keyset> list = keySetService.listKeySet(user.getWxpublicno(), getPageStart(page), PAGE_SIZE);
		mav.addObject(LIST, list);
		mav.addObject(PAGE, page);
		return mav;
	}

	@Override
	protected String getBasePath() {
		return "/keyset/keyservice/";
	}
	
	
}
