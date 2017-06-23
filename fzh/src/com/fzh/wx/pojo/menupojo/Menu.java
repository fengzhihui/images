package com.fzh.wx.pojo.menupojo;

/**
 * 菜单
 * 包含多个菜单�?�?��只能�?�?,这些菜单项可以是子菜单项(不含二级菜单的一级菜�?,也可以是父菜单项(包含二级菜单的菜单项)
 * @author fzh
 */
public class Menu {
	private Button[] button;

	public Button[] getButton() {
		return button;
	}

	public void setButton(Button[] button) {
		this.button = button;
	}
}