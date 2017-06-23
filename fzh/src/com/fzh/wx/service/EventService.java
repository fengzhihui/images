package com.fzh.wx.service;

import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.dao.MenuInfoDAO;
import com.fzh.dao.MessageInfoDAO;
import com.fzh.dao.UserDAO;
import com.fzh.dao.UserInfoDAO;
import com.fzh.entity.MenuInfo;
import com.fzh.entity.MessageInfo;
import com.fzh.entity.User;
import com.fzh.entity.UserInfo;
import com.fzh.wx.intf.GuessMyNum;
import com.fzh.wx.util.MessageManager;
import com.fzh.wx.util.MessageUtil;
import com.fzh.wx.util.StringUtil;

/**
 * 事件服务类
 * @author fzh
 */
@Service
public class EventService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private MenuInfoDAO menuInfoDAO;
	@Autowired
	private UserInfoDAO userInfoDAO;
	@Autowired
	private MessageInfoDAO messageInfoDAO;
	
	private String toUserName = null;
	private String fromUserName = null;
	private String inputContent = null;
	private String respContent =  null;
	private ResourceBundle resourceBundle;
	
	public void init(Map<String, String> requestMap) {
		inputContent = requestMap.get("Content");// 用户输入内容
		toUserName = requestMap.get("ToUserName");// 公众帐号
		fromUserName = requestMap.get("FromUserName");// 发送方帐号(open_id)
	}
	
	public String operateEventMessage(Map<String, String> requestMap) throws Exception {
		String eventType = requestMap.get("Event").trim();
		// 自定义菜单点击事件
		if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
			return replyMessageByClickMenu(requestMap);
		}
		//已关注用户扫描二维码
		if(eventType.equals(MessageUtil.EVENT_TYPE_SCAN)){
			return scanQrCodeEvent();
		}
		// 订阅
		if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
			subscribeEvent();
		}
		// 取消订阅
		if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
			// 取消订阅后用户再收不到公众号发送的消息,因此不需要回复消息
			unsubscribeEvent();
		}
		return null;
	}
	
	public String replyMessageByClickMenu(Map<String, String> requestMap) throws Exception {
		// 事件KEY值,与创建自定义菜单时指定的KEY值对应
		String eventKey = requestMap.get("EventKey").trim();
		MenuInfo menuInfo = menuInfoDAO.doFindByMenuId(eventKey, toUserName);
		if (menuInfo != null) {
			saveMessageInfo(requestMap, null, null, eventKey, "0");
			Logger.getAnonymousLogger().info("eventKey = " + eventKey);
			if(resourceBundle == null) {
				resourceBundle = ResourceBundle.getBundle("common");
			}
			//点击菜单名字对应用户输入
			String inputTxt = resourceBundle.getString(menuInfo.getMenuName());
			
			if(inputTxt == null) return null;//链接事件
			
			requestMap.put("Content", inputTxt);

			//根据菜单名字取得保存类型
			requestMap = StringUtil.menuContent(requestMap, null);
			String type = requestMap.get("type");
			respContent = requestMap.get("respContent");
			
			if("1".equals(type)){
				return MessageUtil.replyTextMessage(fromUserName, toUserName, respContent);
			}
			
			UserInfo userInfo = userInfoDAO.getUserByUserId(fromUserName);
			
			//保存用户操作记录
			saveUserInfoRecord(requestMap, userInfo);
			
			//猜图、成语 图文消息
			if("2".equals(type) || "7".equals(type)){
				return MessageManager.getGuessImageMessage(requestMap, userInfo);
			}
			//返回菜单提示内容
			return MessageUtil.replyTextMessage(fromUserName, toUserName, respContent);
		}
		return null;
	}
	
	/**
	 * 扫描二维码事件
	 * @return
	 */
	private String scanQrCodeEvent(){

//		String senceid = requestMap.get("EventKey");
//		String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+requestMap.get("Ticket");
//		respContent = senceid+"\n<a href=\""+url+"\">查看二维码</a>";
		return null;
	}
	
	private void saveMessageInfo(Map<String, String> requestMap, Long imageTextNo, String remsgType, String url, String keyword){
		MessageInfo messageInfo = MessageManager.createMessageInfo(requestMap, imageTextNo, remsgType, url, keyword);
		messageInfoDAO.save(messageInfo);
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
	
	/**
	 * 用户订阅事件
	 * @return
	 */
	private void subscribeEvent(){
		User user = userDAO.findByOpenId(fromUserName);
		if(user == null){
			user = new User();
			user.setOpenid(fromUserName);
			user.setWxpublicno(toUserName);
			user.setCreateTime(new Date());
			userDAO.save(user);
		}
	}
	
	/**
	 * 用户取消订阅事件
	 * @return
	 */
	private String unsubscribeEvent(){
		return null;
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
}
