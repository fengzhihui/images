package com.fzh.entity;

import java.util.Date;
import java.util.List;

public class ImageTextInfo {
	private Long imageTextNo;
    private String weixinPublicNo;
    private String title;
    private String imageUrl;
    private String digest;
    private String mainText;
    private String clickUrl;
    private String clickOutUrl;
    private String sourceUrl;
    private String imageTextType;
    private Date createTime;
    private String operator;
    private List<ImageTextMore> subVoList;
    
	public Long getImageTextNo() {
		return imageTextNo;
	}
	public void setImageTextNo(Long imageTextNo) {
		this.imageTextNo = imageTextNo;
	}
	public String getWeixinPublicNo() {
		return weixinPublicNo;
	}
	public void setWeixinPublicNo(String weixinPublicNo) {
		this.weixinPublicNo = weixinPublicNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public String getMainText() {
		return mainText;
	}
	public void setMainText(String mainText) {
		this.mainText = mainText;
	}
	public String getClickUrl() {
		return clickUrl;
	}
	public void setClickUrl(String clickUrl) {
		this.clickUrl = clickUrl;
	}
	public String getClickOutUrl() {
		return clickOutUrl;
	}
	public void setClickOutUrl(String clickOutUrl) {
		this.clickOutUrl = clickOutUrl;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getImageTextType() {
		return imageTextType;
	}
	public void setImageTextType(String imageTextType) {
		this.imageTextType = imageTextType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public List<ImageTextMore> getSubVoList() {
		return subVoList;
	}
	public void setSubVoList(List<ImageTextMore> subVoList) {
		this.subVoList = subVoList;
	}
    
}
