package com.fzh.wx.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fzh.jdbctemplate.JDBC;
import com.fzh.wx.pojo.TravelCity;

public class DBManager extends JDBC {
	/**
	 * 保存用户地址
	 * @param fromUserName
	 * @param location
	 */
	public static void saveLocation(String fromUserName,String location){
		String sql = "insert into userinfo(userid,time,type,location,ids) values (?,?,?,?,?)";
		getJdbc().update(sql, new Object[]{
				fromUserName,
				new Date().getTime() + "",
				"5",
				location,
				"5"
		});
	}
	
	/**
	 * 获取短信
	 * @param content
	 * @return
	 */
	public static String getMsg(String content){
		String sql = null;
		Random rand = new Random();
		int num = rand.nextInt(100);
		if(content.equals("短信") || content.equals("02")){
//			sql = "select content from wishes order by rand() limit 1";
			if(num <= 25){
				sql = "select content from notebook where type='幸福瞬间' order by rand() limit 1";
			}else if(num > 25 && num <= 50){
				sql = "select content from notebook where type='节日祝福' order by rand() limit 1";
			}else if(num > 50 && num <= 75){
				sql = "select content from notebook where type='大幽小默' order by rand() limit 1";
			}else{
				sql = "select content from notebook where type='百姓视角' order by rand() limit 1";
			}
		}
		/*else if(content.startsWith("短信心语")){
			sql = "select content from notebook where type='幸福瞬间' order by rand() limit 1";
		}else if(content.startsWith("短信祝福")){
			sql = "select content from notebook where type='节日祝福' order by rand() limit 1";
		}else if(content.startsWith("短信笑话")){
			sql = "select content from notebook where type='大幽小默' order by rand() limit 1";
		}else if(content.startsWith("短信生活")){
			sql = "select content from notebook where type='百姓视角' order by rand() limit 1";
		}else if(content.startsWith("短信词文")){
			sql = "select content from notebook where type='古词文苑' order by rand() limit 1";
		}else if(content.startsWith("短信校园")){
			sql = "select content from notebook where type='校园论语' order by rand() limit 1";
		}*/
		Map<String, Object> map = queryForMap(sql);
		return map == null ? null : map.get("content").toString().replace("\n", "<br>");
	}
	
	/**
	 * 保存城市信息
	 * @param map
	 * @return
	 */
	public static void saveCityInfo(Map<String, String> map){
		String sql = "insert into travelcity(cityid,cityname,citypinyin,location,abstract,url,description) values(?,?,?,?,?,?,?)";
		getJdbc().update(sql, new Object[]{
				map.get("cityid"),
				map.get("typename"),
				map.get("citypinyin"),
				map.get("location"),
				map.get("abstract"),
				map.get("url"),
				map.get("description")
		});
	}
	
	/**
	 * 保存景点行程信息
	 * @param map
	 * @return
	 */
	public static void saveItinerariesInfo(Map<String, String> map){
		String sql = "insert into traveltype(cityid,typeid,typename,description,itineraries) values(?,?,?,?,?)";
		getJdbc().update(sql, new Object[]{
				map.get("cityid"),
				map.get("typeid"),
				map.get("typename"),
				map.get("description"),
				map.get("itineraries")
		});
	}
	
	/**
	 * 根据cityname查询城市
	 * @param cityname
	 * @return
	 */
	public static boolean getCity(String cityname){
		String sql = "select c.cityid from travelcity c where c.cityname = ?";
		Map<String, Object> map = queryForMap(sql, new Object[]{ cityname });
		return map == null ? false : true;
	}
	
	/**
	 * 获取城市景点
	 * @param city
	 * @return
	 */
	public static List<TravelCity> getCityInfo(String city){
		List<TravelCity> list = new ArrayList<TravelCity>();
		
		String sql = "select c.cityid,c.abstract,c.url,t.typeid,t.typename,t.description from travelcity c,traveltype t where c.cityname = ? and c.cityid=t.cityid";
		List<Map<String, Object>> dataList = getJdbc().queryForList(sql, new Object[]{ city });
		
		for(Map<String, Object> obj : dataList){
			TravelCity travelCity = new TravelCity();
			travelCity.setUrl(obj.get("url").toString());
			travelCity.setCityid(obj.get("cityid").toString());
			travelCity.setTypeid(obj.get("typeid").toString());
			travelCity.setTypename(obj.get("typename").toString());
			travelCity.setDescription(obj.get("description").toString());
			travelCity.setCityabstract(obj.get("abstract").toString());
			list.add(travelCity);
		}
		return list;
	}
	
