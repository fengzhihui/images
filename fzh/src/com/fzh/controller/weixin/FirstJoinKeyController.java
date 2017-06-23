package com.fzh.controller.weixin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fzh.controller.PlatformController;
import com.fzh.entity.FirstJoinKey;
import com.fzh.entity.User;
import com.fzh.service.FirstJoinKeyService;

@Controller
@RequestMapping("/firstKey")
public class FirstJoinKeyController extends PlatformController {
	@Autowired
	private FirstJoinKeyService firstJoinKeyService;
	
	@RequestMapping("/firstKeyUI")
	public ModelAndView firstKeyUI(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView(getBasePath() + "firstanswer");
		mav.addObject("firstJoinKey", firstJoinKeyService.findByWxNo(getWeiXinPublicNo(request)));
		return mav;
	}
	
	@RequestMapping("/saveFirstKey")
	public @ResponseBody boolean saveFirstKey(HttpServletRequest request, FirstJoinKey firstJoinKey) throws Exception {
		User user = getSessionUser(request);
		if(firstJoinKey == null) return false;
		firstJoinKeyService.saveFirstJoinKey(firstJoinKey, user);
		return true;
	}

	@Override
	protected String getBasePath() {
		return "/keyset/firstjoinkey/";
	}

}
