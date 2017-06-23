package com.fzh.dao;

import org.springframework.stereotype.Repository;

import com.fzh.entity.User;
import com.fzh.jdbctemplate.JDBCUtil;

@Repository
public class UserDAO extends JDBCUtil {
    
    public int save(User user){
    	try {
    		return insert(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return 0;
    }
    
    public int updateUser(User user){
    	return update(user, "id");
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
	
	public int modify(Object... params){
		String sql = "update user set username = ? , password = ? where id = ?";
		return jdbcTemplate.update(sql, params);
	}
	
}
