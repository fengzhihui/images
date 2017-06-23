package com.fzh.util;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fzh.jdbctemplate.Column;
import com.fzh.wx.util.StringUtil;

public class SQLUtils {

	private static final Logger logger = LoggerFactory.getLogger(SQLUtils.class);

	/**
	 * 创建insert语句及参数
	 * @param <T>
	 * @param t
	 * @param sql
	 * @param args
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> void createInsert(T t, StringBuffer sql, List<Object> args)
			throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = t.getClass().getDeclaredFields();
		String tableName = t.getClass().getSimpleName();
		sql.append("INSERT INTO ").append(underscoreName(tableName)).append(" (");
		StringBuffer values = new StringBuffer(") VALUES (");
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.get(t) != null) {
				// 字段上是否有注解
				if(field.isAnnotationPresent(Column.class)){
					// 输出注解属性
					Column colunm = field.getAnnotation(Column.class);
					if(!colunm.value()) continue;
				}
				sql.append(underscoreName(field.getName())).append(",");
				values.append("?,");
				args.add(field.get(t));
			}
		}
		// 删除最后一个字符
		sql.deleteCharAt(sql.length() - 1);
		values.deleteCharAt(values.length() - 1);
		sql.append(values).append(")");
		logger.info("sql=" + sql.toString());
	}

	/**
	 * 创建update语句及参数
	 * @param <T>
	 * @param t
	 * @param sql
	 * @param args
	 * @param key
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> void createUpdate(T t, StringBuffer sql, List<Object> args, String key)
			throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = t.getClass().getDeclaredFields();
		String tableName = t.getClass().getSimpleName();
		sql.append("UPDATE ").append(underscoreName(tableName)).append(" SET ");
		String keyStr = null;
		Object keyValue = null;
		for (Field field : fields) {
			field.setAccessible(true);
			// 字段是否有值
			if (field.get(t) != null) {
				// 字段上是否有注解
				if(field.isAnnotationPresent(Column.class)){
					// 输出注解属性
					Column colunm = field.getAnnotation(Column.class);
					if(!colunm.value()) continue;
				}
				String colnummName = underscoreName(field.getName());
				if (StringUtil.isBlank(key) && "id".equals(colnummName)) {
					keyStr = "id";
					keyValue = field.get(t);
					continue;
				}
				if (!StringUtil.isBlank(key) && key.equals(colnummName)) {
					keyStr = key;
					keyValue = field.get(t);
					continue;
				}
				sql.append(colnummName).append("=?,");
				args.add(field.get(t));
			}
		}
		args.add(keyValue);
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" WHERE ").append(keyStr).append("=?");
		logger.info("sql=" + sql.toString());
	}

	/**
	 * 创建删除语句
	 * @param <T>
	 * @param clz
	 * @param key
	 * @param sql
	 */
	public static <T> void createDelete(Class<T> clz, String key, StringBuffer sql) {
		sql.append("DELETE FROM ").append(underscoreName(clz.getSimpleName())).append(" WHERE ");
		String keyStr = StringUtil.isBlank(key) ? "id" : key;
		sql.append(keyStr).append("=?");
	}

	/**
	 * 根据指定key获取对象
	 * @param <T>
	 * @param clz
	 * @param sql
	 * @param key
	 * @param fields
	 */
	public static <T> void createGetByKey(Class<T> clz, StringBuffer sql, String key, String fields) {
		sql.append("SELECT ").append(StringUtil.isBlank(fields) ? "*" : fields);
		sql.append(" FROM ").append(underscoreName(clz.getSimpleName())).append(" WHERE ");
		String keyStr = StringUtil.isBlank(key) ? "id" : key;
		sql.append(keyStr).append("=?");
	}
	
	/**
	 * 将驼峰式命名的字符串转换为下划线小写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。
	 * 例如：HelloWorld->hello_world
	 * @param name 转换前的驼峰式命名的字符串
	 * @return 转换后下划线小写方式命名的字符串
	 */
	public static String underscoreName(String name) {
	    StringBuilder result = new StringBuilder();
	    if (!StringUtil.isBlank(name)) {
	    	// 循环处理字符
	        for (int i = 0; i < name.length(); i++) {
	            String s = name.substring(i, i + 1);
	            // 在大写字母前添加下划线
	            if (i > 0 && s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
	                result.append("_");
	            }
	            result.append(s);
	        }
	    }
	    return result.toString().toLowerCase();
	}
}
