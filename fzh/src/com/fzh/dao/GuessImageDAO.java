package com.fzh.dao;

import org.springframework.stereotype.Repository;

import com.fzh.entity.GuessImage;
import com.fzh.entity.User;
import com.fzh.jdbctemplate.JDBCUtil;

@Repository
public class GuessImageDAO extends JDBCUtil {
    
    public int saveGuessImage(GuessImage guessImage){
    	try {
    		return insert(guessImage);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return 0;
    }
    
    public int updateGuessImage(GuessImage guessImage){
    	return update(guessImage, "id");
    }
    
	public User findByUsernameAndPwd(String username,String password) {
    	String sql = "select * from user u where u.username = ? and u.password = ? order by id limit 1";
    	try {
    		return getObject(User.class, sql, new Object[]{ username, password });
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
	}
	
	public User findByUsername(String username) {
		try {
			String sql = "select * from user u where u.username = ? order by id limit 1";
    		return getObject(User.class, sql, new Object[]{ username });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public User findByOpenId(String openId) {
		try {
			String sql = "select * from user where openid = ? order by id limit 1";
    		return getObject(User.class, sql, new Object[]{ openId });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
