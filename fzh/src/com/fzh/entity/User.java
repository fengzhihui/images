package com.fzh.entity;

import java.util.Date;

public class User {

	private int id;
	private String username;
	private String password;
	private int flag;   //是否为fzhwx开发者粉丝：1为是，0为否
	private int level;  //用户级别：0为初级粉丝，1为公众号初级用户
	private String wxpublicno;   //用户公众账号
	private String wxpublicname; //用户公众账号名称
	private String openid;       //OPENID
	private Date createTime;     //createTime
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getWxpublicno() {
		return wxpublicno;
	}
	public void setWxpublicno(String wxpublicno) {
		this.wxpublicno = wxpublicno;
	}
	public String getWxpublicname() {
		return wxpublicname;
	}
	public void setWxpublicname(String wxpublicname) {
		this.wxpublicname = wxpublicname;
	}
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "user: " + username + " , pwd: " + password;
	}
}
