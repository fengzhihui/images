package com.fzh.wx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.fzh.common.LoginValidate;
import com.fzh.entity.UserInfo;
import com.fzh.service.UserInfoService;
import com.fzh.wx.service.GuessImgService;

@Controller
@RequestMapping("/crzguess")
public class GuessImageController {
	@Autowired
	private UserInfoService userInfoService;

	/**
	 * 保存猜图游戏
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	@LoginValidate(value=false)
	@RequestMapping("/saveRecord")
	public @ResponseBody boolean saveGuessImage(String userid, String type) throws Exception {
		int suc = userInfoService.saveIdsByUserIdAndType(userid, type);
		return suc > 0 ? true : false;
	}
	
	@LoginValidate(value=false)
	@RequestMapping("/getAnswer")
	public @ResponseBody JSONObject getAnswer(String userid, String type) {
		UserInfo userInfo = userInfoService.findByUserIdAndType(userid, type);
		String num = userInfo == null ? "1" : userInfo.getIds();
		return GuessImgService.getAnswerInfo(type, num);
	}
	
	
}
