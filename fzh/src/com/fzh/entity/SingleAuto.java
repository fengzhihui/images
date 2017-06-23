package com.fzh.entity;

import java.io.Serializable;
import java.util.Date;

public class SingleAuto implements Serializable {
	private static final long serialVersionUID = -5773659631368133833L;

	private int id;
	private String weixinPublicNo;
    private String reMessageType;
    private String reText;
    private Long imageTextNo;
    private Date createTime;
    private String createUser;
    private String vchar1;
    private String vchar2;
    private String vchar3;
    private String vchar4;
    private String wxName;
    private String title;
    private String operatorName;
    
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWeixinPublicNo() {
		return weixinPublicNo;
	}
	public void setWeixinPublicNo(String weixinPublicNo) {
		this.weixinPublicNo = weixinPublicNo;
	}
	public String getReMessageType() {
		return reMessageType;
	}
	public void setReMessageType(String reMessageType) {
		this.reMessageType = reMessageType;
	}
	public String getReText() {
		return reText;
	}
	public void setReText(String reText) {
		this.reText = reText;
	}
	public Long getImageTextNo() {
		return imageTextNo;
	}
	public void setImageTextNo(Long imageTextNo) {
		this.imageTextNo = imageTextNo;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getVchar1() {
		return vchar1;
	}
	public void setVchar1(String vchar1) {
		this.vchar1 = vchar1;
	}
	public String getVchar2() {
		return vchar2;
	}
	public void setVchar2(String vchar2) {
		this.vchar2 = vchar2;
	}
	public String getVchar3() {
		return vchar3;
	}
	public void setVchar3(String vchar3) {
		this.vchar3 = vchar3;
	}
	public String getVchar4() {
		return vchar4;
	}
	public void setVchar4(String vchar4) {
		this.vchar4 = vchar4;
	}
	public String getWxName() {
		return wxName;
	}
	public void setWxName(String wxName) {
		this.wxName = wxName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
}
