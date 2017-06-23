package com.fzh.wx.intf;

public class XHJ {

	public static String talk(String msg) {
		String  content = "";
//		RenRen rr = new RenRen("495300897@qq.com", "fzh789456");
//        rr.login();
//        content = rr.talk(msg);
		try {
			content = X_HJ.getContent(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	
//	public static void main(String[] args) {
//		System.out.println(talk("辛苦又没钱"));
//	}
}
