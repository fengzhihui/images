package com.fzh.wx.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.fzh.common.Const;

/**
 * 在线电影服务管理
 * @author fzh
 *
 */
public class MovieUtilService {

	public static void main(String[] args) throws Exception {
		
	}
	
	/**
	 * 查询电影并返回10部随机电影
	 * @param movieName
	 * @param movieType
	 * @return 
	 * @throws IOException 
	 */
	public static Map<String, String> getMovieByZHDY(String movieName,String movieType) throws IOException{
		Map<String, String> map = new HashMap<String, String>();
		List<Map<String, String>> mvList = getAllZHDYMovies(movieType);
		Collections.shuffle(mvList);//打乱顺序搜索
		for(Map<String, String> mvMap : mvList){
			String link = "";
			//模糊搜索
			if(mvMap.get("name").contains(movieName)){
				link = mvMap.get("link");
				if(mvMap.get("link").contains(".html")){
					link = Const.ABYY_JSP_URL + mvMap.get("link").substring(mvMap.get("link").lastIndexOf("/") + 1 , mvMap.get("link").lastIndexOf(".html"));
				}
				map.put("link", link);
				map.put("img", mvMap.get("img"));
				map.put("name", mvMap.get("name"));
				map.put("desc", mvMap.get("desc"));
				//精确搜索到则终止搜索
				if(mvMap.get("name").equals(movieName)) break;
			}
		}
		//随机10部电影
		String names = "";
		for(int i=0; i<10; i++){
			names += mvList.get(i).get("name") + "；";
		}
		names = "10部最新电影名单：\n" + names + "\n（电影名前一定要加电影两字才能搜到哦！）";
		map.put("names", names);
		return map;
	}
	
	/**
	 * 根据电影ID查询
	 * @param movieId
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getMovieById(List<Map<String, String>> mvList,String movieId) throws IOException{
		Map<String, String> map = new HashMap<String, String>();
		Collections.shuffle(mvList);//打乱顺序搜索
		for(Map<String, String> mvMap : mvList){
			//只搜索1部电影
			if(mvMap.get("link").contains(movieId)){
				map.put("name", mvMap.get("name"));
				map.put("link", mvMap.get("link"));
				map.put("img", mvMap.get("img").contains("http")?mvMap.get("img") : ("http://weixin.mvduo.com" + mvMap.get("img")));
				break;
			}
		}
		//3部随机电影
		String link1 = mvList.get(1).get("link"), link2 = mvList.get(2).get("link"), link3 = mvList.get(3).get("link");
		link1 = link1.contains("film1") ? link1.substring(link1.indexOf("film1")+6, link1.lastIndexOf(".html")) : "http://mp.weixin.qq.com/s?__biz=MzAxODk3MDA0Mw==&mid=100000003&idx=1&sn=836ed91a91779b484955f06e69ac30c0#rd";
		link2 = link2.contains("film1") ? link2.substring(link2.indexOf("film1")+6, link2.lastIndexOf(".html")) : "http://mp.weixin.qq.com/s?__biz=MzAxODk3MDA0Mw==&mid=100000003&idx=1&sn=836ed91a91779b484955f06e69ac30c0#rd";
		link3 = link3.contains("film1") ? link3.substring(link3.indexOf("film1")+6, link3.lastIndexOf(".html")) : "http://mp.weixin.qq.com/s?__biz=MzAxODk3MDA0Mw==&mid=100000003&idx=1&sn=836ed91a91779b484955f06e69ac30c0#rd";
		map.put("name1", mvList.get(1).get("name"));
		map.put("link1", link1);
		map.put("img1", mvList.get(1).get("img").contains("http")?mvList.get(1).get("img") : ("http://weixin.mvduo.com" + mvList.get(1).get("img")));
		map.put("name2", mvList.get(2).get("name"));
		map.put("link2", link2);
		map.put("img2", mvList.get(2).get("img").contains("http")?mvList.get(2).get("img") : ("http://weixin.mvduo.com" + mvList.get(2).get("img")));
		map.put("name3", mvList.get(3).get("name"));
		map.put("link3", link3);
		map.put("img3", mvList.get(3).get("img").contains("http")?mvList.get(3).get("img") : ("http://weixin.mvduo.com" + mvList.get(3).get("img")));
		return map;
	}
	
	/**
	 * 根据电影ID查询播放源
	 * @param mvList
	 * @param movieId
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getVideoById(List<Map<String, String>> mvList,String movieId) throws IOException{
		Map<String, String> map = new HashMap<String, String>();
		Collections.shuffle(mvList);//打乱顺序搜索
		for(Map<String, String> mvMap : mvList){
			//只搜索1部电影
			if(mvMap.get("link").contains(movieId)){
				map.put("name", mvMap.get("name"));
				map.put("link", mvMap.get("link"));
				map.put("img", mvMap.get("img").contains("http")?mvMap.get("img") : ("http://weixin.mvduo.com" + mvMap.get("img")));
				break;
			}
		}
		//获取播放链接
		Document doc = Jsoup.connect(Const.VIDEO_URL.replace("MOVIEID", movieId)).timeout(30000).get();
		String source = doc.select("video").attr("src");
		if(source != null){
			if(source.contains("type=mp4")){
//				source = Const.VIDEO_HTML.replace("VIDEO_URL", source);
			}else if(!"".equals(doc.select("iframe").attr("src"))){
//				source = Const.IFRAME_HTML.replace("IFRAME_HTML", doc.select("iframe").attr("src"));
				source = doc.select("iframe").attr("src");
			}else{
//				source = Const.IFRAME_HTML.replace("IFRAME_HTML", source);
			}
		}
		map.put("video", source);
		return map;
	}
	
	/**
	 * 10部随机电影
	 * @return
	 * @throws IOException
	 */
	public static String getMovieNames() throws IOException {
		Document doc = Jsoup.connect(Const.MOVIE_DATA_URL).timeout(30000).get();
		Elements elements = doc.select("li a label[class=name]");
		List<String> list = new ArrayList<String>();
		for (Element element : elements) {
			list.add(element.text());
		}
		Collections.shuffle(list);
		String names = "";
		for(int i=0; i<10; i++){
			names += list.get(i) + "；";
		}
		return "10部最新电影名单：\n" + names + "\n（电影名前一定要加电影两字才能搜到哦！）";
	}
	
