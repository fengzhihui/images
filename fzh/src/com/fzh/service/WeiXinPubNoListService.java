package com.fzh.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fzh.dao.WeiXinPubNoListDAO;
import com.fzh.entity.WeixinPubnoList;
import com.fzh.wx.util.SignUtil;

@Service
public class WeiXinPubNoListService {
	@Autowired
	private WeiXinPubNoListDAO weiXinPubNoListDAO;
	@Value("${wx_interface_url}")
    private String wxInterfaceUrl;

	public WeixinPubnoList save(String wxpubno,String wxname,String token,String username){
		Date date = new Date();
		wxInterfaceUrl += SignUtil.encode(wxpubno + "|" + token);
		WeixinPubnoList weiXinPubNoList = new WeixinPubnoList();
		weiXinPubNoList.setToken(token);
		weiXinPubNoList.setCreateTime(date);
		weiXinPubNoList.setOperateTime(date);
		weiXinPubNoList.setOperator(username);
		weiXinPubNoList.setCompanyName(wxname);
		weiXinPubNoList.setWeixinPublicNo(wxpubno);
		weiXinPubNoList.setInterfaceUrl(wxInterfaceUrl);
		weiXinPubNoListDAO.save(weiXinPubNoList);
		return weiXinPubNoList;
	}
	
	public WeixinPubnoList findByOperator(String operator) {
		return weiXinPubNoListDAO.findByOperator(operator);
	}
	
	public void update(WeixinPubnoList weiXinPubNoList) {
		weiXinPubNoListDAO.update(weiXinPubNoList);
	}

	public WeixinPubnoList findByWxNo(String wxpublicno) {
		return weiXinPubNoListDAO.findByWxNo(wxpublicno);
	}
}
