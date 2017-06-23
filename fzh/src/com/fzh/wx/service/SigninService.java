package com.fzh.wx.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fzh.util.VeDate;
import com.fzh.wx.pojo.respmsgpojo.Article;
import com.fzh.wx.util.DBManager;

public class SigninService {
	/**
	 * 不带图片的图文消息显示积分
	 * @return
	 */
	public static List<Article> getNewsScore(Map<String, String> requestMap){
		String desc = "签到赢积分，积分可换取更多精彩内容噢！\n";
		Date date = new Date();
		String openid = requestMap.get("FromUserName");
		String[] arr = DBManager.getSignInScore(openid);
		
		if(arr == null){
	 		desc += "\n★获得签到积分：10\n★当前积分：10";
			DBManager.saveSignIn(openid, "10", VeDate.DateToString(date));
		}
		
		if(arr !=null && arr.length > 0){
			if(VeDate.getDateYMD(date).equals(VeDate.stringDate(arr[1], "yyyy-MM-dd"))){
				desc += "\n你想干嘛？一天只能签一次到，还签...罚你跪搓衣板20分钟...\n★当前积分：" + arr[0];
			}else{
				LinkedHashMap<Date, Integer> map = DBManager.getSignInScoreByDate(openid);
				int data[] = getScore(map, date);
				int all_score = data[0] + Integer.parseInt(arr[0]);
				if(data[1] > 0 && data[1] %5 == 0){
					desc += "\n★你已连续签到" + data[1] + "天，此次获得签到积分：" + data[0] + "\n★当前积分：" + all_score;
				}else{
					desc += "\n★获得签到积分：" + data[0] + "\n★当前积分：" + all_score;
				}
				DBManager.saveSignIn(openid, String.valueOf(all_score), VeDate.DateToString(date));			
			}
	 	}
		
		List<Article> articles = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle("今日战报");
		article.setUrl("http://wsq.qq.com/reflow/232282146");
		article.setDescription(desc);
		articles.add(article);
		return articles;
	}
	
	/**
	 * 根据规则计算签到积分
	 * @param map
	 * @param newDate
	 * @return
	 */
	public static int[] getScore(Map<Date, Integer> map, Date newDate){
		int score = 10, date_count = 0;//连续天数
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(newDate);
		//计算连续签到天数
		for(Date date : map.keySet()){
			calendar.add(Calendar.DATE, -1);//当前时间减一天
			if(sdf.format(date).equals(sdf.format(calendar.getTime()))){
				date_count ++;
			}
		}
		if(date_count != 0){
			date_count ++;//把当前天加一
			if(date_count >= 5 && date_count %5 == 0){
				score += date_count/5 * 10;
			}
//			System.out.println("连续了"+date_count+"天，所获积分为：" + score);
		}
		int data[] = { score, date_count };
//		System.out.println("没有连续，只能拿到当天签到的10积分");
		return data;
	}
}
