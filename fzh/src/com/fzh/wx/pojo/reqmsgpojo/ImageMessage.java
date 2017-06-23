package com.fzh.wx.pojo.reqmsgpojo;

/**
 * 图片消息
 * @author fzh
 */
public class ImageMessage extends BaseMessage {
	// 图片链接
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
}

