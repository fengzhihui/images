package com.fzh.wx.util;

import java.io.InputStream;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.fzh.wx.pojo.respmsgpojo.Article;
import com.fzh.wx.pojo.respmsgpojo.MusicMessage;
import com.fzh.wx.pojo.respmsgpojo.NewsMessage;
import com.fzh.wx.pojo.respmsgpojo.TextMessage;
import com.fzh.wx.service.APIUtilService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 消息工具类
 * @author fzh
 */
public class MessageUtil {
	/** 返回消息类型：文本  */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/** 返回消息类型：音乐 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/** 返回消息类型：图文 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/** 请求消息类型：文本 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/** 请求消息类型：图片 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/** 请求消息类型：链接 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/** 请求消息类型：地理位置 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/** 请求消息类型：音频 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/** 请求消息类型：推送 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/** 事件类型：subscribe(订阅) */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/** 事件类型：unsubscribe(取消订阅) */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/** 事件类型：SCAN(扫描二维码事件) */
	public static final String EVENT_TYPE_SCAN = "SCAN";
	
	/** 事件类型：CLICK(自定义菜单点击事件) */
	public static final String EVENT_TYPE_CLICK = "CLICK";

	/**
	 * 回复文本消息
	 * @param fromUserName
	 * @param toUserName
	 * @param content
	 * @return
	 */
	public static String replyTextMessage(String fromUserName, String toUserName, String content){
		TextMessage textMessage = new TextMessage();
		textMessage.setFuncFlag(0);
		textMessage.setContent(content);
		textMessage.setToUserName(fromUserName);
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		return textMessageToXml(textMessage);
	}
	
	/**
	 * 随机回复消息
	 * @param requestMap
	 * @return
	 * @throws Exception
	 */
	public static String replyMessageByRand(Map<String, String> requestMap) throws Exception {
		Random rand = new Random();
		int num = rand.nextInt(100);

		if(num <= 33){
			return replyMusicMessage(requestMap);
		}
		if(num > 33 && num < 66){
			String inputContent = APIUtilService.talk(requestMap.get("Content"));
			return MessageUtil.replyTextMessage(requestMap.get("FromUserName"), requestMap.get("ToUserName"), inputContent);
		}
		requestMap.put("name", requestMap.get("Content"));
		return MessageManager.getMovieNews(requestMap, null, null);
	}
	
	public static String replyMusicMessage(Map<String, String> requestMap) throws Exception {
		Map<String, String> musicMap = APIUtilService.getMusicByRand(requestMap.get("Content"));
		if(musicMap != null){
			musicMap.put("ToUserName", requestMap.get("ToUserName"));
			musicMap.put("FromUserName", requestMap.get("FromUserName"));
			return MessageManager.getMusicMsg(musicMap);
		}
		return null; 
	}
	
	/**
	 * 解析微信发来的请求(xml)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList){
			map.put(e.getName(), e.getText());
		}

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> parse2Xml(String html) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();

		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(html);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList){
			map.put(e.getName(), e.getText());
		}

		return map;
	}
	
	/**
	 * 文本消息对象转换成xml
	 * @param textMessage 文本消息对象
	 * @return xml
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}
	
	/**
	 * 音乐消息对象转换成xml
	 * @param musicMessage 音乐消息对象
	 * @return xml
	 */
	public static String musicMessageToXml(MusicMessage musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}
	
	/**
	 * 图文消息对象转换成xml
	 * @param newsMessage 图文消息对象
	 * @return xml
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
	 * 扩展xstream,使其支持CDATA块
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
	
}
