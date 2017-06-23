package com.fzh.dao;

import org.springframework.stereotype.Repository;

import com.fzh.entity.WeixinPubnoList;
import com.fzh.jdbctemplate.JDBCUtil;

@Repository
public class WeiXinPubNoListDAO extends JDBCUtil {
	
	public void save(WeixinPubnoList weiXinPubNoList) {
    	try {
    		insert(weiXinPubNoList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update(WeixinPubnoList weiXinPubNoList) {
    	try {
    		this.update(weiXinPubNoList, "id");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public WeixinPubnoList findByWxNo(String wxNo) {
    	try {
    		String sql = "select * from weixin_pubno_list w where w.weixin_public_no = ?";
    		return this.getObject(WeixinPubnoList.class, sql, new Object[]{ wxNo });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public WeixinPubnoList findByOperator(String operator) {
    	try {
			String sql = "select * from weixin_pubno_list w where w.operator = ?";
			return this.getObject(WeixinPubnoList.class, sql, new Object[]{ operator });
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
	}
	
}
