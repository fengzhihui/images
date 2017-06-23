package com.fzh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.dao.JssdkConfigDAO;
import com.fzh.entity.JssdkConfig;

@Service
public class JssdkConfigService {
	@Autowired
	private JssdkConfigDAO jssdkConfigDAO;

	public int save(JssdkConfig jssdkConfig) {
		jssdkConfig.setCreateTime(System.currentTimeMillis());
		return jssdkConfigDAO.save(jssdkConfig);
	}


	
}
