package com.fzh.wx.pojo.menupojo;

/**
 * 普�?按钮(子按�?
 * 子菜单是这样定义的：没有子菜单的菜单�?有可能是二级菜单�?也有可能是不含二级菜单的�?��菜单。这类子菜单项一定会包含三个属�?：type、name和key
 * @author fzh
 */
public class CommonButton extends Button {
	private String type;
	private String key;
	private String url;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
}