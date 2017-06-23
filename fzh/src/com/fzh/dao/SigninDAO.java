package com.fzh.dao;

import org.springframework.stereotype.Repository;

import com.fzh.entity.Signin;
import com.fzh.jdbctemplate.JDBCUtil;

@Repository
public class SigninDAO extends JDBCUtil {
    
    public int save(Signin signin){
    	try {
    		return insert(signin);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return 0;
    }
    
    public int updateSignin(Signin signin){
    	return update(signin, "id");
    }
    
	public Signin findScoreByOpenId(String openId) {
		try {
			String sql = "SELECT score,create_time FROM signin WHERE openid = ? order by create_time desc limit 1";
    		return getObject(Signin.class, sql, new Object[]{ openId });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
