package com.fzh.entity;

import java.util.Date;

public class Signin {
	private int id;
	private int openid;
	private int score;
	private Date createTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOpenid() {
		return openid;
	}
	public void setOpenid(int openid) {
		this.openid = openid;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
