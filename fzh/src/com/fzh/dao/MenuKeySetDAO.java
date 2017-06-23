package com.fzh.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.fzh.entity.MenuKeyset;
import com.fzh.jdbctemplate.JDBCUtil;

@Repository
public class MenuKeySetDAO extends JDBCUtil {
	public void save(MenuKeyset menuKeySet) {
		this.insert(menuKeySet);
	}
	
	public int update(MenuKeyset menuKeySet) {
		menuKeySet.setCreateTime(new Date());
		return this.update(menuKeySet, "menu_keyset_no");
	}
	
	public int delete(String menuKeySetNo) {
		String sql = "delete from menu_keyset where menu_keyset_no = ?";
		return this.jdbcTemplate.update(sql, new Object[] { menuKeySetNo });
	}
	
	public MenuKeyset findByPK(String menuKeySetNo) {
		String sql = "select * from menu_keyset m where m.menu_keyset_no = ?";
		return this.getObject(MenuKeyset.class, sql, new Object[]{ menuKeySetNo });
	}
	
	public List<MenuKeyset> findByWxNo(String weixinPublicNo){
		String sql = "select * from menu_keyset m where m.weixin_public_no = ?";
		return this.getListObject(MenuKeyset.class, sql, new Object[]{ weixinPublicNo });
	}

	public int deleteByWxNo(String wxno) {
		String sql = "delete from menu_keyset where weixin_public_no = ?";
		return this.jdbcTemplate.update(sql, new Object[]{ wxno });
	}

	public MenuKeyset findMenuKeySet(String createUser, String menuId) {
		String sql = "select * from menu_keyset where create_user = ? and menu_id = ?";
		return this.getObject(MenuKeyset.class, sql, new Object[]{ createUser, menuId });
	}
	
}
