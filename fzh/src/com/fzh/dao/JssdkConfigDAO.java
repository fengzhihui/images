package com.fzh.dao;

import org.springframework.stereotype.Repository;

import com.fzh.entity.JssdkConfig;
import com.fzh.jdbctemplate.JDBCUtil;

@Repository
public class JssdkConfigDAO extends JDBCUtil {
    
    public int save(JssdkConfig jssdkConfig){
    	return insert(jssdkConfig);
    }
    
}
