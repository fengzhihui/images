package com.fzh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.dao.KeysetDAO;
import com.fzh.entity.Keyset;
import com.fzh.entity.User;
import com.fzh.wx.util.StringUtil;

@Service
public class KeysetService {
	@Autowired
	private KeysetDAO keySetDAO;

	public boolean save(Keyset keyset, User user) {
		keyset.setCreateTime(new Date());
		keyset.setCreateUser(user.getUsername());
		keyset.setOperatorName(user.getUsername());
		keyset.setWeixinPublicNo(user.getWxpublicno());
		if(!StringUtil.isBlank(keyset.getRefText())){
			keyset.setRefText(keyset.getRefText().replace("&lt;", "<") .replace("&gt;", ">"));
		}
		int suc = keySetDAO.save(keyset);
		if(suc > 0) return true;
		return false;
	}

	public int update(Keyset keyset) {
		Keyset _keyset = this.findByPK(keyset.getKeyServiceNo());
		if(_keyset != null) {
			return keySetDAO.update(keyset);
		}
		return 0;
	}

	public int delete(String keyServiceNo) {
		return keySetDAO.delete(keyServiceNo);
	}

	public Keyset findKeySet(String WxNo,String keyWord) {
		return keySetDAO.findKeySet(WxNo, keyWord);
	}
	
	public Keyset findByPK(Long keyServiceNo) {
		return keySetDAO.findByPK(keyServiceNo);
	}

	public List<Keyset> listKeySet(String wxPublicNo, int start, int length) {
		return keySetDAO.listKeySet(wxPublicNo, start, length);
	}
	
	public int getCounts(String wxPublicNo){
		return keySetDAO.getCounts(wxPublicNo);
	}
	
	public String findKeywords(String wxPublicNo) {
		return keySetDAO.findKeywords(wxPublicNo);
	}
	
}
