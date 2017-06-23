package com.fzh.dao;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.fzh.entity.MessageInfo;
import com.fzh.jdbctemplate.JDBCUtil;

@Repository
public class MessageInfoDAO extends JDBCUtil {

	public int save(MessageInfo messageInfo) {
		return this.insert(messageInfo);
	}
	
	public int update(MessageInfo messageInfo) {
		return this.update(messageInfo, "id");
	}
	
	public int delete(String id) {
		String sql = "delete from message_info where id=?";
		return this.jdbcTemplate.update(sql, new Object[]{ id });
	}
	
	public int deleteByWxNo(String wxno) {
		String sql = "delete from message_info where weixin_public_no=?";
		return this.jdbcTemplate.update(sql, new Object[]{ wxno });
	}
	
	public List<MessageInfo> findByWxNo(String wxno) {
		String sql = "select * from message_info m where m.weixin_public_no = ?";
		return this.getListObject(MessageInfo.class, sql, new Object[]{ wxno });
	}
	
	public MessageInfo findByMenuId(String menuId, String wxno) {
		String sql = "select * from message_info where menu_id = ? and weixin_public_no = ?";
		return this.getObject(MessageInfo.class, sql, new Object[]{ menuId, wxno });
	}
	
	/**
	 * 关键字统计
	 * @param date
	 * @param keyword
	 * @param wxno
	 */
	public List<Map<String, Object>> findByKeyword(String date,String keyword,String wxno){
		String dateSql = "";
		if("1".equals(date)){	   //按日
			dateSql = "AND DATE(msg_send_time) = CURDATE()";
		}else if("2".equals(date)){//按周
			dateSql = "AND DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= DATE(msg_send_time)";
		}else if("3".equals(date)){//按月
			dateSql = "AND DATE_SUB(CURDATE(), INTERVAL 1 MONTH) <= DATE(msg_send_time)";
		}
	
		String sql = "SELECT msg_content, DATE_FORMAT(msg_send_time, '%Y-%m-%d') AS msg_send_time, COUNT(msg_content) AS _count" +
				 " FROM message_info " +
				 " WHERE public_no = ? AND keyword='1' AND msg_content LIKE ? " + dateSql +
				 " GROUP BY DATE_FORMAT(msg_send_time, '%Y-%m-%d'), msg_content ORDER BY msg_send_time DESC";
		return this.jdbcTemplate.queryForList(sql, new Object[]{ wxno, "%" + keyword + "%" });
	}
	
	/**
	 * 用户交互统计
	 * @param date
	 * @param wxno
	 */
	public List<Map<String, Object>> findUserMsg(String date,String wxno){
		String dateSql = "";
		if("1".equals(date)){	   //按日
			dateSql = "AND DATE(w.msg_send_time) = CURDATE()";
		}else if("2".equals(date)){//按周
			dateSql = "AND DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= DATE(w.msg_send_time)";
		}else if("3".equals(date)){//按月
			dateSql = "AND DATE_SUB(CURDATE(), INTERVAL 1 MONTH) <= DATE(w.msg_send_time)";
		}
	
		String sql = "SELECT COUNT(DISTINCT a.o) AS _count,SUM(a.c) AS _sum,a.msg_send_time" +
					 " FROM (" +
					 " SELECT DATE_FORMAT(w.msg_send_time, '%Y-%m-%d') AS msg_send_time, w.open_id AS o,COUNT(w.open_id) AS c" +
					 " FROM message_info w " +
					 " WHERE w.public_no = ? " + dateSql +" GROUP BY w.msg_send_time" +
					 ") a GROUP BY a.msg_send_time";
		return this.jdbcTemplate.queryForList(sql, new Object[]{ wxno });
	}
	
	/**
	 * 自定义菜单统计
	 * @param date
	 * @param wxno
	 */
	public List<Map<String, Object>> findMenuMsg(String date,String wxno){
		String dateSql = "";
		if("1".equals(date)){	   //按日
			dateSql = "AND DATE(m.msg_send_time) = CURDATE()";
		}else if("2".equals(date)){//按周
			dateSql = "AND DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= DATE(m.msg_send_time)";
		}else if("3".equals(date)){//按月
			dateSql = "AND DATE_SUB(CURDATE(), INTERVAL 1 MONTH) <= DATE(m.msg_send_time)";
		}
	
		String sql = "SELECT DATE_FORMAT(m.msg_send_time, '%Y-%m-%d') AS msg_send_time, m.menu_key, i.parent_menu_id, COUNT(1) AS _count, i.order_no" +
					 " FROM message_info m,menu_info i" +
					 " WHERE m.public_no = ? AND m.msg_type='event' AND m.public_no=i.weixin_public_no AND m.menu_key=i.menu_id " + dateSql +
					 " GROUP BY DATE_FORMAT(m.msg_send_time, '%Y-%m-%d'),m.menu_key ORDER BY m.msg_send_time";
		Logger.getAnonymousLogger().info("-----------------  sql = " + sql);
		return this.jdbcTemplate.queryForList(sql, new Object[]{ wxno });
	}

}
