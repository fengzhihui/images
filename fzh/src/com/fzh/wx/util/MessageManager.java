package com.fzh.wx.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.math.RandomUtils;

import com.fzh.common.Const;
import com.fzh.entity.ImageTextInfo;
import com.fzh.entity.ImageTextMore;
import com.fzh.entity.MessageInfo;
import com.fzh.entity.UserInfo;
import com.fzh.wx.intf.BaiduMap;
import com.fzh.wx.pojo.Movie;
import com.fzh.wx.pojo.TravelCity;
import com.fzh.wx.pojo.respmsgpojo.Article;
import com.fzh.wx.pojo.respmsgpojo.Music;
import com.fzh.wx.pojo.respmsgpojo.MusicMessage;
import com.fzh.wx.pojo.respmsgpojo.NewsMessage;
import com.fzh.wx.service.APIUtilService;
import com.fzh.wx.service.MovieService;
import com.fzh.wx.service.MovieUtilService;

public class MessageManager {
	/**
	 * 获取图文信息
	 * @param requestMap
	 * @param imageTextInfo
	 * @param imagetextMoreList
	 * @return
	 * @throws Exception
	 */
	public static String getImageTextInfo(Map<String, String> requestMap, ImageTextInfo imageTextInfo, List<ImageTextMore> imagetextMoreList)throws Exception{
		String picUrl = Const.IMAGE_URL + imageTextInfo.getImageUrl();
		String clickUrl = Const.MAIN_CLICK_URL + imageTextInfo.getImageTextNo();
		
		List<Article> articleList = new ArrayList<Article>();
		Article article = new Article();
		article.setPicUrl(picUrl);						  //图片URL
		article.setUrl(clickUrl);						  //点击图文消息跳转链接
		article.setTitle(imageTextInfo.getTitle()); 	  //图文消息标题 
		article.setDescription(imageTextInfo.getDigest());//图文消息描述
	
		if(!StringUtil.isBlank(imageTextInfo.getClickOutUrl())){
			article.setUrl(imageTextInfo.getClickOutUrl() + "?openid=" + requestMap.get("FromUserName")); //如果外链不为空则为外链
		}
		articleList.add(article);
		
		//判断是否多图文
		if("2".equals(imageTextInfo.getImageTextType())){
			for(ImageTextMore textMore : imagetextMoreList){
				article = new Article();
				article.setTitle(textMore.getTitle()); //图文消息标题 
				 
				picUrl = Const.IMAGE_URL + textMore.getImageUrl();
				clickUrl = Const.MORE_CLICK_URL + textMore.getMoreImageTextNo();
				article.setUrl(clickUrl);//点击图文消息跳转链接
				article.setPicUrl(picUrl);//图片链接
				article.setDescription(textMore.getDigest());//图文消息描述
				if(!StringUtil.isBlank(textMore.getClickOutUrl())){
					article.setUrl(textMore.getClickOutUrl() + "?openid=" + requestMap.get("FromUserName")); //如果外链不为空则为外链
				}
				articleList.add(article);
			}
		}
		return getNewsMessage(requestMap, articleList);
	}
	
