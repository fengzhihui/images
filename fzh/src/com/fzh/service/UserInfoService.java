package com.fzh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.common.Const;
import com.fzh.dao.UserInfoDAO;
import com.fzh.entity.UserInfo;

@Service
public class UserInfoService {
	@Autowired
	private UserInfoDAO userInfoDAO;

	public int updateUserInfo(UserInfo userInfo) throws Exception {
		return userInfoDAO.update(userInfo, "id");
	}
	
	public UserInfo findByUserIdAndType(String userid, String type){
		return userInfoDAO.findByUserIdAndType(userid, type);
	}
	
	public UserInfo findByUserId(String userid){
		return userInfoDAO.getUserByUserId(userid);
	}
	
	/**
	 * 保存猜图游戏
	 * @param userid
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public int saveIdsByUserIdAndType(String userid, String type) throws Exception{
		UserInfo userInfo = findByUserIdAndType(userid, type);
		if(userInfo != null) {
			String ids = userInfo.getIds() == null ? "1" : userInfo.getIds();
			Integer num = Integer.parseInt(ids) + 1;
			userInfo.setIds(num.toString());
			if(num == Const.GUESS_CY_COUNT || num == Const.GUESS_IMG_COUNT){
				userInfo.setIsend(1);
			}
			return updateUserInfo(userInfo);
		}
		return 0;
	}
}
