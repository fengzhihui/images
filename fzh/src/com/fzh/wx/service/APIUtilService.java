package com.fzh.wx.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.fzh.common.ApiConst;
import com.fzh.wx.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * API工具服务类
 * @author fzh
 *
 */
public class APIUtilService {
	private static final HttpClient client = new HttpClient();
	private static final StringBuffer bufone = new StringBuffer();
	
	/**
	 * 百度天气
	 * @param city
	 * @return
	 */
	public static String getBAEWeather(String city) {
		StringBuffer sbf = new StringBuffer();
	    try {
       	    city = city.replaceAll("天气", "").replaceAll("市", "");
       	    String url = ApiConst.BAE_WEATHER_URL + URLEncoder.encode(city, "UTF-8");
       	    
       	    getReuestData(url);
       	    
      		JSONObject json = JSONObject.fromObject(bufone.toString());
      		if(json == null || !json.has("results")) return "暂无数据";
      		
			JSONArray array = json.getJSONArray("results");
			if(array == null || array.size() < 0) return "暂无数据";
			
			JSONObject obj = array.getJSONObject(0);
			if(obj == null) return "暂无数据";
			
			JSONArray arr = obj.getJSONArray("weather_data");
			for (int i = 0; i < arr.size(); i++) {
				JSONObject o = (JSONObject) arr.get(i);
				if(o == null) return "暂无数据";
				if(i == 0){
					sbf.append("【").append(city).append("】").append("天气实况\n")
					   .append(o.get("date")).append(" 温度：").append(o.get("temperature"))
					   .append(" 天气：").append(o.get("weather"))
					   .append(" 风速：").append(o.get("wind")).append("\n");
				} else {
					sbf.append(o.get("date")).append(" ").append(o.get("temperature"))
					   .append(o.get("weather")).append(" ").append(o.get("wind")).append("\n");
				}
			}
      		bufone.setLength(0);
	    }catch(Exception e){
	    	System.out.println("抓取出错"+e);
	    }
	    return sbf.toString();
	}
	
	/**
	 * 随机获取一首歌曲
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getMusicByRand(String name) throws Exception {
		Map<String, String> map = null;
        JSONObject json = getMusicList(name);
        if(json.containsKey("song")){
        	//随机获取歌曲列表
        	JSONArray arr = json.getJSONArray("song");
        	int num = StringUtil.getRandNum(arr.size());
			json = JSONObject.fromObject(arr.get(num));
			if (StringUtil.isBlank(json.getString("songid"))) return null;
			String musicUrl = getMp3Url(json.getString("songid"));
			if(musicUrl == null) return null;
			map = new HashMap<String, String>();
			map.put("musicUrl", musicUrl);
			map.put("title", json.getString("songname"));
			map.put("author", json.getString("artistname"));
        }
		return map;
	}
	
	public static JSONObject getMusicList(String name) throws Exception {
		String url = ApiConst.BAIDU_TING_URL + URLEncoder.encode(name, "UTF-8");
        return getJsonByJsoup(url);
	}
	
	/**
	 * 获取歌曲播放链接
	 * @param songid
	 * @return
	 * @throws Exception 
	 */
	public static String getMp3Url(String songid) throws Exception {
		String url = ApiConst.BAIDU_TING_PLAY_URL + songid;
		JSONObject json = getJsonByJsoup(url);
		if (json.containsKey("bitrate")) {
			json = JSONObject.fromObject(json.getString("bitrate"));
			return json.getString("file_link");
		}
		return null;
	}
	
	/**
	  * 有道翻译
	  * @param word
	  * @return
	  */
	 public static String getTranslation(String word) {
  		try{
  			String url = ApiConst.YOUDAO_URL + URLEncoder.encode(word, "UTF-8");
  			getReuestData(url);
  	   		String json = bufone.toString();
  	   		if(StringUtil.isBlank(json)) return null;
  	   		
  	   		//对应的翻译
   			JSONObject translation = JSONObject.fromObject(json);
   		    if(!translation.containsKey("translation")) return null;

   		    String tslt = translation.getString("translation"), phonetic = null, explains = null;
	    	tslt = tslt.replaceAll("\\[\"", "").replaceAll("\"\\]", "") + "\n";
		    
   		    //json字符串 (基本意义,包括音标和解释)
   			JSONObject basic = JSONObject.fromObject(json);      
   			if(basic != null && basic.containsKey("basic")){
   				JSONObject basicStr = (JSONObject)basic.get("basic");
   				if(basicStr == null) return null;
				if(basicStr.containsKey("phonetic")){
					phonetic = "[" + basicStr.getString("phonetic") + "]" + "\n";
				}
				if(basicStr.containsKey("explains")){
					explains = basicStr.getString("explains");
					explains = explains.replaceAll("\\[\"", "").replaceAll("\"\\]", "").replaceAll("\"", "");
				}
   			}
   			bufone.setLength(0);
   		    return tslt + phonetic + explains;
  		}catch(Exception e){
  			e.printStackTrace();
  		}
  		return null;
	 }
	
