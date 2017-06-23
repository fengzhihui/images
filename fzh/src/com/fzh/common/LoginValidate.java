package com.fzh.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证类级别的Controller
 * 用户是否需要登陆使用
 * @author fzh
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginValidate {
	/**
	 * true: 需要登录 
	 * false: 不需要登录
	 */
	boolean value() default true;
}
