package com.fzh.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fzh.entity.MenuInfo;
import com.fzh.jdbctemplate.JDBCUtil;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

@Repository
public class MenuInfoDAO extends JDBCUtil {
	private static final Logger logger = LoggerFactory.getLogger(MenuInfoDAO.class);
	
	public int save(MenuInfo menuInfo) throws Exception {
		return insert(menuInfo);
	}
	
	public int deleteMenuInfo(int id) {
		logger.info(" ---------------------- DAO deleteMenuInfo");
		String sql = "delete from menu_info where id = ?";
		return jdbcTemplate.update(sql, new Object[]{ id });
	}
	
	public int deleteByWxNo(String wxno) {
		String sql = "delete from menu_info where weixin_public_no = ?";
		logger.info(" ---------------------- DAO deleteByWxNo");
		return jdbcTemplate.update(sql, new Object[]{ wxno });
	}
	
	public MenuInfo findByPK(String pk) throws Exception {
		String sql = "select * from menu_info m where m.id = ?";
		logger.info(" ---------------------- DAO MenuInfo findByPk");
		return this.getObject(MenuInfo.class, sql, new Object[]{ pk });
	}
	
	public MenuInfo doFindByWxNo(String weixinNo) throws Exception {
		String sql = "select * from menu_info m where m.weixin_public_no = ? order by m.op_time desc limit 1";
		logger.info(" ---------------------- DAO MenuInfo findByWxNo");
		return this.getObject(MenuInfo.class, sql, new Object[]{ weixinNo });
	}
	
	public List<MenuInfo> findByWxNo(String weixinNo) {
		String sql = "select * from menu_info m where m.weixin_public_no = ?";
		return this.getListObject(MenuInfo.class, sql, new Object[]{ weixinNo });
	}
	
	public List<MenuInfo> doFindMenuByWxNo(String weixinNo) {
		String sql = "select m.menu_id,m.parent_menu_id,m.menu_name,m.menu_type,m.url,k.ref_text,k.reftext_image_id,k.ref_vedio_id"+
				 		" from menu_info m,menu_keyset k" +
				 		" where m.weixin_public_no=k.weixin_public_no and m.menu_id=k.menu_id and m.weixin_public_no = ?";
		return this.getListObject(MenuInfo.class, sql, new Object[]{ weixinNo });
	}
	
	public MenuInfo doFindByMenuId(String menuId, String wxno) {
		String sql = "select * from menu_info where menu_id = ? and weixin_public_no = ?";
		return this.getObject(MenuInfo.class, sql, new Object[]{ menuId,wxno });
	}
	
	public String doFindByUrl(String weixinNo,String url) {
		String sql = "select m.menu_id from menu_info m where m.weixin_public_no = ? and m.url = ? limit 1";
		logger.info(" ---------------------- DAO MenuInfo doFindByUrl");
		MenuInfo menuInfo = this.getObject(MenuInfo.class, sql, new Object[]{ weixinNo, url });
		return menuInfo.getMenuId() + "";
	}
	
	public List<String> findParentMenuList(String weixinNo) {
		String sql = "select menu_id from menu_info m where m.weixin_public_no = ? and m.parent_menu_id = 0";
		logger.info(" ---------------------- DAO MenuInfo doFindParentMenu");
		return this.jdbcTemplate.queryForList(sql, String.class, new Object[]{ weixinNo });
	}
	
}
