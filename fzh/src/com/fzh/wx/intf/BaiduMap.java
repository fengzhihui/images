package com.fzh.wx.intf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.fzh.wx.pojo.respmsgpojo.Article;
import com.fzh.wx.service.APIUtilService;
import com.fzh.wx.util.DBManager;
import com.fzh.wx.util.MessageManager;

/**
 * 百度地图接口
 * @author fzh
 *
 */
public class BaiduMap {
	private static final StringBuffer bufone = new StringBuffer(); 
	private static final HttpClient client = new HttpClient();
	
	public static void main(String[] args) throws Exception {
//		System.out.println(searchByLocationAndPlace("23.127028,113.316122", "银行", "", "", 1));
//		System.out.println(MessageManager.getTravelCity("广州", "", ""));//图文消息
//		System.out.println(getTravelCity("绍兴"));
		//System.out.println(getScenic("guangzhou"));//zhujiang qinghuiyuan
//		System.out.println(baike("人心不古"));
//		System.out.println(getDataBySina("韩寒"));
//		String url = "http://www.kjson.com/weixin/api?key=095ae577cc33dc29352b673b8456e337&urls=http://mp.weixin.qq.com/s?__biz=MzAxNzMwNjE4MQ==&mid=203358740&idx=1&sn=3b528a4000d05de873eea9e877371aee#rd";
//		org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
//		System.out.println(doc);
	}
	public static void example(){
		String html = "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>";
		org.jsoup.nodes.Document doc = Jsoup.parse(html);//解析HTML字符串返回一个Document实现
		org.jsoup.nodes.Element link = doc.select("a").first();//查找第一个a元素

		String text = doc.body().text(); // "An example link"//取得字符串中的文本
		System.out.println(text);
		String linkHref = link.attr("href"); // "http://example.com/"//取得链接地址
		System.out.println(linkHref);
		String linkText = link.text(); // "example""//取得链接地址中的文本
		System.out.println(linkText);
		String linkOuterH = link.outerHtml();
		System.out.println(linkOuterH);
		    // "<a href="http://example.com"><b>example</b></a>"
		String linkInnerH = link.html(); // "<b>example</b>"//取得链接内的html内容
		System.out.println(linkInnerH);
	}
	
	/**
	 * 百度百科搜索
	 * @param word
	 * @return
	 * @throws IOException
	 */
	public static String baike(String word) throws IOException{
		String url = "http://baike.baidu.com/search/word?pic=1&sug=1&enc=utf8&word="+word;
		org.jsoup.nodes.Document doc = Jsoup.connect(url).timeout(3000).get();
		String content = "";
		 if(doc != null){
			 Elements ul = doc.select("ul[class=custom_dot  para-list list-paddingleft-1]").select("a");
			 if(ul.html().length() == 0){
				content = doc.select("div[class=card-summary-content]").text();
				 if(content.length() == 0){
					 content = doc.select("div[class=para]").text();
				 }
				content =  content.length()>0?content:"暂无数据！";
			 }else{
				 for (org.jsoup.nodes.Element tag : ul) {
					 org.jsoup.nodes.Element link = tag.select("a").first();
					 if(link != null){
						 String _link = "http://baike.baidu.com" + link.attr("href");
//					 System.out.println(_link);
//					 System.out.println(link.text());
						 content += link.text() + "\n" + baikeByLink(_link) + "\n\n";
						 if(content.length() > 1000){
							 break;
						 }
					 }
				 }
			 }
		 }
		 return content.length() > 1000 ? content.substring(0, 1000) + ". . ." : content;
	}
	
	public static String baikeByLink(String link) throws IOException {
		org.jsoup.nodes.Document doc = Jsoup.connect(link).timeout(3000).get();
		 if(doc != null){
			 String content = doc.select("div[class=card-summary-content]").text();
			 if(content.length() == 0){
				 content = doc.select("div[class=lemma-main-content rainbowlemma--]").text();
			 }
			 return content.length() >=0 ? content : "暂无数据！";
		 }
		 return null;
	}
	
