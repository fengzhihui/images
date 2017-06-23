package com.fzh.wx.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.dao.ImageTextInfoDAO;
import com.fzh.dao.ImageTextMoreDAO;
import com.fzh.dao.KeysetDAO;
import com.fzh.dao.MessageInfoDAO;
import com.fzh.dao.UserDAO;
import com.fzh.dao.UserInfoDAO;
import com.fzh.entity.ImageTextInfo;
import com.fzh.entity.ImageTextMore;
import com.fzh.entity.Keyset;
import com.fzh.entity.MessageInfo;
import com.fzh.entity.User;
import com.fzh.entity.UserInfo;
import com.fzh.util.encrypt.TjrAES;
import com.fzh.wx.intf.BaiduMap;
import com.fzh.wx.intf.FaceService;
import com.fzh.wx.intf.GuessMyNum;
import com.fzh.wx.util.MessageManager;
import com.fzh.wx.util.MessageUtil;
import com.fzh.wx.util.StringUtil;

/**
 * 核心服务类
 * @author fzh
 */
@Service
public class FZHService {
	@Autowired
	UserDAO userDAO;
	@Autowired
	private KeysetDAO keysetDAO;
	@Autowired
	private UserInfoDAO userInfoDAO;
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
		inputContent = requestMap.get("Content"); // 用户输入内容
		toUserName = requestMap.get("ToUserName");// 公众帐号
		fromUserName = requestMap.get("FromUserName");// 发送方帐号(open_id)
	}
	
	public String operateTextMessage(Map<String, String> requestMap) throws Exception {
		//---------- 进入【01-16】功能菜单 ----------
		requestMap = StringUtil.menuContent(requestMap, null);
		String type = requestMap.get("type");
		respContent = requestMap.get("respContent");
		
		//表情、天气、登录、菜单提示消息
		if(respContent != null && "1".equals(type)){
			//保存用户消息记录
			saveMessageInfo(requestMap, null, null, null, "0");
			if("登录".equals(respContent)) {
				respContent = saveAndGetLoginUser();
			}
			return MessageUtil.replyTextMessage(fromUserName, toUserName, respContent);
		}
		
		//电影
		if("getMovieNews".equals(respContent) && "imgtxt".equals(type)){
			//保存用户消息记录
			saveMessageInfo(requestMap, null, null, null, "0");
			return MessageManager.getMovieNews(requestMap, null, null);
		}
		
		//用户输入为关键词或2-7功能回复
		if("record".equals(type)){
			//查询关键字回复（优先于其它上下行记录回复-20150323）
			respContent = replyMessageByKeyword(requestMap);
			if(respContent == null) {
				//保存用户消息记录
				saveMessageInfo(requestMap, null, null, null, "0");
			} else {
				return respContent;
			}
		}
		
		UserInfo userInfo = userInfoDAO.getUserByUserId(fromUserName);
		if(userInfo != null){
			type = "record".equals(type) ? userInfo.getType() : type;
			requestMap.put("type", type);
		}
		
		//---------- 保存用户操作记录并返回消息 ----------
		if(type.matches("[2-9]")){
			saveUserInfoRecord(requestMap, userInfo);
			//猜图、成语 图文消息
			if("2".equals(type) || "7".equals(type)){
				return MessageManager.getGuessImageMessage(requestMap, userInfo);
			}
			//菜单提示消息
			if(respContent != null){
				return MessageUtil.replyTextMessage(fromUserName, toUserName, respContent);
			}
			//根据用户最新的操作记录返回相应消息
			respContent = replyMessageByUserIdAndType(requestMap);
			if(respContent != null) return respContent;
		}
		return null;
	}
	
	/**
	 * 处理地理位置消息
	 * @param requestMap
	 * @return
	 */
	public String operateLocationMessage(Map<String, String> requestMap){
		String location = requestMap.get("Location_X") + "," + requestMap.get("Location_Y");
		if(!"".equals(location)){
//			CRUD.saveLocation(fromUserName, location);
			requestMap.put("place", "餐厅");
			requestMap.put("location", location);
//			respContent = BaiduMap.searchByLocationAndPlace(requestMap);
			if(respContent !=null && respContent.length()>5)return respContent;
		} else {
			respContent = "请选择位置\n" + StringUtil.msg("5");
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
	
	private String replyMessageByKeyword(Map<String, String> requestMap) throws Exception{
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
	
	private String replyMessageByUserIdAndType(Map<String, String> requestMap) throws Exception{
		String type = requestMap.get("type");
		UserInfo userInfo = userInfoDAO.findByUserIdAndType(fromUserName, type);
		if(userInfo == null) return null;
		
		//音乐 
		if("4".equals(type)){ return MessageUtil.replyMusicMessage(requestMap); }
		 //图文消息 之 地方搜索、附近搜索
		if("5".equals(type)){ return replyLocationMessage(requestMap, userInfo.getLocation()); }
		//图文消息 之 公交查询
		if("6".equals(type)){ return replyBusMessage(type); }
		
		if("3".equals(type)){ 		//翻译
			respContent = APIUtilService.getTranslation(inputContent);
		}
		else if("8".equals(type)){	//聊天
            respContent = APIUtilService.talk(inputContent);
		}
		else if("9".equals(type)){	//猜数
			 //获取答案、回答次数及猜测提示
			 if(userInfo.getIds() != null && !"14".equals(inputContent)){
				respContent = GuessMyNum.getAnswer(inputContent, userInfo);
				//更新操作
				if(!"wrong".equals(userInfo.getContent())){
					userInfoDAO.updateUser(userInfo);
				}
			 }
		} else {              //用户在"猜图, 搜地方, 查公交, 猜成语"状态发送任何内容时的回复
			//按内容查询
			respContent = APIUtilService.talk(inputContent);
		}
		if(respContent != null){
			return MessageUtil.replyTextMessage(fromUserName, toUserName, respContent);
		}
		return null;
	}
	
	private void saveMessageInfo(Map<String, String> requestMap, Long imageTextNo, String remsgType, String url, String keyword){
		MessageInfo messageInfo = MessageManager.createMessageInfo(requestMap, imageTextNo, remsgType, url, keyword);
		messageInfoDAO.save(messageInfo);
	}
	
	public String replyLocationMessage(Map<String, String> requestMap, String location){
		 if(respContent.contains(",")){//地方搜索
			 respContent = BaiduMap.getAddressMessage(requestMap);
			 if(respContent != null) return respContent;
		 }
		 if(respContent.contains("附近")){
			 String place = respContent.replaceAll("附近", "");
		     if(location != null){
		    	 requestMap.put("place", place);
		    	 requestMap.put("location", location);
		    	 respContent = BaiduMap.getMessageBySearchPlace(requestMap);
		    	 if(respContent != null) return respContent;
		     }
		 }
		return null;
	}
	
	public String replyBusMessage(String type){
		String qArr[] = respContent.split(",");
		if(qArr.length == 3){
	    	String origin = qArr[0], destination = qArr[1], region = qArr[2];//具体的起点,具体的终点,城市名
	    	if(!"".equals(origin) && !"".equals(destination) && !"".equals(region)){
	    		return MessageManager.getBusMessage(origin, destination, region, destination, region);
	    	}
		}
		return null;
	}
	
	/**
	 * 保存用户操作记录
	 * 新闻, 猜图, 翻译, 音乐, 地方, 公交, 成语, 聊天 , 猜数
	 * @param requestMap
	 * @param userInfo
	 */
	private void saveUserInfoRecord(Map<String, String> requestMap, UserInfo userInfo) {
		String type = requestMap.get("type");
		 try {
			 if(userInfo != null){
				 saveUserInfo(userInfo, type);
				 return;
			 }
			 // 不存在记录
			 userInfo = new UserInfo();
			 userInfo.setType(type);
			 userInfo.setTime(new Date());
			 userInfo.setUserid(fromUserName);
			 // 猜数答案、猜图关数
			 userInfo.setIds("9".equals(type) ? GuessMyNum.setAnswer() : "1");
			 userInfoDAO.insert(userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveUserInfo(UserInfo userInfo, String type) {
		UserInfo userInfo_ = new UserInfo();
		userInfo_.setType(type);
		userInfo_.setTime(new Date());
		userInfo_.setUserid(fromUserName);
		// 两次操作类型不同
		if (!type.equals(userInfo.getType())) {
			if ("2".equals(type) || "7".equals(type)) {
				UserInfo userInf = userInfoDAO.findByUserIdAndType(userInfo.getUserid(), type);
				if (userInf != null) {
					userInfo.setIds(userInf.getIds());
					return;
				}
			}
			userInfo_.setIds("9".equals(type) ? GuessMyNum.setAnswer() : "1");
			userInfoDAO.insert(userInfo_);
			return;
		}
		// 重新猜数
		if ("9".equals(type)) {
			if (userInfo.getIsend() == 1 && "14".equals(inputContent)) {
				userInfo_.setIds(GuessMyNum.setAnswer());
				userInfoDAO.insert(userInfo_);
			}
			return;
		}
		// 重新猜图、成语
		if ("2".equals(type) || "7".equals(type)) {
			if (userInfo.getIsend() == 0) return;
			if ("06".equals(inputContent) || "07".equals(inputContent)) {
				userInfo_.setIds("1");
				userInfoDAO.insert(userInfo_);
			}
		}
	}
	
	public String saveAndGetLoginUser() throws Exception {
		User user = userDAO.findByOpenId(fromUserName);
		if(user == null) {
			user = new User();
			user.setOpenid(fromUserName);
			user.setCreateTime(new Date());
			user.setPassword(TjrAES.encrypt("123456"));
			user.setUsername(StringUtil.getShortStr(fromUserName));
			userDAO.save(user);
		}
		return String.format("你的登录账号：%s，密码：%s", user.getUsername(), "123456");
	}
	
}
