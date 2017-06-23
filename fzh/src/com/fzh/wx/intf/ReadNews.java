package com.fzh.wx.intf;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.fzh.jdbctemplate.JDBC;
import com.fzh.wx.pojo.respmsgpojo.Article;
import com.fzh.wx.util.MessageManager;
import com.fzh.wx.util.StringUtil;

public class ReadNews extends JDBC {
	
	/**
	 * 自定义菜单事件获取新闻列表
	 * @param fromUserName
	 * @param createTime
	 * @param toUserName
	 * @param eventKey
	 * @return
	 */
	public static String[] getArticles(Map<String, String> requestMap){
		String[] news = null;
		String respContent = "", ids = "", url = "", sql = "";
		try{
			if(requestMap.get("EventKey").equals("10")){
				sql = "select id,title from article where type='0' group by title order by time desc limit 20";
			}else if(requestMap.get("EventKey").equals("14")){
				sql = "select id,title from article where type='1' group by title order by time desc limit 20";
			}
			List<Map<String, Object>> list = getJdbc().queryForList(sql);
			int i = 1;
			for(Map<String, Object> article : list){
				if(i <= 8){
					respContent += i + ". " + article.get("title") +"\n";
				}
				ids += article.get("id") + ",";
				i ++;
			}
			
			String picUrl = "http://fzh2014.duapp.com/images/WALL.E.jpg";
			String title = "新闻导航";
			if(requestMap.get("EventKey").equals("10"))respContent = respContent+StringUtil.msg("1");//文章列表
            //设置看新闻的跳转链接
			if(requestMap.get("EventKey").equals("10")){
				url = "http://fzh2014.duapp.com/fzh/article.jsp?type=0&ids="+ids;
			}else{
				url = "http://fzh2014.duapp.com/fzh/article.jsp?type=1&ids="+ids;
			}
			Article article = new Article();
			List<Article> articles = new ArrayList<Article>();
		    article.setUrl(url);
		    article.setTitle(title);
			article.setPicUrl(picUrl);
			article.setDescription(respContent);
			articles.add(article);
			String respMessage = MessageManager.getNewsMessage(requestMap, articles);
			news = new String[]{ids, respMessage};
		}catch(Exception e){
			e.printStackTrace();
			Logger.getAnonymousLogger().info(e.getMessage());
		}
		return news;
	}
	
