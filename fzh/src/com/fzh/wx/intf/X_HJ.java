package com.fzh.wx.intf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class X_HJ {

	/**
	 * 获取内容
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public static String getContent(String content) throws IOException {
		URL url = new URL("http://sns.video.qq.com/tvideo/fcgi-bin/video?otype=json&callback=jsonp1320749705618&idx=0&vid=b0015g7ciw7&g_tk=470893615&loginUin=&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=jsonp1320749705618&needNewCode=0");
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
//		connection.setRequestProperty("Host", "s.plcloud.music.qq.com");
//		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:32.0) Gecko/20100101 Firefox/32.0");
//		connection.setRequestProperty("Accept", "*/*");
//		connection.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
//		connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
//		connection.setRequestProperty("Referer", "http://y.qq.com/w/sign.html?uin=519918611");
//		connection.setRequestProperty("Cookie", "uin_cookie=495300897; euin_cookie=C1CD076EF542B174B52ACE1FB23C5D2C6D9AA2ADCA8207E1; ac=1,030,017; pgv_pvid=613843880; o_cookie=495300897; pt2gguin=o0495300897; ptcz=b4f5c7296fc367e394a0c0191c106db7541e792425de7e7765bb5e4dcc1e7cc2; ptui_loginuin=1561494574; luin=o0495300897; lskey=00010000a2634d0057239ac566bd69837785e76ce26b3e24fac6aee72030b3c7bda07667f0299ca34b7df2a4; pgv_pvi=7454834688; RK=CdGLhfRRMn; qq_photo_key=71d35991d98569bc45342924b91aaf91; uin=o0495300897; skey=@nfp85Vy2x; ptisp=ctc; pgv_info=ssid=s9155650286");
//		connection.setRequestProperty("Connection", "keep-alive");
//		connection.setRequestProperty("Cache-Control", "max-age=0");
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "gb2312");
//		out.write("para="+URLEncoder.encode(content, "gbk")); // 向页面传递数据。post的关键所在
		// remember to clean up
		out.flush();
		out.close();
		// 一旦发送成功，用以下方法就可以得到服务器的回应：
		String sCurrentLine = "";
		String sTotalString = "";
		InputStream l_urlStream = connection.getInputStream();
		
		// 传说中的三层包装阿！
		BufferedReader l_reader = new BufferedReader(new InputStreamReader(l_urlStream));
		while ((sCurrentLine = l_reader.readLine()) != null) {
			sTotalString += sCurrentLine;
		}
		return sTotalString;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(getContent("问你个问题"));
	}

}
