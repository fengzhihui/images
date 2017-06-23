package com.fzh.entity;

import java.util.Date;

public class MenuInfo {
	private int id;
	private int menuId;
	private int menuType;
    private String weixinPublicNo;
    private String menuName;
    private Long orderNo;
    private Long parentMenuId;
    private String url;
    private Date opTime;
    private String vchar1;
    private String vchar2;
    private String vchar3;
    private String vchar4;
    
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getWeixinPublicNo() {
		return weixinPublicNo;
	}
	public void setWeixinPublicNo(String weixinPublicNo) {
		this.weixinPublicNo = weixinPublicNo;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public Long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	public Long getParentMenuId() {
		return parentMenuId;
	}
	public void setParentMenuId(Long parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
	public int getMenuType() {
		return menuType;
	}
	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getOpTime() {
		return opTime;
	}
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
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
    
}
