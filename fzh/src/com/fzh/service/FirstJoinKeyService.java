package com.fzh.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.dao.FirstJoinKeyDAO;
import com.fzh.entity.FirstJoinKey;
import com.fzh.entity.User;

@Service
public class FirstJoinKeyService {
	@Autowired
	private FirstJoinKeyDAO firstJoinKeyDAO;

	public int saveFirstJoinKey(FirstJoinKey firstJoinKey, User user) {
		FirstJoinKey firstjoinkey = findByWxNo(user.getWxpublicno());
		if(firstjoinkey != null){
			if(firstJoinKey.getReText() != null){
				firstJoinKey.setReText(firstJoinKey.getReText().trim().replace("&lt;", "<") .replace("&gt;", ">"));
			}
			return firstJoinKeyDAO.update(firstJoinKey, "id");
		} else {
			firstJoinKey.setCreateTime(new Date());
			firstJoinKey.setCreateUser(user.getUsername());
			firstJoinKey.setOperatorName(user.getUsername());
			firstJoinKey.setWeixinPublicNo(user.getWxpublicno());
			return firstJoinKeyDAO.save(firstJoinKey);
		}
	}

	public void update(FirstJoinKey firstJoinKey) {
		firstJoinKeyDAO.update(firstJoinKey);
	}

	public void delete(int id) {
		firstJoinKeyDAO.delete(id);
	}

	public FirstJoinKey findByWxNo(String WxNo) {
		return firstJoinKeyDAO.findByWxNo(WxNo);
	}

}