	/**
	 * 查询景点详情
	 * @param cityid
	 * @param typeid
	 * @return
	 */
	public static String getItineraries(String cityid,String typeid){
		String sql = "select itineraries from traveltype where cityid = ? and typeid = ?";
		Map<String, Object> map = queryForMap(sql, new Object[]{ cityid, typeid });
		return map == null ? null : map.get("itineraries").toString().replace("\n", "<br>");
	}
	
	/**
	 * 保存用户游戏记录
	 * @param openid
	 * @param score
	 */
	public static void saveLifeGame(String openid, String score){
		String sql = "insert into user_record(openid,game_type,play_time,score) values(?,?,?,?)";
		getJdbc().update(sql, new Object[]{ openid, "lifegame", new Date().getTime(), score });
	}
	
	/**
	 * 查询用户分数记录
	 * @param openid
	 * @return
	 */
	public static String getScore(String openid) {
		String sql = "select score from user_record where openid = ?";
		Map<String, Object> map = getJdbc().queryForMap(sql, new Object[]{ openid });
		return map == null ? null : map.get("score").toString();
	}
	
	/**
	 * 修改用户分数
	 * @param openid
	 * @param score
	 */
	public static void updateLifeGame(String openid, String score){
		String sql = "update user_record set openid = ?, play_time = ?, score = ? where openid = ?";
		getJdbc().update(sql, new Object[]{ openid, new Date().getTime(), score, openid });
	}
	
	/**
	 * 查询第一名和当前用户的排名
	 * @param openid
	 * @param score
	 * @return
	 */
	public static String[] getLifeGame(String openid, String score){
		String sql = "SELECT (select max(score) from user_record) as max, " +
							"(SELECT COUNT(1) FROM user_record WHERE score>= ?) as rank FROM user_record WHERE openid = ?";
		
		Map<String, Object> map = queryForMap(sql, new Object[]{ score, openid });
		if(map == null) return null;
		
		String[] info = {
			map.get("max").toString(),
			map.get("rank").toString()
		};
		return info;
	}
	
	/**
	 * 查询最近30天内的数据
	 * @param openid
	 * @return
	 */
	public static LinkedHashMap<Date, Integer> getSignInScoreByDate(String openid){
		LinkedHashMap<Date, Integer> map = new LinkedHashMap<Date, Integer>();

		String sql = "select score,create_time from signin where openid = ? and create_time > date_sub(curdate(), interval 30 day) order by create_time desc";
		List<Map<String, Object>> list = getJdbc().queryForList(sql, new Object[]{ openid });
		
		for(Map<String, Object> obj : list){
//			map.put(obj.get("create_time").toString(), obj.get("score"));
		}
		return map;
	}

	public static Map<String, Object> getConfig(String appid) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void updateConfig(String ticket, String appid) {
		// TODO Auto-generated method stub
	}

	public static String[] getSignInScore(String openid) {
		String sql = "select score,create_time from signin where openid = ? order by create_time desc limit 1";
		Map<String, Object> map = queryForMap(sql, new Object[]{ openid });
		if(map == null) return null;
		
		String[] data = {
			map.get("score").toString(),
			map.get("create_time").toString()
		};
		return data;
	}

	public static void saveSignIn(String openid, String score, String date) {
		String sql = "insert into signin(openid,score,create_time) values(?,?,?)";
		getJdbc().update(sql, new Object[]{ openid, score, date });
	}
	
	/**
	 * 保存游戏关数
	 * @param num
	 * @param openid
	 * @param type
	 */
	public static String saveRecord(String num,String openid, String type) {
		String sql = "update userinfo set time=?,ids=? where userid=? and type=?";
		int suc = getJdbc().update(sql, new Object[]{
				Long.toString(new Date().getTime()),
				num, openid, type 
		});
		return suc > 0 ? "ok" : "no";
	}
	
	/**
	 * 查询是否有保存关数
	 * @param openid
	 * @param type
	 * @return
	 */
	public static String getNum(String openid, String type) {
		String sql = "select ids from userinfo where type=? and userid=? order by time desc limit 1";
		Map<String, Object> map = queryForMap(sql, new Object[]{ type, openid });
		if(map == null) return null;
		return map.get("ids") !=null ? map.get("ids").toString() : "1";
	}
	
}
