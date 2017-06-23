package com.fzh.wx.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fzh.common.LoginValidate;
import com.fzh.wx.service.ABYYService;
import com.fzh.wx.util.MessageUtil;
import com.fzh.wx.util.SignUtil;

/**
 * 核心请求处理类
 * @author fzh
 */
@Controller
public class ABYYController {
	@Autowired
	private ABYYService abyyService;
	
	/**
	 * 确认请求来自微信服务器
	 * @return 
	 */
	@LoginValidate(value=false)
	@RequestMapping(value="/movie/abyy", method=RequestMethod.GET, produces="text/html;charset=UTF-8")
	public @ResponseBody String doGet(HttpServletRequest request) throws Exception {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		String token = "abyy";
		
		// 通过检验signature对请求进行校验,若校验成功则原样返回echostr,表示接入成功,否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce, token)) {
			return echostr;
		}
		return null;
	}

	/**
	 * 处理微信服务器发来的消息
	 * @return 
	 */
	@LoginValidate(value=false)
	@RequestMapping(value="/movie/abyy", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody String doPost(HttpServletRequest request) throws Exception {
        String respMessage = null;
        try {
			//解析微信发来的请求(xml)
			Map<String, String> requestMap = MessageUtil.parseXml(request);
				
			String msgType = requestMap.get("MsgType");// 消息类型
			
			// 事件推送
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
			}
			
			abyyService.init(requestMap);
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				return abyyService.operateTextMessage(requestMap);
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				return abyyService.operateImageMessage(requestMap.get("PicUrl"));
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				return abyyService.operateVoiceMessage(requestMap.get("Recognition"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return respMessage;
	}
	

}
