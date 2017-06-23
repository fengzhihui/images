package com.fzh.util.http;

import org.apache.http.message.BasicNameValuePair;

import com.fzh.util.http.impl.HttpApiImpl;

public class TjrClientHttp {
	private static TjrClientHttp instance;
	private static Object lock = new Object();
	private HttpApi mHttpApi;

	private TjrClientHttp() {
		mHttpApi = new HttpApiImpl(HttpApiImpl.createHttpClient());
	}

	/**
	 * 单例化一个TjrClientHttp
	 */
	public static TjrClientHttp getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new TjrClientHttp();
				}
			}
		}
		return instance;
	}

	/**
	 * 用户报名圈子活动
	 * 
	 * @param userId
	 * @param circleNum
	 * @param ses
	 * @param sessionid
	 * @return
	 * @throws Exception
	 */
	public String circleSign(String userId, String sessionid, String circleNum, String ses,String actNo) throws Exception {
		return mHttpApi.doHttpPost("http://circle.taojinroad.com/tjrcircle/gameSesSign.do", //
				new BasicNameValuePair("method", "sign"), //
				new BasicNameValuePair("userId", userId), //
				new BasicNameValuePair("sessionid", sessionid),// 
				new BasicNameValuePair("circleNum", circleNum), //
				new BasicNameValuePair("actNo", actNo), //
				new BasicNameValuePair("ses", ses));
	}

	/**
	 * 检查用户是否已经报名圈子活动
	 * 
	 * @param userId
	 * @param circleNum
	 * @param ses
	 * @param sessionid
	 * @return
	 * @throws Exception
	 */
	public String isSign(String userId, String sessionid, String circleNum, String ses,String actNo) throws Exception {
		return mHttpApi.doHttpPost("http://circle.taojinroad.com/tjrcircle/gameSesSign.do", //
				new BasicNameValuePair("method", "isSign"), //
				new BasicNameValuePair("userId", userId), //
				new BasicNameValuePair("sessionid", sessionid),// 
				new BasicNameValuePair("circleNum", circleNum), //
				new BasicNameValuePair("actNo", actNo), //
				new BasicNameValuePair("ses", ses));
	}

	/**
	 * 用户查看总比赛排名(对应活动区h5)
	 * 
	 * @param ses
	 *            当前届数
	 * @param rank
	 *            当前最后一项排名
	 * @param updown
	 *            0代表是加载第一页面数据,1代表是加载第二页面以后的数据
	 * @return
	 * @throws Exception
	 */
	public String findGameRankBySes(String signSes, String rank, String updown) throws Exception {
		return mHttpApi.doHttpPost("http://circle.taojinroad.com/tjrcircle/gameActArea.do", //
				new BasicNameValuePair("method", "findGameRankBySes"), //
				new BasicNameValuePair("signSes", signSes), //
				new BasicNameValuePair("rank", rank),// 
				new BasicNameValuePair("updown", updown));
	}
	
	public String getGameByGameId(String username) throws Exception {
		return mHttpApi.doHttpPost("http://localhost:8080/fzh/http/findUser",
				new BasicNameValuePair("username", username));
	}
	
	public static void main(String[] args) throws Exception {
		String username = "fzh";
		String str = new TjrClientHttp().getGameByGameId(username);
		System.out.println(str);
	}
}
