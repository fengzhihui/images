package com.fzh.test;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.fzh.entity.User;
import com.fzh.jdbctemplate.JDBC;
import com.fzh.wx.pojo.Movie;
import com.fzh.wx.service.MovieService;
import com.fzh.wx.util.SignUtil;

public class MainTest extends JDBC {
	public static void main(String[] args) throws Exception {
		testShortStr();
		/*String str = "ab";
		int count = 0;
		String[] pids = null;
		User user = new User();
		StringBuffer sb = null;
		setValue(str, count, pids, user, sb);
		System.out.println(str);
		
		String s1 = new String("java");
		String s2 = new String("java");

		System.out.println(s1==s2);            //true
		System.out.println(s1.equals(s2));    //true
*/	
	}
	
	public static void testShortStr(){
		String str = "oBdGYjvcNbEkvrdWJ7r-vY0X3VwE";
		int hashcode = str.hashCode();
		BigInteger bi = new BigInteger(Math.abs(hashcode) + "");
		System.out.println(bi.toString(36));
		System.out.println(str + ", " + str.hashCode());
		System.out.println(new BigInteger("vwgkc4", 36));
	}
	
	public static void testSOKU() throws Exception {
		List<Movie> list = MovieService.getMovieBySOKU("功夫粤语");
		for(Movie mv : list) {
			System.out.println(mv.getTitle() + ", " + mv.getUrl());
		}
	}
	
	public static int getValue(int i) {
        int result = 0;
        switch (i) {
        case 1:
            result = result + i;
        case 2:
            result = result + i * 2;
        case 3:
            result = result + i * 3;
        }
        return result;
    }
	private static void setValue(String str,Integer count,String[] pids,User user,StringBuffer sb){
		str = "fzh";
		sb = new StringBuffer("hi ");
		sb.append("world");
		count = 9;
		user.setUsername("冯志辉");
		pids = new String[3];//重新创建则赋值失败
		for(int i=0;i<3;i++){
			pids[i] = "pid" + i; 
		}
	}
	
	public static void query(){
		Map<String, Object> map = getJdbc().queryForMap("select * from message_info where id= 1");
		System.out.println(map.toString());
	}
	
}
