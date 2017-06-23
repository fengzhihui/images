package com.fzh.wx.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.dao.ImageTextInfoDAO;
import com.fzh.dao.ImageTextMoreDAO;
import com.fzh.dao.MenuInfoDAO;
import com.fzh.dao.MenuKeySetDAO;
import com.fzh.dao.MessageInfoDAO;
import com.fzh.dao.UserDAO;
import com.fzh.entity.ImageTextInfo;
import com.fzh.entity.ImageTextMore;
import com.fzh.entity.MenuKeyset;
import com.fzh.entity.MessageInfo;
import com.fzh.entity.User;
import com.fzh.wx.util.MessageManager;
import com.fzh.wx.util.MessageUtil;

/**
 * 事件服务类
 * @author fzh
 */
@Service
public class CoreEventService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private MenuInfoDAO menuInfoDAO;
	@Autowired
	private MenuKeySetDAO menuKeySetDAO;
	@Autowired
	private MessageInfoDAO messageInfoDAO;
	@Autowired
	private ImageTextInfoDAO imageTextInfoDAO;
	@Autowired
	private ImageTextMoreDAO imageTextMoreDAO;
	
	private String toUserName = null;
	private String fromUserName = null;
	private String respContent =  null;
	
	public void init(Map<String, String> requestMap) {
		toUserName = requestMap.get("ToUserName");	  // 公众帐号
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
		String menuId = null;
        Logger.getAnonymousLogger().info("eventKey = " + eventKey);
        if("VIEW".equals(requestMap.get("Event"))){
        	menuId = menuInfoDAO.doFindByUrl(toUserName, eventKey);
		}
        //保存消息记录
        saveMessageInfo(requestMap, null, null, menuId, "0");
		MenuKeyset menu = menuKeySetDAO.findMenuKeySet(toUserName, eventKey);
		if(menu != null){
			if("1".equals(menu.getReType())){	   //文本
				respContent = menu.getRefText();
				return MessageUtil.replyTextMessage(fromUserName, toUserName, respContent);
			}else if("2".equals(menu.getReType())){//图文
				ImageTextInfo imageTextInfo = imageTextInfoDAO.findByPK(menu.getRefImageTextId());
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
	
}