	/**
	 * 回复关键字电影图文
	 * @param requestMap
	 * @param imageTextInfo
	 * @param movieNames
	 * @return
	 */
	public static String getMovieNewsMessage(Map<String, String> requestMap, ImageTextInfo imageTextInfo, String movieNames) {
		List<Article> articleList = new ArrayList<Article>();
		//电影图文
		Article article = new Article();
		article.setPicUrl(imageTextInfo.getImageUrl());   //图片URL
		article.setUrl(imageTextInfo.getClickOutUrl());   //点击图文消息跳转链接
		article.setTitle(imageTextInfo.getTitle()); 	  //图文消息标题 
		article.setDescription(imageTextInfo.getDigest());//图文消息描述
		articleList.add(article);
		//1元打赏
		article = new Article();
		article.setUrl("http://mp.weixin.qq.com/s?__biz=MzAxODk3MDA0Mw==&mid=100000003&idx=3&sn=43286afe089abc833214da0800d020ea#rd");
		article.setTitle(StringUtil.getTitle());
		article.setPicUrl("http://mmbiz.qpic.cn/mmbiz/FAahqknuAWlen3HL6SS6J7D79gPN42aiaAFkicwcvqyicnicPLwrcZpAGUgMe9w6tZg8lVGpsV6zAIbY9t1MB8cVwg/0?wx_fmt=jpeg");
		article.setDescription("阿伯影院 微信号：abyy008 （观看电影请回复电影名，如：潘金莲）");
		articleList.add(article);
		//公号互推
		article = new Article();
		article.setUrl("http://mp.weixin.qq.com/s?__biz=MzAxODk3MDA0Mw==&mid=100000003&idx=3&sn=43286afe089abc833214da0800d020ea#rd");
		article.setTitle("关注更多公众号，长期免费观看更多好电影！");
		article.setPicUrl("http://mmbiz.qpic.cn/mmbiz/FAahqknuAWlen3HL6SS6J7D79gPN42aiaAFkicwcvqyicnicPLwrcZpAGUgMe9w6tZg8lVGpsV6zAIbY9t1MB8cVwg/0?wx_fmt=jpeg");
		article.setDescription("阿伯影院 微信号：abyy008 （观看电影请回复电影名，如：潘金莲）");
		articleList.add(article);
		//电影名字
		article = new Article();
		article.setTitle("10部随机电影名单：\n" + movieNames + "\n观看电影请回复电影名");
		article.setDescription("阿伯影院 微信号：abyy008 （观看电影请回复电影名，如：潘金莲）");
		articleList.add(article);
		return getNewsMessage(requestMap, articleList);
	}
	
