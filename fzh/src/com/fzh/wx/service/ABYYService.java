package com.fzh.wx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.dao.ImageTextInfoDAO;
import com.fzh.dao.ImageTextMoreDAO;
import com.fzh.dao.KeysetDAO;
import com.fzh.entity.ImageTextInfo;
import com.fzh.entity.ImageTextMore;
import com.fzh.entity.Keyset;
import com.fzh.wx.intf.FaceService;
import com.fzh.wx.pojo.respmsgpojo.Article;
import com.fzh.wx.util.MessageManager;
import com.fzh.wx.util.MessageUtil;
import com.fzh.wx.util.QQFace;

/**
 * 核心服务类
 * @author fzh
 */
@Service
public class ABYYService {
	@Autowired
	private KeysetDAO keysetDAO;
	@Autowired
	private ImageTextInfoDAO imageTextInfoDAO;
	@Autowired
	private ImageTextMoreDAO imageTextMoreDAO;
	private String toUserName = null;
	private String fromUserName = null;
	private String respContent =  null;
	private String inputContent = null;
	
	public void init(Map<String, String> requestMap) {
		inputContent = requestMap.get("Content");// 用户输入内容
		toUserName = requestMap.get("ToUserName");// 公众帐号
		fromUserName = requestMap.get("FromUserName");// 发送方帐号(open_id)
	}
	
	public String operateTextMessage(Map<String, String> requestMap) throws Exception {
		// 回复表情
		if (QQFace.isQqFace(inputContent)) {
			respContent = inputContent;
		}
		// 获取百度天气
		else if ((inputContent.startsWith("天气") || inputContent.endsWith("天气"))
				&& inputContent.length() >= 4) {
			respContent = APIUtilService.getBAEWeather(inputContent);
		}
		// 回复电影图文
		else {
			//查询关键字回复（优先于其它上下行记录回复-20130623）
			respContent = replyMessageByKeyword(requestMap);
			if(respContent != null) {
				return respContent;
			}
			requestMap.put("name", inputContent.replace("电影", "").replace("+", ""));
			respContent = MessageManager.getMovieNews(requestMap, null, null);
			return respContent != null ? respContent : replyMessageByRand(requestMap);
		}
		return MessageUtil.replyTextMessage(fromUserName, toUserName, respContent);
	}
	
	private String replyMessageByKeyword(Map<String, String> requestMap) throws Exception {
		Keyset keySet = keysetDAO.findKeySet(toUserName, inputContent);
		if(keySet != null){
			//文本
			if("1".equals(keySet.getReType())){
				return MessageUtil.replyTextMessage(fromUserName, toUserName, keySet.getRefText());
			}
			//图文
			if("2".equals(keySet.getReType())){
				ImageTextInfo imageTextInfo = imageTextInfoDAO.findByPK(keySet.getRefImageTextId().toString());
				if(imageTextInfo == null) return null;
				List<ImageTextMore> imagetextMoreList = null;
				if("2".equals(imageTextInfo.getImageTextType())){
					imagetextMoreList = imageTextMoreDAO.findImageTextMoreList(imageTextInfo.getImageTextNo());
				}
				//回复关键字电影图文
				if(imageTextInfo.getImageUrl().contains("http")){ // 添加图片链接
					String movieNames = keysetDAO.findKeywords(toUserName);
					return MessageManager.getMovieNewsMessage(requestMap, imageTextInfo, movieNames);
				}
				return MessageManager.getImageTextInfo(requestMap, imageTextInfo, imagetextMoreList);
			}
		}
		return null;
	}
	
	public String replyMusicMessage(String input) throws Exception {
		Map<String, String> musicMap = APIUtilService.getMusicByRand(input);
		if(musicMap != null){
			musicMap.put("ToUserName", toUserName);
			musicMap.put("FromUserName", fromUserName);
			return MessageManager.getMusicMsg(musicMap);
		}
		return null; 
	}
	
	/**
	 * 处理图片消息
	 * @param picUrl
	 * @return
	 */
	public String operateImageMessage(String picUrl){
        // 人脸检测
        return MessageUtil.replyTextMessage(fromUserName, toUserName, FaceService.detect(picUrl));
	}
	
	/**
	 * 处理音频消息
	 * @param recognition
	 * @return
	 * @throws Exception 
	 */
	public String operateVoiceMessage(String recognition) throws Exception {
		respContent = APIUtilService.talk(recognition) + "\n[撇嘴]我暂时还识别不了您说的内容！";
		return MessageUtil.replyTextMessage(fromUserName, toUserName, respContent); 
	}
	
	/**
	 * 随机回复消息
	 * @param requestMap
	 * @return
	 * @throws Exception
	 */
	public String replyMessageByRand(Map<String, String> requestMap) throws Exception {
		Random rand = new Random();
		int num = rand.nextInt(100);

		if(num <= 33){
			return replyMusicMessage(inputContent);
		}
		if(num > 33 && num < 66){
			inputContent = APIUtilService.talk(inputContent);
			return MessageUtil.replyTextMessage(fromUserName, toUserName, respContent);
		}
		requestMap.put("name", inputContent);
		return MessageManager.getMovieNews(requestMap, null, null);
	}
	
	private String getPayMovie(Map<String, String> requestMap) {
		List<Article> articles = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle("x-x-o-o (O_O)");
		article.setDescription("x-x-o-o (O_O)");
		article.setPicUrl("");
		article.setUrl("http://ikanav.duapp.com/images/upload/20170521_092750.mp4");
		articles.add(article);
		return MessageManager.getNewsMessage(requestMap, articles);
	}
}

