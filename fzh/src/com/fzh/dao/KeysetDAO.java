package com.fzh.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fzh.entity.Keyset;
import com.fzh.jdbctemplate.JDBCUtil;

@Repository
public class KeysetDAO extends JDBCUtil {
    
	public int save(Keyset keySet) {
    	return this.insert(keySet);
	}

	public int update(Keyset keySet) {
		return this.update(keySet, "key_service_no");
	}

	public int delete(String keyServiceNo) {
		String sql = "delete from keyset where id=?";
		return this.jdbcTemplate.update(sql, new Object[]{ keyServiceNo });
	}

	public Keyset findByPK(Long keyServiceNo) {
		String sql = "select * from keyset k where k.key_service_no = ?";
		return this.getObject(Keyset.class, sql, new Object[]{ keyServiceNo });
	}
	
	//随机10个电影名字
	public String findKeywords(String wxPublicNo) {
		String sql = "SELECT GROUP_CONCAT(t.key_word) FROM (SELECT key_word FROM keyset WHERE weixin_public_no = ? ORDER BY RAND() LIMIT 10) AS t;";
		return this.jdbcTemplate.queryForObject(sql, String.class, wxPublicNo);
	}
	
	public Keyset findKeySet(String WxNo, String keyWord) {
		String sql = "select * from keyset k where k.weixin_public_no = ? and k.key_word = ?";
		return this.getObject(Keyset.class, sql, new Object[]{ WxNo, keyWord });
	}

	public List<Keyset> listKeySet(String wxPublicNo, int start, int length) {
		String sql = "select * from keyset k where k.weixin_public_no = ? order by k.create_time desc limit ?,?";
		return this.getListObject(Keyset.class, sql, new Object[]{ wxPublicNo, start, length });
	}
	
	public int getCounts(String wxPublicNo){
		String sql = "select count(*) as count from keyset where weixin_public_no = ?";
		return this.getCounts(sql, new Object[]{ wxPublicNo });
	}

}