	 /**
	   * 公交查询
	   * @param origin      具体的起点
	   * @param destination 城市名
	   * @param region      具体的终点
	   * @return
	   */
	public static List<String> getBUS(String origin, String destination, String region) {
		InputStream is = null;
		XMLInputFactory factory = XMLInputFactory.newInstance();
		List<String> slist = new ArrayList<String>();
		slist.add("为您查询到以下几种方案，仅供参考");
		try {
			origin = URLEncoder.encode(origin, "UTF-8");
			region = URLEncoder.encode(region, "UTF-8");
			destination = URLEncoder.encode(destination, "UTF-8");
			String url = String.format(ApiConst.BAE_BUS_URL, origin, destination, region);
			
			GetMethod getMethod = new GetMethod(url);
			getMethod.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
			int statusCode = client.executeMethod(getMethod);
			if(statusCode == HttpStatus.SC_OK){
				is = getMethod.getResponseBodyAsStream();   
			}
			//基于Filter的过滤方式，可以有效的过滤掉不用进行操作的节点，效率会高一些
			XMLEventReader reader = factory.createFilteredReader(factory.createXMLEventReader(is),
				new EventFilter() {
					@Override
					public boolean accept(XMLEvent event) {
						//返回true表示会显示，返回false表示不显示
						if(event.isStartElement()) {
							String name = event.asStartElement().getName().toString();
							if(name.equals("stepInstruction")||name.equals("remark")){
								return true;
							}
						}
						return false;
					}
			});
			
			String str = "", stepInstruction = "";
			while(reader.hasNext()) {
				//通过XMLEvent来获取是否是某种节点类型
				XMLEvent event = reader.nextEvent();
				if(event.isStartElement()) {
					//通过event.asxxx转换节点
					String name = event.asStartElement().getName().toString();
					if(name.equals("stepInstruction")) {
						String etxt = reader.getElementText();
						if(!StringUtil.isBlank(etxt)){
							stepInstruction = etxt.replaceAll("<.*?>", "").trim() + "-->";
							str += stepInstruction;
							if(stepInstruction.contains("终点站")){
								slist.add(str.substring(0, str.length()-3));
								str = "";
							}
						}
					}
					if(name.equals("remark")) {
						String remark = reader.getElementText();
						if(!StringUtil.isBlank(remark)){
							slist.add("(打的)" + remark.trim());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(is!=null) is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return slist;
	}
	
	/**
	 * 搜索音乐
	 * @param title
	 * @param author
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String getMusicByXml(String title,String author) throws Exception {
		if(StringUtil.isBlank(title) || StringUtil.isBlank(author)) return null;
		String musicUrl = "";
		String url = String.format(ApiConst.BAE_MUSIC_URL, URLEncoder.encode(title, "UTF-8"), URLEncoder.encode(author, "UTF-8"));
		//使用dom4j读取XML配置文件
		try {
			SAXReader reader = new SAXReader();
			org.dom4j.Document document = reader.read(url);
			org.dom4j.Element element = document.getRootElement();
			if(element != null){
				/**
				 * result: 对应XML文件中的result元素节点
				 * url：对应XML文件中result元素节点下的url元素节点
				 * encode：对应XML文件中result元素节点下的encode元素节点
				 * decode：对应XML文件中result元素节点下的decode元素节点
				 * type：对应XML文件中result元素节点下的type元素节点
				 */
				for (Iterator<org.dom4j.Element> iter = element.elementIterator("url"); iter.hasNext();) {
					org.dom4j.Element xmlElement = iter.next();
					if(xmlElement != null){
						String encode = xmlElement.elementText("encode");
						String decode = xmlElement.elementText("decode");
//						String type = xmlElement.elementText("type");//1为rm,2、8为mp3
						if(!"".equals(encode) && !"".equals(decode)){
							encode = encode.substring(0, encode.lastIndexOf("/")).trim();
							if(decode.contains("&")){
								decode = decode.substring(0, decode.lastIndexOf("&")).trim();
							}
							musicUrl = encode + "/" + decode;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return musicUrl;
	}
	
	/**
	 * 请求数据
	 * @param url
	 */
	public static void getReuestData(String url) {
		GetMethod getMethod = null;
		try {
			getMethod = new GetMethod(url);
	   		getMethod.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
	   		int statusCode = client.executeMethod(getMethod);
	   		if(statusCode == HttpStatus.SC_OK){
	   			InputStream inputStream = getMethod.getResponseBodyAsStream();   
	   			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	   			String str = "";
	   			while((str = br.readLine()) != null){
	   				bufone.append(str+System.getProperty("line.separator"));
	   			}
	   			str = null;
	   			br.close();
	   			inputStream.close();
	   		}
		}catch(Exception e){
	    	System.out.println("抓取出错"+e);
	    }finally{
	    	if(getMethod != null){
	    		getMethod.releaseConnection();
	    	} else {
	    		getMethod = null;
	    	}
	    }
	}
	
	public static String talk(String msg) throws Exception {
		String url = ApiConst.TALK_URL + URLEncoder.encode(msg, "UTF-8");
		JSONObject json = getJsonByJsoup(url);
		return !"0".equals(json.getString("result")) ? null : json.getString("content").replaceAll("\\{br\\}", "\n");
	}
	
	public static JSONObject getJsonByJsoup(String url) throws Exception {
		Document doc = Jsoup.connect(url).timeout(10000).ignoreContentType(true).get();
		Elements elements = doc.getElementsByTag("body");
		return elements == null ? null : JSONObject.fromObject(elements.get(0).text());
	}
}
