package com.fzh.entity;

import java.util.Date;

public class GuessImage {
	private String userid;
	private String username;
	private String phone;
	private int score;
	private Date createtime;
	private int sortscore;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public int getSortscore() {
		return sortscore;
	}
	public void setSortscore(int sortscore) {
		this.sortscore = sortscore;
	}
	
}