	/**
	 * 所有电影
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static List<Map<String, String>> getAllZHDYMovies(String type) throws IOException {
		Document doc = Jsoup.connect("mvlist".equals(type)?Const.MOVIE_DATA_URL:Const.MOVIE_AV_URL).timeout(30000).get();
		Elements elements = doc.select("li");
		List<Map<String, String>> allMovies = new ArrayList<Map<String,String>>();
		for (Element element : elements) {
			String href = element.select("a").attr("href").trim();
			if(href !=null && href.length() > 0){
				String img = element.select("img").attr("data-original").trim();
				String desc = element.select("label[class=title]").text().trim();
				String name = element.select("label[class=name]").text().trim().toUpperCase();
				Map<String, String> movies = new HashMap<String, String>();
				movies.put("name", name);//电影名称
				movies.put("link", href.replace(" ", "%20"));//电影链接
				movies.put("desc", desc);//电影描述（如：完结）
				movies.put("img", img.contains("http")? img : ("http://weixin.mvduo.com" + img));
				allMovies.add(movies);
			}
		}
		return allMovies;
	}
	
	/**
	 * 获取前20页电影（每页15部）
	 * @param page
	 * @return
	 * @throws IOException
	 */
	public static List<Map<String, String>> getTop20PageMovies(List<Map<String, String>> mvList,int page) throws IOException {
		/*Document doc = Jsoup.connect(Const.MOVIE_DATA_URL).timeout(30000).get();
		Elements elements = doc.select("li");
		int count = 0;
		for (Element element : elements) {
			String href = element.select("a").attr("href").trim();
			if(href !=null && href.length() > 0){
				if(href.contains(".html")){
					href = Const.ABYY_JSP_URL + href.substring(href.lastIndexOf("/") + 1 , href.lastIndexOf(".html"));
				}
				String img = element.select("img").attr("data-original").trim();
				String name = element.select("label[class=name]").text().trim();
				String desc = element.select("label[class=title]").text().trim();
				Map<String, String> movies = new HashMap<String, String>();
				movies.put("sTit", name);//名称
				movies.put("sNum", desc);//描述
				movies.put("link", href);
				movies.put("img", img.contains("http")? img : ("http://weixin.mvduo.com" + img));
				mvList.add(movies);
				count ++;
				if(count == 300) break;
			}
		}*/
		mvList = mvList.subList(page*15, page==19 ? 300 : 15*(page+1));
		//System.out.println(mvList);
		return mvList;
	}
	
	/**
	 * 随机10个AV播放URL
	 * @param data
	 * @return
	 * @throws IOException  
	 */
	public static List<String> getAVlist(Object data) throws IOException {
		List<String> list = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, String>> mvList = (List<Map<String, String>>) data;
		Collections.shuffle(mvList);//打乱顺序搜索
		for(Map<String, String> mvMap : mvList.subList(0, 20)){
			String link = mvMap.get("link");
			link = mvMap.get("link");
			if(link !=null && link.contains(".html")){
				String movieId = link.substring(link.lastIndexOf("/") + 1 , link.lastIndexOf(".html"));
				Map<String, String> videoMap = getVideoById(mvList, movieId);
				if(videoMap.containsKey("video")) link = Const.VIDEO_HTML_URL + videoMap.get("video");
			}
			list.add(link);
			if(list.size() == 10) break;
		}
		return list;
	}
	
	
}