	/**
	 * 消息记录
	 * @param requestMap
	 * @param imageTextNo
	 * @param remsgType
	 * @param url
	 * @param keyword
	 * @return
	 */
	public static MessageInfo createMessageInfo(Map<String, String> requestMap,Long imageTextNo,String remsgType,String url,String keyword){
		String msgType = requestMap.get("MsgType");
		String menuKey = requestMap.get("EventKey");
		MessageInfo messageInfo = new MessageInfo();
		try {
			Logger.getAnonymousLogger().info("-----------------  saveMessageInfo  menuKey = " + menuKey);
			if(url != null){
				Logger.getAnonymousLogger().info("-----------------  saveMessageInfo  url.menuKey = " + url);
			}
			messageInfo.setUrl(menuKey);
			messageInfo.setMsgType(msgType);
			messageInfo.setKeyword(keyword);
			messageInfo.setImageNo(imageTextNo);
			messageInfo.setRemsgType(remsgType);
			messageInfo.setMsgSendTime(new Date());
			messageInfo.setMsgContent(requestMap.get("Content"));
			messageInfo.setOpenId(requestMap.get("FromUserName"));
			messageInfo.setPublicNo(requestMap.get("ToUserName"));
			messageInfo.setMenuKey(url!=null ? url : menuKey);//url保存对应的menuKey
			if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)){
				messageInfo.setLocation(requestMap.get("Location_X") + "," + requestMap.get("Location_Y"));
			}
			Logger.getAnonymousLogger().info("------ messageInfoService.save(messageInfo)");
		} catch (Exception e) {
			Logger.getAnonymousLogger().info("e.getMessage() = "+e.getMessage());
			e.printStackTrace();
		}
		return messageInfo;
	}
	
	/**
	 * 疯狂猜图 图文消息
	 * @param requestMap
	 * @param userInfo
	 * @return
	 * @throws Exception 
	 */
	public static String getGuessImageMessage(Map<String, String> requestMap, UserInfo userInfo) throws Exception{
		String ids = userInfo.getIds(), type = requestMap.get("type");
		String title = null, picUrl = null, descrition = null;
		if("2".equals(type)){
			title = "疯狂猜图，第" + ids + "关";
			descrition = "查看答案：请回复 猜图+关数，如：猜图1";
			picUrl = String.format(Const.GUESS_IMG_URL, "ques" + ids);
		}else if("7".equals(type)){
			title = "疯狂成语，第" + ids + "关";
			descrition = "查看答案：请回复 成语+关数，如：成语1";
			picUrl = String.format(Const.GUESS_IMG_URL, "question" + ids);
		}
		String detailUrl = String.format(Const.GUESS_IMG_DETAIL_URL, type, requestMap.get("FromUserName"));
	
		Article article = new Article();
		article.setTitle(title);
		article.setPicUrl(picUrl);
		article.setUrl(detailUrl);
		article.setDescription(descrition);
		List<Article> articles = new ArrayList<Article>();
		articles.add(article);
		return getNewsMessage(requestMap, articles);
	}
	
	/**
	 * 音乐消息
	 * @param requestMap
	 * @return
	 */
	public static String getMusicMsg(Map<String, String> requestMap){
		 Music music = new Music();
		 music.setMusicUrl(requestMap.get("musicUrl"));
		 music.setHQMusicUrl(requestMap.get("musicUrl"));
		 music.setDescription("喜欢就转发给朋友听吧，按住转发！\n公众号：fzhwx开发者");
		 if(requestMap.get("author").length() > 1){
			 music.setTitle(requestMap.get("title") + "-" + requestMap.get("author"));
		 }else{
			 music.setTitle(requestMap.get("title"));
		 }
		 MusicMessage musicMessage = new MusicMessage();
		 musicMessage.setFuncFlag(0);
		 musicMessage.setMusic(music);
		 musicMessage.setCreateTime(new Date().getTime());
		 musicMessage.setFromUserName(requestMap.get("ToUserName"));
		 musicMessage.setToUserName(requestMap.get("FromUserName"));
		 musicMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_MUSIC);
		 return MessageUtil.musicMessageToXml(musicMessage);
	}
	
	/**
	 * 公交查询图文消息
	 * @return
	 */
	public static String getBusMessage(String origin,String destination,String region,String fromUserName,String toUserName){
		List<String> list = APIUtilService.getBUS(origin, destination, region);
		if(list.size() == 1) return null;
		
		NewsMessage newsMessage = new NewsMessage();
		newsMessage.setFuncFlag(1);
		newsMessage.setArticleCount(list.size());
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		
		List<Article> articles = new ArrayList<Article>();
		String title = null;
		for(int i = 0; i < list.size(); i ++){
			title = i == 0 ? list.get(i) : String.format("方案%s\n%s", i, list.get(i));
			Article article = new Article();
			article.setUrl("");
			article.setTitle(title);
			article.setDescription(title);
			article.setPicUrl("http://fzh2014.duapp.com/images/map.jpg");
			articles.add(article);
		}
		newsMessage.setArticles(articles);
		return MessageUtil.newsMessageToXml(newsMessage);
	}
	
	/**
	 * 城市旅游图文消息
	 * @param map
	 * @return
	 */
	public static String getTravelCity(Map<String, String> requestMap){
//		int count = 0;
		String picUrl = "";
		String city = requestMap.get("Content").replace("旅游", "");
		//先查询数据库
		List<TravelCity> list = com.fzh.wx.util.DBManager.getCityInfo(city);
		List<Article> articles = new ArrayList<Article>();
		if(list.size() == 0){
			//获取百度数据
			LinkedHashMap<String, String> map = BaiduMap.getTravelCity(city);
			if(map.size() < 1) return "暂无数据！";
//			count = map.size();
			for(String key : map.keySet()){
				String title = "", value = map.get(key);
				Article article = new Article();
				if(value.startsWith("http")){
					title = key;
					article.setUrl(value);
					if(value.contains("itineraries")){
						title += "\n >> 查看全文";
					}
				}else{
					title = key + " " + value;//城市印象和城市天气
				}
				
				if(key.contains("旅游攻略")){
					picUrl = "http://fzh2014.duapp.com/uploadimages/ly.png";
				}else if(key.contains("城市印象")){
					picUrl = "http://fzh2014.duapp.com/uploadimages/yx.png";
				}else if(key.contains("城市天气")){
					picUrl = "http://fzh2014.duapp.com/uploadimages/tq.png";
				}else{
					picUrl = "http://fzh2014.duapp.com/images/map.jpg";
				}
				article.setTitle(title);
				article.setPicUrl(picUrl);
				articles.add(article);
			}
		}else{
			int i = 0;
			Article article = null;
//			count = list.size() + 3;
			String cityabstr = "";
			for(TravelCity travelCity : list){
				if(articles.size() < 9){
					if(i == 0){
						cityabstr = travelCity.getCityabstract().trim();
						article = new Article();
						article.setPicUrl("http://itwx01.duapp.com/uploadimages/ly.png");
						article.setTitle(city + "：旅游攻略");
						article.setUrl(travelCity.getUrl());
						articles.add(article);
					}
					String title  = "【"+travelCity.getTypename()+"】" + travelCity.getDescription().trim() + "\n >> 查看全文";
					String url = "http://itwx01.duapp.com/fzh/itineraries.jsp?"+"cityid="+travelCity.getCityid()+"&typeid="+travelCity.getTypeid();
					article = new Article();
					article.setUrl(url);
					article.setTitle(title);
					article.setPicUrl("http://fzh2014.duapp.com/images/map.jpg");
					articles.add(article);
					i ++;
				}
			}
			article = new Article();
			article.setTitle("【城市印象】" + cityabstr);
			article.setPicUrl("http://fzh2014.duapp.com/uploadimages/yx.png");
			articles.add(article);
			article = new Article();
			String weather = APIUtilService.getBAEWeather(city).replace("【", "").replace("】", "").replace(city, "").replace("天气实况", "");
			article.setTitle("【城市天气】" + weather.trim());
			article.setPicUrl("http://fzh2014.duapp.com/uploadimages/tq.png");
			articles.add(article);
		}
		return getNewsMessage(requestMap, articles);
	}
	
	/**
	 * 组装图文消息
	 * @param requestMap
	 * @param articles
	 * @return
	 */
	public static String getNewsMessage(Map<String, String> requestMap, List<Article> articles) {
		NewsMessage newsMessage = new NewsMessage();
		newsMessage.setFuncFlag(1);
		newsMessage.setArticles(articles);
		newsMessage.setArticleCount(articles.size());
		newsMessage.setToUserName(requestMap.get("FromUserName"));
		newsMessage.setFromUserName(requestMap.get("ToUserName"));
		newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		newsMessage.setCreateTime(Long.parseLong(requestMap.get("CreateTime")));
		return MessageUtil.newsMessageToXml(newsMessage);
	}
	
	/**
	 * 回复单图文消息
	 * @param requestMap
	 * @param article
	 * @return
	 */
	public static String getSingleNews(Map<String, String> requestMap,Article article){
		List<Article> articleList = new ArrayList<Article>();
		articleList.add(article);
		NewsMessage newsMessage = new NewsMessage();
		newsMessage.setFuncFlag(1);
		newsMessage.setArticles(articleList);
		newsMessage.setArticleCount(articleList.size());
		newsMessage.setToUserName(requestMap.get("FromUserName"));
		newsMessage.setFromUserName(requestMap.get("ToUserName"));
		newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		newsMessage.setCreateTime(Long.parseLong(requestMap.get("CreateTime")));
		return MessageUtil.newsMessageToXml(newsMessage);
	}
	
	/**
	 * 电影图文消息
	 * @param requestMap
	 * @param mvData
	 * @param avData
	 * @return
	 * @throws Exception
	 */
	public static String getMovieNews(Map<String, String> requestMap, Object mvData, Object avData) throws Exception {
		List<Article> articles = new ArrayList<Article>();
		//搜库电影
		List<Movie> list = MovieService.getMovieBySOKU(requestMap.get("name"));
		if(list.size() == 0) return null;
		//组装图文消息
		for(Movie movie : list) {
			Article article = new Article();
			article.setUrl(movie.getUrl());
			article.setTitle(movie.getTitle());
			article.setPicUrl(movie.getImage());
			article.setDescription("");
			articles.add(article);
		}
		return getNewsMessage(requestMap, articles);
		
		/*Map<String, String> map = MovieService.getMovieByZHDY(requestMap.get("name"), mvData);//最火电影
		if(map.get("name") != null){
			//搜索电影
			Article article = new Article();
			article.setUrl(map.get("link"));
			article.setTitle(map.get("name"));
			article.setPicUrl(map.get("img"));
			article.setDescription("阿伯影院 微信号：abyy008 （观看电影请回复：电影+电影名，如：电影大鱼海棠）");
			articles.add(article);
			//影院随机页
			article = new Article();
			article.setTitle("阿伯精选，包你满意！");
			article.setUrl(MovieUtilService.getAVlist(avData).get(RandomUtils.nextInt(10)));
			article.setPicUrl(Const.IMAGE_URL.replace("imgname", RandomUtils.nextInt(6) + ""));
			article.setDescription("阿伯影院 微信号：abyy008 （观看电影请回复：电影+电影名，如：电影我不是潘金莲）");
			articles.add(article);
			//阿伯影院
			article = new Article();
			article.setUrl("http://mp.weixin.qq.com/s?__biz=MzAxODk3MDA0Mw==&mid=100000003&idx=1&sn=836ed91a91779b484955f06e69ac30c0#rd");
			article.setTitle("阿伯影院，精彩不断！");
			article.setPicUrl("http://mmbiz.qpic.cn/mmbiz/FrKmF09vc5BxwfCARJDJNGX6E4sk2rWUnvgfjmw2JmYOOXtG3Psqva1MyPFMDrv5KGIcTLM8w1QJDc7oXlicICg/0?wx_fmt=png");
			article.setDescription("阿伯影院 微信号：abyy008 （观看电影请回复：电影+电影名，如：电影我不是潘金莲）");
			articles.add(article);
			//1元打赏
			article = new Article();
			article.setUrl("http://mp.weixin.qq.com/s?__biz=MzAxODk3MDA0Mw==&mid=100000003&idx=3&sn=43286afe089abc833214da0800d020ea#rd");
			article.setTitle(StringUtil.getTitle());
			article.setPicUrl("http://mmbiz.qpic.cn/mmbiz/FAahqknuAWlen3HL6SS6J7D79gPN42aiaAFkicwcvqyicnicPLwrcZpAGUgMe9w6tZg8lVGpsV6zAIbY9t1MB8cVwg/0?wx_fmt=jpeg");
			article.setDescription("阿伯影院 微信号：abyy008 （观看电影请回复：电影+电影名，如：电影我不是潘金莲）");
			articles.add(article);
			//最新电影
			article = new Article();
			article.setTitle(map.get("names"));
			article.setUrl(MovieUtilService.getAVlist(avData).get(RandomUtils.nextInt(10)));
			articles.add(article);
			return getNewsMessage(requestMap, articles);
		}
		return getRandom8Movies(requestMap, mvData);*/
	}
	
	/**
	 * 8部随机电影
	 * @param requestMap
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static String getRandom8Movies(Map<String, String> requestMap, Object data) throws IOException{
		List<Article> articles = new ArrayList<Article>();
		Article article = null;
		List<Map<String, String>> list = (List<Map<String, String>>) data;
		Collections.shuffle(list);
		for(int i=0; i<7 && list.size()>7; i++){
			String link = list.get(i).get("link");
			if(link.contains(".html")){
				String movieId = link.substring(link.lastIndexOf("/") + 1 , link.lastIndexOf(".html"));
				Map<String, String> videoMap = MovieUtilService.getVideoById(list, movieId);
				if(videoMap.containsKey("video")) link = Const.VIDEO_HTML_URL + videoMap.get("video");
			}
			article = new Article();
			article.setUrl(link);
			article.setPicUrl(list.get(i).get("img"));
			article.setTitle(list.get(i).get("name"));
			article.setDescription("阿伯影院 微信号：abyy008 （观看电影请回复：电影名，如：奇幻森林）");
			articles.add(article);
		}
		Collections.shuffle(list);
		String names = "";
		for(int i=0; i<10; i++){
			names += list.get(i).get("name") + "；";
		}
		article = new Article();
		article.setDescription("阿伯影院 微信号：abyy008 （观看电影请回复：电影名，如：奇幻森林）");
		article.setTitle("10部最新电影名单：\n" + names + "\n（回复电影名可以搜索你想看的电影哦！）");
		article.setUrl(MovieUtilService.getAVlist(data).get(RandomUtils.nextInt(10)));
		articles.add(article);
		return MessageManager.getNewsMessage(requestMap, articles);
	}
}