	/**
	 * 地方搜索
	 * @param query
	 * @param fromUserName
	 * @param toUserName
	 * @param createTime
	 * @return
	 */
	public static String getAddressMessage(Map<String, String> requestMap) {
		GetMethod getMethod = null;
		String content = requestMap.get("Content").replaceAll("，", ",").trim();
		String mapUrl = "http://api.map.baidu.com/marker?output=html&location=";
		List<Article> articles = new ArrayList<Article>();
		try {
			String city = content.substring(0, content.indexOf(","));// 城市
			city = URLEncoder.encode(city, "UTF-8");
			String place = content.substring(content.indexOf(",") + 1,
					content.length());// 地方名
			place = URLEncoder.encode(place, "UTF-8");
			String url = "http://api.map.baidu.com/place/v2/search?output=json&ak=860a497074e46a24f7f42388dcf6c540&scope=2&q="
					+ place + "&region=" + city;
			// String url =
			// "http://api.map.baidu.com/place/v2/search?q=%C2%F3%B5%B1%C0%CD&region=%C3%AF%C3%FB&output=json&ak=860a497074e46a24f7f42388dcf6c540&scope=2";
			getMethod = new GetMethod(url);
			getMethod.getParams().setParameter("http.protocol.cookie-policy",
					CookiePolicy.BROWSER_COMPATIBILITY);
			int statusCode = client.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				InputStream inputStream = getMethod.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						inputStream, "UTF-8"));
				String str = "";
				while ((str = br.readLine()) != null) {
					bufone.append(str + System.getProperty("line.separator"));
				}
				str = null;
				br.close();
				inputStream.close();
			}
			String json = bufone.toString();
			if (json != null) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				if (jsonObject != null && jsonObject.has("results")) {
					JSONArray resultArr = jsonObject.getJSONArray("results");
					if (resultArr != null && resultArr.size() > 0) {
						for (int i = 0; i < resultArr.size(); i++) {
							String name = "", address = "", detail_url = "", location_X = "", location_Y = "", location = "";
							JSONObject tempJson = JSONObject.fromObject(resultArr.get(i));
							if (tempJson != null && tempJson.has("name"))
								name = tempJson.get("name").toString();
							if (tempJson != null && tempJson.has("address"))
								address = tempJson.get("address").toString();
							// if(tempJson!=null &&
							// tempJson.has("telephone"))telephone =
							// tempJson.get("telephone").toString();
							if (tempJson != null && tempJson.has("location")) {
								JSONObject lcObj = (JSONObject) tempJson
										.get("location");
								location_X = lcObj.get("lat").toString();
								location_Y = lcObj.get("lng").toString();
							}
							if (tempJson != null && tempJson.has("detail_info")) {
								JSONObject dtObj = (JSONObject) tempJson
										.get("detail_info");
								detail_url = dtObj.has("detail_url") ? dtObj
										.get("detail_url").toString() : "";
							}
							if (!"".equals(location_X)
									&& !"".equals(location_Y)
									&& !"".endsWith(name)) {
								location = location_X + "," + location_Y
										+ "&content=" + name;
							}

							if (!"".equals(name) && !"".equals(address)
									&& !"".equals(location)) {
								Article article = new Article();
								// article.setDescription("");
								article.setPicUrl("http://fzh2014.duapp.com/images/map.jpg");
								article.setTitle(name + " :\n " + address);
								if (!"".equals(detail_url))
									article.setUrl(detail_url);
								else
									article.setUrl(mapUrl + location
											+ "&title=" + name);
								articles.add(article);
							}
						}
					}
				}
			}
			if (articles.size() > 0) {
				return MessageManager.getNewsMessage(requestMap, articles);
			}
			bufone.setLength(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (getMethod != null)
				getMethod.releaseConnection();
			else
				getMethod = null;
		}
		return null;
	}
	 
	 /**
	  * 根据坐标或地方搜索
	  * @param location
	  * @param place
	  * @param fromUserName
	  * @param toUserName
	  * @param createTime
	  * @return
	  */
		public static String getMessageBySearchPlace(Map<String, String> requestMap) {
			String place = requestMap.get("place");
			List<Article> articles = new ArrayList<Article>();
			try{
				place = URLEncoder.encode(place, "UTF-8");
				String mapUrl = "http://api.map.baidu.com/marker?output=html&location=";
				String url = "http://api.map.baidu.com/place/v2/search?&query=args1&location=args2&radius=2000&scope=2&filter=sort_name:distance|sort_rule:1&output=xml&ak=860a497074e46a24f7f42388dcf6c540";
				
				org.jsoup.nodes.Document doc = Jsoup.connect(url.replaceAll("args1", place).replaceAll("args2", requestMap.get("location").toString())).get();
				 if(doc != null){
					 Elements imgs = doc.select("result");
					 for (org.jsoup.nodes.Element tag : imgs) {
						 String name = tag.getElementsByTag("name").text().trim();
						 String distance = tag.getElementsByTag("distance").text().trim();
//						 String address = tag.getElementsByTag("address").text().trim();
						 String telephone = tag.getElementsByTag("telephone").text().trim();
						 String lat = tag.getElementsByTag("lat").text().trim();
						 String lng = tag.getElementsByTag("lng").text().trim();
						 String latlng = lat +","+ lng;
						 
						 Article article = new Article();
					     article.setUrl(mapUrl+latlng+"&title="+name);
					     article.setPicUrl("http://fzh2014.duapp.com/images/map.jpg");
					     if(!"".equals(telephone)){
					    	 article.setTitle(name + "\n距离约" + distance + "米\n电话: " + telephone);
					     } else {
					    	 article.setTitle(name + "\n距离约" + distance + "米");
					     }
						 articles.add(article);
					 }
				 }
				 if(articles.size() > 0){
					 return MessageManager.getNewsMessage(requestMap, articles);
				 }
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * 城市旅游景点
		 * @param city
		 * @return
		 * @throws Exception  
		 */
		public static LinkedHashMap<String, String> getTravelCity(String city) {
			   GetMethod getMethod = null;
			   //初次调用接口，保存信息
			   LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		       try {
		    	   String url = "http://api.map.baidu.com/telematics/v3/travel_city?output=json&ak=140E6A73F89b353afe0df18433cf106f&location="+URLEncoder.encode(city,"UTF-8");
		      		getMethod = new GetMethod(url);
		      		getMethod.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
		      		int statusCode = client.executeMethod(getMethod);
		      		if(statusCode == HttpStatus.SC_OK){
		      			InputStream inputStream = getMethod.getResponseBodyAsStream();
		      			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
		      			String str = "";
		      			while((str = br.readLine()) != null){
		      				bufone.append(str+System.getProperty("line.separator"));
		      			}
		      			str = null;
		      			br.close();
		      			inputStream.close();
		      		}
		      		JSONObject jsonObject = JSONObject.fromObject(bufone.toString());
		      		if(jsonObject != null && jsonObject.has("result")){
		      			String link = "http://fzh2014.duapp.com/fzh/itineraries.jsp?";
		      			JSONObject jsonObj = jsonObject.getJSONObject("result");
		      			if(jsonObj != null){
		      				city = jsonObj.getString("cityname");//输入为高州，其隶属于茂名，百度接口自动转为茂名
		      				String cityUrl = jsonObj.getString("url");
		      				String cityId = jsonObj.getString("cityid");
		      				String cityAbstract = jsonObj.getString("abstract");
		      				cityAbstract = "".equals(cityAbstract)?"暂无介绍":cityAbstract;
		      				String location = jsonObj.getString("location");
		      				if(location != null){
		      					JSONObject loctObj = JSONObject.fromObject(location);
		      					if(loctObj != null){
		      						location = loctObj.getString("lng") + "," + loctObj.getString("lat");
		      					}
		      				}
		      				//保存城市信息
		      				Map<String, String> cityMap = new HashMap<String, String>();
		      				cityMap.put("url", cityUrl);
		      				cityMap.put("cityid", cityId);
		      				cityMap.put("cityname", city);
		      				cityMap.put("location", location);
		      				cityMap.put("abstract", cityAbstract);
		      				cityMap.put("description", jsonObj.getString("description"));
		      				cityMap.put("citypinyin", cityUrl.substring(cityUrl.lastIndexOf("/")+1, cityUrl.length()));
		      				//输入为高州，其隶属于茂名，百度接口自动转为茂名，故再查询一次
		      				boolean flag = false;
		      				if(!DBManager.getCity(city)){
		      					flag = true;
		      					DBManager.saveCityInfo(cityMap);
		      				}
		      				map.put(city + "：旅游攻略", cityUrl);
		      				
		      				//旅游类别（一日游，两日游。。）
		      				if(jsonObj.has("itineraries")){
		      					JSONArray array = jsonObj.getJSONArray("itineraries");
		      					for(int i = 0; array !=null && i < array.size(); i++){
		      						JSONObject jsonobj = array.getJSONObject(i);
		      						if(jsonobj != null){
		      							String info = "";
		      							String title = "【" + jsonobj.getString("name") + "】" + jsonobj.getString("description").trim();
		      							String str = "[{\"description\":\"desct\",\"dinning\":\"dinner\",\"accommodation\":\"acmd\",\"info\":\"istr\"}]";
		      							//旅游路线餐厅
		      							JSONArray arr = jsonobj.getJSONArray("itineraries");
		      							for (int j = 0; arr != null && j < arr.size(); j++) {
		      								JSONObject jsonO = arr.getJSONObject(j);
		      								String accommodation = "";
		      								if(jsonO != null){
		      									String dinning = jsonO.getString("dinning").replace("\"", "”").replace("\n", "<br>");
		      									String description = jsonO.getString("description").replace("\"", "”").replace("\n", "<br>");
		      									if(jsonO.has("accommodation")) accommodation = jsonO.getString("accommodation").replace("\"", "”").replace("\n", "<br>");
		      									str = str.replace("desct", description).replace("dinner", dinning).replace("acmd", accommodation);
		      									//景点详情
		      									JSONArray ar = jsonO.getJSONArray("path");
		      									for (int k = 0; ar != null && k < ar.size(); k++) {
		      										JSONObject object = ar.getJSONObject(k);
		      										if(object != null){
		      											String detail = object.getString("detail");
		      											String id = detail.substring(detail.indexOf("id="), detail.indexOf("output"));
		      											String pname = object.getString("name");
		      											if(!"null".equals(pname) && !info.contains(pname) && !info.contains(id)){
		      												info += pname.replace("\"", "”").replace("\n", "<br>") + "," + id;
		      											}
		      										}
		      									}
		      								}
		      							}
		      							int typeid = i + 1;
		      							str = str.replace("istr", info);
		      							map.put(title, link+"cityid="+cityId+"&typeid="+typeid);
		      							
		      							//保存景点行程信息
		      							Map<String, String> itinerariesMap = new HashMap<String, String>();
		      							itinerariesMap.put("cityid", cityId);
		      							itinerariesMap.put("typeid", typeid+"");
		      							itinerariesMap.put("itineraries", str);
		      							itinerariesMap.put("typename", jsonobj.getString("name"));
		      							itinerariesMap.put("description", jsonobj.getString("description"));
		      							if(flag){
		      								DBManager.saveItinerariesInfo(itinerariesMap);
		      							}
		      						}
		      					}
		      				}
		      				map.put("【城市印象】", cityAbstract);
		      				String weather = APIUtilService.getBAEWeather(city).replace("【", "").replace("】", "").replace(city, "").replace("天气实况", "");
		      				map.put("【城市天气】", weather.trim());
		      			}
		      		}
		      		bufone.setLength(0);
		       }catch(Exception e){
		    	   System.out.println("抓取出错"+e);
		       }finally{
		    	if(getMethod!=null) getMethod.releaseConnection();
				   else getMethod = null;
		       }
		       return map;
		}
		
		/**
		 * 景点详情
		 * @param id
		 * @throws IOException 
		 */
		public static Map<String, String> getScenic(String id) throws IOException{
			Map<String, String> map = new HashMap<String, String>();
			String url = "http://api.map.baidu.com/telematics/v3/travel_attractions?output=json&ak=140E6A73F89b353afe0df18433cf106f&id="+id;
			Document doc = Jsoup.connect(url).get();
			if(doc != null){
				JSONObject jsonObject = JSONObject.fromObject(doc.select("body").text());
				if(jsonObject != null && jsonObject.has("result")){
					JSONObject jsonObj = jsonObject.getJSONObject("result");
					if(jsonObj == null) return null;
					url = jsonObj.getString("url");
					String name = jsonObj.getString("name");
					String abstr = jsonObj.getString("abstract");
					String telephone = jsonObj.getString("telephone");
					String description = jsonObj.getString("description");
					String location = jsonObj.getString("location");
      				if(location != null){
      					JSONObject loctObj = JSONObject.fromObject(location);
      					if(loctObj != null){
      						location = loctObj.getString("lng") + "," + loctObj.getString("lat");
      					}
      				}
      				map.put("url", url);
      				map.put("name", name);
					map.put("location", location);
					map.put("description", description);
					map.put("abstract", abstr==null||"".equals(abstr) ? "无" : abstr);
					map.put("telephone", telephone==null||"".equals(telephone) ? "无" : telephone);
					
					if(jsonObj.has("ticket_info")){
						JSONObject jsonO =JSONObject.fromObject(jsonObj.getString("ticket_info"));
						if(jsonO != null){
							String price = jsonO.getString("price");
							String open_time = jsonO.getString("open_time");
							map.put("open_time", open_time==null||"".equals(open_time) ? "无" : open_time);
							if(jsonO.has("attention")){
								JSONArray arr = jsonO.getJSONArray("attention");
								for (int k = 0; arr != null && k < arr.size(); k++) {
									JSONObject object = arr.getJSONObject(k);
									if(object != null){
										String pname = object.getString("name")+"\n";			//优惠名称
										String desc = object.getString("description");				//优惠政策
										map.put("ticket_info", price==null||"".equals(price) ? "无" : price + "\n" + pname + "\n" + desc);
									}
								}
							}else{
								map.put("ticket_info", "无");
							}
						}
					}
				}
			}
			return map;
		}
		
		@SuppressWarnings("unchecked")
		public static void test() throws Exception {
			String url="http://api.map.baidu.com/telematics/v3/travel_city?output=xml&ak=140E6A73F89b353afe0df18433cf106f&location=%E6%B7%B1%E5%9C%B3";
			SAXReader saxReader = new SAXReader();
			org.dom4j.Document document = saxReader.read(url);
			Element telematics = (Element)document.getRootElement().element("result").element("itineraries");
			for(Iterator<Element> it = ((org.dom4j.Element) telematics).elementIterator("item"); it.hasNext();){
				Element element = (Element) it.next();
				String name = ((org.dom4j.Element) element).elementText("name");
				String description1 = ((org.dom4j.Element) element).elementText("description");
				System.out.println(name + description1);
			}
		}
}
