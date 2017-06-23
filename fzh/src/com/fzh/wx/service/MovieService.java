package com.fzh.wx.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
import com.fzh.wx.pojo.Movie;

/**
 * 在线电影服务管理
 * @author fzh
 *
 */
public class MovieService {
	//D:/news/news/WebContent/movie/movie.html
	private static final String filePath = "D:/movie.html";
	
	/**
	 * 抓取电影数据 09-18
	 * @throws IOException
	 */
	public static void loadMovie() throws IOException {
		StringBuffer sbf = new StringBuffer("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><meta charset=\"UTF-8\">");
		//新数据
		for (int i = 1; i < 15; i++) {
			Document doc = Jsoup
					.connect("http://cd.tdwqf.com/" + i + ".html")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
					.timeout(25000).get();
			
			Elements elements = doc.select("ul[class=list_tab_img] li");
			sbf.append(elements);
			System.out.println("第" + i + "页：\n" + elements);
		}
		try {
//			//旧数据
			File in = new File(filePath);
			Document doc = Jsoup.parse(in, "UTF-8", "");
			Elements elements = doc.select("li");
			sbf.append(elements);//合并数据
			BufferedWriter writer = new BufferedWriter(new FileWriter(in));
			writer.write(sbf.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getNames() throws IOException{
		File in = new File(filePath);
		Document doc = Jsoup.parse(in, "UTF-8", "");
		Elements elements = doc.select("li");
		int i = 0;
		for (Element element : elements) {
			String href = element.select("a").attr("href").trim();
			if(href !=null && href.length() > 0){
				String link = href.replace("lose.wuxicsc.com/", "win.wuxicsc.com");
				String img = element.select("img").attr("data-original").trim();
				String title = element.select("label[class=title]").text().trim();
				String name = element.select("label[class=name]").text().trim();
				System.out.println(img);
				System.out.println(title + " , " + name + " , " + link);
				i ++;
				if(i == 100) break;
//				System.out.println("第" + i);
			}
		}
	}
	
	/**
	 * 获取本地电影
	 * @return
	 * @throws IOException
	 */
	public static List<Map<String, String>> getLocalMovies() throws IOException {
		List<Map<String, String>> allMovies = new ArrayList<Map<String,String>>();
		File in = new File(filePath);
		Document doc = Jsoup.parse(in, "UTF-8", "");
		Elements elements = doc.select("li");
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
	 * 查询电影并返回10部随机电影
	 * @param mvName
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getMovieByZHDY(String mvName, Object data) throws IOException{
		Map<String, String> map = new HashMap<String, String>();
		//统一大写处理
		mvName = mvName.toUpperCase();
		@SuppressWarnings("unchecked")
		List<Map<String, String>> mvList = (List<Map<String, String>>) data;
		Collections.shuffle(mvList);//打乱顺序搜索
		for(Map<String, String> mvMap : mvList){
			String link = "";
			//模糊搜索
			if(mvMap.get("name").contains(mvName)){
				link = mvMap.get("link");
				//2016-12-17 直接跳转播放URL
				//http://cd.tdwqf.com/film1/17649.html?woaijjjjj
				if(link !=null && link.contains(".html")){
					String movieId = link.substring(link.lastIndexOf("/") + 1 , link.lastIndexOf(".html"));
					Map<String, String> videoMap = MovieUtilService.getVideoById(mvList, movieId);
					if(videoMap.containsKey("video")) link = Const.VIDEO_HTML_URL + videoMap.get("video");
				}
				/*//旧版跳转页面再播放
				if(mvMap.get("link").contains(".html")){
					link = Const.ABYY_JSP_URL + mvMap.get("link").substring(mvMap.get("link").lastIndexOf("/") + 1 , mvMap.get("link").lastIndexOf(".html"));
				}*/
				map.put("link", link);
				map.put("img", mvMap.get("img"));
				map.put("name", mvMap.get("name"));
				map.put("desc", mvMap.get("desc"));
				//精确搜索到则终止搜索
				if(mvMap.get("name").equals(mvName)) break;
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
	 * 最火电影
	 * @throws IOException
	 */
	public static List<Map<String, String>> getAllZHDYMovies() throws IOException {
		List<Map<String, String>> allMovies = new ArrayList<Map<String,String>>();
		Document doc = Jsoup.connect(Const.MOVIE_DATA_URL).timeout(30000).get();
		Elements elements = doc.select("li");
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
	 * 烂蕃茄电影（无图片）
	 */
	public static Map<String, String> getMovieByLFQ(String movieName) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer bufferRes = new StringBuffer();
		try {
			URL realUrl = new URL("http://dycr001.6fuxuan.com/index.php?m=vod-search");
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// 连接超时
			conn.setConnectTimeout(25000);
			// 读取超时 --服务器响应比较慢，增大时间
			conn.setReadTimeout(25000);

			HttpURLConnection.setFollowRedirects(true);
			// 请求方式
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0");
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Referer", "http://dycr001.6fuxuan.com/index.php?m=vod-search");
			conn.connect();
			// 获取URLConnection对象对应的输出流
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			// 发送请求参数
			out.write("wd=" + URLEncoder.encode(movieName, "UTF-8"));
			out.flush();
			out.close();

			InputStream in = conn.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String valueString = null;
			while ((valueString = read.readLine()) != null) {
				bufferRes.append(valueString);
			}
			in.close();
			if (conn != null) {
				conn.disconnect();// 关闭连接
			}
		} catch (Exception e) {
			System.out.println("接口调用出错：" + e.getMessage());
		}
		Document doc = Jsoup.parse(bufferRes.toString());
		Elements elements = doc.select("div[class=article_list] a");
		//只搜索1部电影
		for(Element element : elements){
			map.put("img", "");//不显示此图片
			map.put("name", element.select("h2").text());
			map.put("desc", element.select("h2").text() + "\n喜欢记得分享哦~");
			map.put("link", "http://dycr001.6fuxuan.com/" + element.attr("href"));
			break;
		}
		return map;
	}
	
	/**
	 * 搜库电影资源
	 * @param movieName
	 * @return
	 * @throws Exception
	 */
	public static List<Movie> getMovieBySOKU(String movieName) throws Exception {
		List<Movie> movieList = new ArrayList<Movie>();
		int movieCount = 8;
		try{
			String url = Const.SOKU_SEARCH + URLEncoder.encode(movieName, "UTF-8");
			Document doc = Jsoup.connect(url).timeout(40000).get();
			if(doc != null){
				Elements movieArr = doc.select("div[class=v]");
				for (Element element : movieArr) {
					Movie movie = new Movie();
					movie.setUrl("http:" + element.select(".v-meta .v-title a").attr("href").trim());
					movie.setTitle(element.select(".v-meta .v-title a").attr("title").trim());
					movie.setImage(element.select(".v-thumb .v-pic img").attr("src").trim());
					//不可播放的链接
					if(movie.getUrl().contains("list.youku.com") || movie.getUrl().contains("/detail/show/")){
						continue;
					}
					if(movieList.size() == movieCount) break;
					movieList.add(movie);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return movieList;
	}
	
}
