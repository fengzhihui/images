package com.fzh.wx.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.misc.BASE64Encoder;

/**
 * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
 * 
 * @author fzh
 *
 */
public class Image2Base64Util {

	public static void main(String[] args) {
		System.out.println(GetImageStr("E:/1046902651.jpg"));
	}

	public static String GetImageStr(String imgFilePath) {
		byte[] data = null;

		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}
}
