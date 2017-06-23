package com.fzh.controller.weixin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.fzh.controller.PlatformController;
import com.fzh.entity.User;
import com.fzh.service.UserService;
import com.fzh.util.encrypt.TjrAES;

@Controller
@RequestMapping("/user")
public class UserController extends PlatformController {
	@Autowired
	private UserService userService;
	
	/**
	 * 添加用户页面
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/addUser")
	public ModelAndView addUI(){
		ModelAndView mav = new ModelAndView(getBasePath() + "adduser");
		return mav;
	}
	
	/**
	 * 保存用户
	 * @param request
	 * @param user
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping("/saveUser")
	public @ResponseBody JSONObject saveUser(HttpServletRequest request, User user) throws Exception {
		JSONObject json = new JSONObject();
		json.put("data", "no");
		User existUser = userService.findByUsername(user.getUsername());
		//检查重复用户
		if(existUser != null){
			return json;
		}
		int suc = userService.saveUser(user);
		if(suc > 0){
			json.put("data", "ok");
		}
		return json;
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateUI")
	public ModelAndView updateUI(HttpServletRequest request){
		ModelAndView mav = new ModelAndView(getBasePath() + "modify");
		return mav;
	}
	
	/**
	 * 修改用户密码
	 * @param request
	 * @param user
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/updatePassword")
	public @ResponseBody JSONObject updatePassword(HttpServletRequest request, User user) throws Exception{
		JSONObject json = new JSONObject();
		json.put("flag", "no");
		if(user == null) return json;
		if(user.getUsername() !=null && user.getPassword() !=null){
			User existUser = userService.findByUsername(user.getUsername());
			if(existUser != null){
				existUser.setPassword(TjrAES.encrypt(user.getPassword()));
				if(userService.updateUser(existUser) > 0){
					json.put("flag", "ok");
				}
			}
		}
		return json;
	}
	
	@Override
	protected String getBasePath() {
		return "/user/";
	}
	
}
