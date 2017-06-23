package com.fzh.wx.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fzh.common.LoginValidate;
import com.fzh.wx.service.APIUtilService;

import net.sf.json.JSONObject;

@Controller
public class JsonpController {
	/**
	 * JSONP跨域请求(本质是GET，查看浏览器的General->Request Method)
	 * @return 
	 */
	@LoginValidate(value=false)
	@RequestMapping(value="/jsonp", produces="application/json;charset=UTF-8")
	public @ResponseBody Object jsonp(HttpServletRequest request) throws Exception {
		String callback = request.getParameter("callback");
		JSONObject json = new JSONObject();
		json.put("data", "JSONP跨域请求(本质是GET，查看浏览器的General->Request Method)");
		System.out.println(request.getParameter("name"));
		return callback + String.format("(%s)", json);
	}
	
	@LoginValidate(value=false)
	@RequestMapping(value="/musicList", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody Object getMusicList(HttpServletRequest request) throws Exception {
		String name = request.getParameter("name");
		return APIUtilService.getMusicList(name);
	}
	
	@LoginValidate(value=false)
	@RequestMapping(value="/getMp3Url", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody Object getMp3Url(HttpServletRequest request) throws Exception {
		String songid = request.getParameter("songid");
		return APIUtilService.getMp3Url(songid);
	}

}
