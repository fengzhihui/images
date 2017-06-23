package com.fzh.wx.pojo.reqmsgpojo;

/**
 * 文本消息
 * 
 * @author fzh
 */
public class TextMessage extends BaseMessage {
	// 消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
