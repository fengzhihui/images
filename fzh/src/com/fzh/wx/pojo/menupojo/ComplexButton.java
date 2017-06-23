package com.fzh.wx.pojo.menupojo;

/**
 * 复杂按钮(父按�?
 * 对父菜单项的定义：包含有二级菜单项的�?��菜单。这类菜单项包含有二个属性：name和sub_button，�?sub_button以是�?��子菜单项数组
 * @author fzh
 */
public class ComplexButton extends Button {
	private Button[] sub_button;

	public Button[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(Button[] sub_button) {
		this.sub_button = sub_button;
	}

}