	/**
	 * 根据id查询文章
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static String getArticleById(String id) throws Exception {
		Map<String, Object> article = getJdbc().queryForMap("select content from article where id = ?", new Object[]{ id });
		String content = article.get("content").toString().replaceAll(".*<img(.*)>","").replaceAll("</br>", "");
		return content;
	}
	
	/**
	 * 查询用户输入内容
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String getByContent(String content) throws Exception {
		String sql = "select content from notebook where instr(content,?)>0 order by rand() limit 1";
		Map<String, Object> article = getJdbc().queryForMap(sql);
		return article.get("content").toString();
	}
	
	/**
	 * Json格式的文章
	 * @return
	 */
	public static String getArticlesJson() {
		String respContent = "";
		try {
			StringBuffer stringBuffer = new StringBuffer();

			//String focusArticles = JDBCTest.getFocusArticles(); // 焦点头条
			String amusementArticles = getArticlesByType("0");// 娱乐国际
			String stockArticles = getArticlesByType("1");         // 股票财经
			respContent = stringBuffer
					.append("{")
					.append(amusementArticles)
					//.append(focusArticles)
					.append(stockArticles)
					.append("}")
					.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respContent;
	}
	
	/**
	 * 根据文章类型获取文章
	 * @param type
	 * @return
	 */
	public static String getArticlesByType(String type){
		String jsonstr = "", title = "", content = "";
		String str = "{\\\"title\\\":\\\"titlestr\\\",\\\"content\\\":\\\"contstr\\\"},", typestr = "";
		if("0".equals(type)){//娱乐国际
			typestr = "\"amusement\":\"[";
		}else{                   //股票财经
			typestr = "\"stock\":\"[";
		}
		StringBuffer stringBuffer = new StringBuffer(typestr);
		String sql = "select id,title,content from article where type=? group by title order by time desc limit 15";//type=0为娱乐
		List<Map<String, Object>> list = getJdbc().queryForList(sql);
		for(Map<String, Object> article : list){
			title =  article.get("title").toString().trim();
			if("0".equals(type))content = article.get("content").toString().replaceAll("\r\n", "<br>").replaceAll("\n", "<br>");
			if("1".equals(type))content = article.get("content").toString().replaceAll(" 　　", "<br><br> 　　");
			String json = str.replaceAll("titlestr", title).replaceAll("contstr", content);
			stringBuffer.append(json);
		}
		jsonstr = stringBuffer.append("]\",").toString();
		return jsonstr;
	}
	
	/**
	 * 查看更多文章
	 * @param curtMaxId
	 * @param type
	 * @return
	 */
	public static String getMoreArticles(String curtMaxId, String type) {
		int minId = 0, maxId = 0;
		List<Map<String, Object>> list = null;
		String jsonstr = "", title = "", content = "", str = "{\"title\":\"titlestr\",\"content\":\"contstr\"},";
		
		String sql = "select title,content from article group by title order by time desc limit 10";
		
		if(curtMaxId !=null && !"".equals(curtMaxId)){
			minId = Integer.parseInt(curtMaxId) - 100;
			maxId = Integer.parseInt(curtMaxId) - 20;
			sql = "select title,content from article  where id > ? and id< ? and type = ? group by title order by time desc limit 10";
			list = getJdbc().queryForList(sql, new Object[]{ minId, maxId, type });
		} else {
			list = getJdbc().queryForList(sql);
		}
		
		StringBuffer stringBuffer = new StringBuffer("[");
		for(Map<String, Object> article : list){
			title = article.get("title").toString().trim();
			if("0".equals(type)){
				content = article.get("content").toString().replaceAll("\r\n", "<br>").replaceAll("\n", "<br>");
			}
			if("1".equals(type)){
				content = article.get("content").toString().replaceAll(" 　　", "<br><br> 　　");
			}
			String json = str.replaceAll("titlestr", title).replaceAll("contstr", content);
			stringBuffer.append(json);
		}
		jsonstr = stringBuffer.append("]").toString().replaceAll("},]", "}]");
		return jsonstr;
	}
	
	public static String getArticles(){
		String jsonstr = "", title = "", content = "", str = "{\"title\":\"titlestr\",\"content\":\"contstr\"},";
		String sql = "select title,content from article where type = '0' group by title order by time desc limit 20";
		
		List<Map<String, Object>> list = getJdbc().queryForList(sql);
		
		StringBuffer stringBuffer = new StringBuffer("[");
		for(Map<String, Object> article : list){
			title =  article.get("title").toString().trim();
			content = article.get("content").toString().replaceAll("\r\n", "<br>").replaceAll("\n", "<br>");
			String json = str.replaceAll("titlestr", title).replaceAll("contstr", content);
			stringBuffer.append(json);
		}
		jsonstr = stringBuffer.append("]").toString().replaceAll("},]", "}]");
		return jsonstr;
	}
	
	/**
	 * 获取文章内容
	 * @param ids
	 * @return
	 */
	public static LinkedHashMap<String, String> getContent(String ids) {
		ids = ids.substring(0, ids.length()-1);//去掉最后一个","
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		String sql = "select title,content from article where id in("+ids+") order by time desc";
		List<Map<String, Object>> list = getJdbc().queryForList(sql);
		for(Map<String, Object> article : list){
			map.put(article.get("title").toString(), article.get("content").toString().replaceAll(" 　　", "<br><br> 　　"));
		}
		return map;
	}
}
