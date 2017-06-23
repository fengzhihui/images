package com.fzh.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.fzh.common.LoginValidate;
import com.fzh.service.UserService;

@Controller
@RequestMapping("/http")
public class HttpRequestController {
	@Autowired
	private UserService userService;
	
	@LoginValidate(value=false)
	@RequestMapping(value="/findUser", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody JSONObject findUser(HttpServletRequest request) throws Exception {
		JSONObject json = new JSONObject();
		String username = request.getParameter("username");
		json.put("data", userService.findByUsername(username));
		return json;
	}
	
    
}
