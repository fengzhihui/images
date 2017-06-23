package com.fzh.dao;

import org.springframework.stereotype.Repository;

import com.fzh.entity.UserInfo;
import com.fzh.jdbctemplate.JDBCUtil;

@Repository
public class UserInfoDAO extends JDBCUtil {
    
    public int save(UserInfo user){
    	try {
    		return insert(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return 0;
    }
    
    public int updateUser(UserInfo userInfo){
    	return update(userInfo, "id");
    }
    
	public UserInfo findByUserIdAndType(String userid,String type) {
		try {
			String sql = "select * from user_info where userid = ? and type = ? order by time desc limit 1";
    		return getObject(UserInfo.class, sql, new Object[]{ userid, type });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public UserInfo getUserByUserId(String userid) {
		try {
			String sql = "select * from user_info where userid = ? order by time desc limit 1";
    		return getObject(UserInfo.class, sql, new Object[]{ userid });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
