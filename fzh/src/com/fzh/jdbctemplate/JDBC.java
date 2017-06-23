package com.fzh.jdbctemplate;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/***
 * JDBC通用类
 * @author fzh
 */
public class JDBC {
	// 日志
	private static final Logger logger = LoggerFactory.getLogger(JDBC.class);
	
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:../spring-servlet.xml");
	private static JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
	
	public static JdbcTemplate getJdbc() {
		logger.info("ctx：" + ctx);
		return jdbcTemplate;
	}
	
	public static Map<String, Object> queryForMap(String sql, Object... args){
		return jdbcTemplate.queryForMap(sql, args);
	}
	

}
