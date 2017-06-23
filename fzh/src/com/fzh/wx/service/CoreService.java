package com.fzh.wx.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.dao.ImageTextInfoDAO;
import com.fzh.dao.ImageTextMoreDAO;
import com.fzh.dao.KeysetDAO;
import com.fzh.dao.MessageInfoDAO;
import com.fzh.entity.ImageTextInfo;
import com.fzh.entity.ImageTextMore;
import com.fzh.entity.Keyset;
import com.fzh.entity.MessageInfo;
import com.fzh.wx.intf.FaceService;
import com.fzh.wx.util.MessageManager;
import com.fzh.wx.util.MessageUtil;
import com.fzh.wx.util.QQFace;
import com.fzh.wx.util.StringUtil;

/**
 * 核心服务类
 * @author fzh
 */
@Service
public class CoreService {
	@Autowired
	private KeysetDAO keysetDAO;
	@Autowired
	private MessageInfoDAO messageInfoDAO;
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
		//保存用户消息记录
		saveMessageInfo(requestMap, null, null, null, "0");
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
		else if ((inputContent.startsWith("电影")) && inputContent.length() > 2) {
			requestMap.put("name", inputContent.replace("电影", "").replace("+", ""));
			return MessageManager.getMovieNews(requestMap, null, null);
		} else {
			//查询关键字回复（优先于其它上下行记录回复-20130623）
			respContent = replyMessageByKeyword(requestMap);
			return respContent != null ? respContent : replyMessageByRand(requestMap);
		}
		return MessageUtil.replyTextMessage(fromUserName, toUserName, respContent);
	}
	
	private void saveMessageInfo(Map<String, String> requestMap, Long imageTextNo, String remsgType, String url, String keyword){
		MessageInfo messageInfo = MessageManager.createMessageInfo(requestMap, imageTextNo, remsgType, url, keyword);
		messageInfoDAO.save(messageInfo);
	}
	
	private String replyMessageByKeyword(Map<String, String> requestMap) throws Exception {
		Keyset keySet = keysetDAO.findKeySet(toUserName, inputContent);
		if(keySet != null){
			//保存消息记录
			saveMessageInfo(requestMap, null, null, null, "1");
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
		int num = StringUtil.getRandNum(100);

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
	
}

