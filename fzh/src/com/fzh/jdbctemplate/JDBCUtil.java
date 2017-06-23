package com.fzh.jdbctemplate;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.fzh.util.SQLUtils;

/***
 * JDBC工具类
 * @author fzh
 */
public abstract class JDBCUtil {
	// 日志
	private static final Logger logger = LoggerFactory.getLogger(JDBCUtil.class);
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	/**
	 * 插入数据 Class类名一定和数据库表名相同
	 * @param <T>
	 * @param t
	 * @return
	 */
	public <T> int insert(T t) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		try {
			SQLUtils.createInsert(t, sql, args);
		} catch (Exception e) {
			logger.error(String.format("Error createInsert sql params:%s", t), e);
			return 0;
		}
		return jdbcTemplate.update(sql.toString(), args.toArray());
	}
	
	 /**
     * 插入一个对象，并返回这个对象的自增id
     * @param t
     * @return
	 * @throws Exception 
     */
    public <T> long insertAndGetId(T t) throws Exception {
    	final StringBuffer sql = new StringBuffer();
		final List<Object> args = new ArrayList<Object>();
		SQLUtils.createInsert(t, sql, args);
		Long id = 0L;
		KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
    		public PreparedStatement createPreparedStatement(Connection conn)
    				throws SQLException {
    			PreparedStatement preparedStatement = conn.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
    			for(int i=0; i < args.size(); i ++){
    				preparedStatement.setObject(i+1, args.get(i));
    			}
    			return preparedStatement;
    		}
    	}, keyHolder);
        id = keyHolder.getKey().longValue();
        return id;
    }

	/**
	 * 更新赋值的实体 key 位主键名称，如果没有 使用id作为默认的
	 * @param <T>
	 * @param t
	 * @param key
	 * @return
	 */
	public <T> int update(T t, String key) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		try {
			SQLUtils.createUpdate(t, sql, args, key);
		} catch (Exception e) {
			logger.error(String.format("Error createUpdate sql params:%s", t), e);
			return 0;
		}
		return jdbcTemplate.update(sql.toString(), args.toArray());
	}

	/**
	 * 批量更新数据
	 * @param sql
	 * @param values
	 * @return
	 */
	public int[] batchUpdate(String sql, List<Object[]> values) {
		return jdbcTemplate.batchUpdate(sql, values);
	}
	
	/**
	 * 删除数据 key 位主键名称，如果没有 使用id作为默认的
	 */
	public <T> int delete(Class<T> clz, String key, Object value) {
		if (value == null) return 0;
		StringBuffer sql = new StringBuffer();
		SQLUtils.createDelete(clz, key, sql);
		return jdbcTemplate.update(sql.toString(), value);
	}
	
	/**
	 * 获取单个对象
	 * @param clz
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> T getObject(final Class<T> clz, final String sql, final Object[] args) {
		return jdbcTemplate.query(sql, new ResultSetExtractor<T>() {
			public T extractData(ResultSet rs) throws SQLException, DataAccessException {
				final List<String> columms = getResultSetColumm(rs.getMetaData());
				if (rs.next()) {
					try {
						T t = clz.newInstance();
						Field[] fields = clz.getDeclaredFields();
						for (Field f : fields) {
							// 存在当前列
							String colnummName = SQLUtils.underscoreName(f.getName());
							if (columms.contains(colnummName)) {
								f.setAccessible(true);
								if (rs.getString(colnummName) != null) {
									f.set(t, rs.getObject(colnummName));
								}
							}
						}
						return t;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		}, args);
	}
	
	/**
	 * 返回多个对象
	 * @param clz
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> List<T> getListObject(final Class<T> clz, final String sql, final Object[] args) {
		return jdbcTemplate.query(sql, args, new RowMapper<T>() {
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				final List<String> columms = getResultSetColumm(rs.getMetaData());
				try {
					T t = clz.newInstance();
					Field[] fields = clz.getDeclaredFields();
					for (Field f : fields) {
						// 存在当前列
						String colnummName = SQLUtils.underscoreName(f.getName());
						if (columms.contains(colnummName)) {
							f.setAccessible(true);
							if (rs.getString(colnummName) != null) {
								f.set(t, rs.getObject(colnummName));
							}
						}
					}
					return t;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}
	
	/**
	 * 获取数据数量
	 * @param sql
	 * @param args
	 * @return
	 */
	public int getCounts(final String sql, final Object[] args) {
		return jdbcTemplate.query(sql, new ResultSetExtractor<Integer>() {
			public Integer extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					try {
						return rs.getInt("count");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return 0;
			}
		}, args);
	}
	
	/**
	 * 获取结果集中返回的字段名称
	 * @param metaData
	 * @return
	 * @throws SQLException 
	 */
	protected List<String> getResultSetColumm(ResultSetMetaData metaData) throws SQLException {
		List<String> columns = new ArrayList<String>();
		int num = metaData.getColumnCount();
		for (int i = 1; i <= num; i ++) {
			columns.add(metaData.getColumnName(i));
		}
		return columns;
	}
	

}
