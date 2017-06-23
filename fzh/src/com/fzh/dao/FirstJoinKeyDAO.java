package com.fzh.dao;

import org.springframework.stereotype.Repository;

import com.fzh.entity.FirstJoinKey;
import com.fzh.jdbctemplate.JDBCUtil;

@Repository
public class FirstJoinKeyDAO extends JDBCUtil {
    
	public int save(FirstJoinKey firstJoinKey) {
		return insert(firstJoinKey);
	}

	public int update(FirstJoinKey firstJoinKey) {
		return update(firstJoinKey);
	}

	public void delete(int id) {
    	try {
    		jdbcTemplate.update("delete from first_join_key where id = ?", new Object[]{id});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FirstJoinKey findByWxNo(String WxNo) {
    	try {
			String sql = "select * from first_join_key k where k.weixin_public_no = ?";
			return this.getObject(FirstJoinKey.class, sql, new Object[]{ WxNo });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
