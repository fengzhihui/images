package com.fzh.wx.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.fzh.common.Const;
import com.fzh.wx.intf.GuessMyNum;
import com.fzh.wx.service.APIUtilService;
import com.fzh.wx.service.GuessImgService;

public class StringUtil {

	/**
	 * 追加字符串(消息菜单)
	 * 
	 * @return
	 */
	public static String strContent() {
		StringBuffer sb = new StringBuffer();

		sb.append("/::)亲，怎么现在才来，赶快让我为你服务吧~\n请回复【】内的数字获取服务:\n")
				.append(QQFace.emoji(0x2600))
				.append("  【01】天气预报\n")
//				.append(QQFace.emoji(0x1F4EB))
//				.append("  【02】短信阅读\n")
//				.append(QQFace.emoji(0x1F4BB))
//				.append("  【03】新闻阅读\n")
				.append(QQFace.emoji(0x1F3B5))
				.append("  【04】音乐搜听\n")
				.append(QQFace.emoji(0x1F3A5))
				.append("  【05】在线电影\n")
				.append(QQFace.emoji(0x1F3C6))
				.append("  【06】疯狂猜图\n")
				.append(QQFace.emoji(0x1F3C6))
				.append("  【07】疯狂成语\n")
				.append(QQFace.emoji(0x1F511))
				.append("  【08】中英互译\n")
//				.append(QQFace.emoji(0x1F334))
//				.append("  【09】地方搜索\n")
//				.append(QQFace.emoji(0x1F684))
//				.append("  【10】公交查询\n")
				.append(QQFace.emoji(0x1F491))
				.append("  【11】小E陪聊\n")
				.append(QQFace.emoji(0x1F513))
				.append("  【14】高级猜数\n")
				.append(QQFace.emoji(0x1F48F))
				.append("  【15】人脸识别\n")
//				.append(QQFace.emoji(0x1F6B2))
//				.append("  【16】城市旅游\n")
				.append("发送\"?\"返回主菜单");
		return sb.toString();
	}

	/**
	 * 消息内容提示
	 * 
	 * @param type
	 *            1为新闻,2为猜图,3为翻译,4为音乐搜索,5地方搜索,6公交查询,7疯狂成语,8小E陪聊,9猜数游戏
	 * @return
	 */
	public static String msg(String type) {
		String msg = "";
		if (type.equals("0")) {
			msg = "格式: 天气城市\n  例如：天气广州 或 广州天气\n\n发送\"?\"返回主菜单";
		} else if (type.equals("1")) {
			msg = "\n回复数字阅读文章(如:1)\n\n发送\"?\"返回主菜单";
		} else if (type.equals("2")) {
			msg = "\n疯狂猜图已更新至第476关，点击进去通关吧！\n\n发送\"?\"返回主菜单";
		} else if (type.equals("3")) {
			msg = "发送中文或拼音或英文或句子\n\n发送\"?\"返回主菜单";
		} else if (type.equals("4")) {
			msg = "1.发送【歌名】   例如: 阿牛\n" + "2.发送【歌手】   例如: 陈奕迅\n\n发送\"?\"返回主菜单";
		} else if (type.equals("41")) {
			msg = "[难过]未找到适合播放的音乐链接!";
		} else if (type.equals("5")) {
			msg = "1.发送地理位置\n点击窗口底部的\"+\"按钮,选择\"位置\",点击\"发送\"\n默认搜索附近餐厅\n\n"
					+ "2.发送查询地址\n格式1: 城市,地方(逗号分隔)\n例如: 广州,天河区银行"
					+ "\n格式2: 附近地方\n例如: 附近银行、附近餐厅、附近酒店. . ."
					+ "\n\n发送\"?\"返回主菜单";
		} else if (type.equals("51")) {
			msg = "请点击窗口底部的\"+\"按钮,选择\"位置\",点击\"发送\"\n发送位置成功后,请输入【附近+地方名】";
		} else if (type.equals("52")) {
			msg = "1.发送地理位置\n点击窗口底部的\"+\"按钮,选择\"位置\",点击\"发送\"\n默认搜索附近餐厅\n\n"
					+ "2.发送查询地址\n格式1: 城市,地方(逗号分隔)\n例如: 广州,天河区银行"
					+ "\n格式2: 附近地方\n例如: 附近银行、附近餐厅、附近酒店. . ."
					+ "\n\n发送\"?\"返回主菜单";
		} else if (type.equals("6")) {
			msg = "格式: 起点,终点,城市(逗号分隔)\n例如: 岗顶,省汽车站,广州\n如没数据,可以把起终点写具体一点\n\n发送\"?\"返回主菜单";
		} else if (type.equals("7")) {
			msg = "\n疯狂成语已更新至第300关，点击进去通关吧！\n\n发送\"?\"返回主菜单";
		} else if (type.equals("8")) {
			msg = "我是小E，您已经进入聊天模式，我们一起聊天吧!\n\n发送\"?\"返回主菜单";
		} else if (type.equals("9")) {
			msg = "请输入一个任意不重复数字的四位数，例如：1230\n\n查看帮助，请回复【帮助】\n\n发送\"?\"返回主菜单";
		} else if (type.equals("tips")) {// 操作提示
			msg = "\n\n/::)精彩服务请按菜单操作!\n发送\"?\"返回主菜单";
		}
		return msg;
	}

	public static String getMsg(String type) {
		String msg = "";
		int msgT = Integer.parseInt(type);
		switch (msgT) {
		case 1:
			msg = "格式: 天气城市\n  例如：天气广州 或 广州天气";
			break;
		case 2:
			msg = "格式: 发送括号中的关键词\n【短信】\n【短信心语】\n【短信祝福】\n【短信笑话】\n【短信生活】\n【短信词文】\n【短信校园】";
			break;
		case 3:
			msg = "格式: 电影+关键词\n  例如：电影小黄人";
			break;
		case 4:
			msg = "【1】发送地理位置\n点击窗口底部的\"+\"按钮,选择\"位置\",点击\"发送\"\n默认搜索附近餐厅\n\n"
					+ "【2】发送查询地址\n格式: 附近地方\n例如: 附近银行、附近餐厅、附近酒店. . .";
			break;
		default:
			break;
		}
		return msg;
	}

	/**
	 * 【01-16】功能菜单 type：1为默认关键词标记不须入库，2-9为入库标记
	 * 
	 * @param requestMap
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> menuContent(Map<String, String> requestMap, Object data)
			throws Exception {
		String type = "1", respContent = null;
		String inputTxt = requestMap.get("Content").replaceAll("，", ",").trim();// 去空格

		if (isBlank(inputTxt)) return null;

		if (QQFace.isQqFace(inputTxt)) {
			respContent = inputTxt;
		} else if("登录".equals(inputTxt)) {
			respContent = inputTxt;
		} else if ((inputTxt.startsWith("天气") || inputTxt.endsWith("天气"))
				&& inputTxt.length() >= 4) {
			// 获取百度天气
			respContent = APIUtilService.getBAEWeather(inputTxt);
		} else if ("01".equals(inputTxt)) {
			respContent = msg("0");
		} else if ("02".equals(inputTxt) || inputTxt.startsWith("短信")) {
			respContent = DBManager.getMsg(inputTxt);
		} else if ("04".equals(inputTxt) || "音乐".equals(inputTxt)) {
			type = "4";// 记录入库
			respContent = msg(type);// 搜索说明
		} else if ("05".equals(inputTxt) || "电影".equals(inputTxt)) {
			respContent = "格式: 电影+关键词\n  例如：电影小黄人\n\n发送\"?\"返回主菜单";
		} else if ("06".equals(inputTxt) || "猜图".equals(inputTxt)) {
			type = "2";// 记录入库
		} else if ("07".equals(inputTxt) || "成语".equals(inputTxt)) {
			type = "7";// 记录入库
		} else if ("08".equals(inputTxt) || "翻译".equals(inputTxt)) {
			type = "3";// 记录入库
			respContent = msg(type);// 翻译说明
		} else if ("地方".equals(inputTxt) || "09".equals(inputTxt)) {
			type = "5";// 记录入库
			respContent = msg(type);// 搜索说明
		} else if ("公交".equals(inputTxt) || "10".equals(inputTxt)) {
			type = "6";// 记录入库
			inputTxt = msg(type);// 搜索说明
		} else if ("11".equals(inputTxt)) {// 小E陪聊
			type = "8";
			respContent = msg(type);
		} else if ("14".equals(inputTxt)) {// 高级猜数
			type = "9";
			respContent = msg(type);
		} else if ("15".equals(inputTxt)) {// 人脸识别
			respContent = "请发送一张你的照片过来";
		} else if ("16".equals(inputTxt)) {// 城市旅游
			respContent = "发送：城市名+旅游，例如：广州旅游";
		} else if ("帮助".equals(inputTxt)) {
			respContent = GuessMyNum.gameDesc();// 猜数游戏帮助
		} else if(inputTxt.startsWith("猜图") || inputTxt.startsWith("成语")) {
			respContent = getAnswer(inputTxt);
		} else if ((inputTxt.startsWith("旅游") || inputTxt.endsWith("旅游"))
				&& inputTxt.length() >= 4) {// 旅游
			type = "imgtxt";// 图文消息
			respContent = MessageManager.getTravelCity(requestMap);
		} else if (inputTxt.startsWith("电影")) {// 电影
			requestMap.put("name", inputTxt.replace("电影", "").replace("+", ""));
			type = "imgtxt";// 图文消息
			respContent = "getMovieNews";
		} else if ("?".equals(inputTxt) || "？".equals(inputTxt)) {
			respContent = StringUtil.strContent();// 主菜单
		} else {
			// 用户输入为关键词或2-7功能回复
			type = "record";
			respContent = inputTxt;
		}
		requestMap.put("type", type);
		requestMap.put("respContent", respContent);
		return requestMap;
	}

	/**
	 * 随机广告
	 * 
	 * @return
	 */
	public static Map<String, String> randomAds() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", "是什么让她如此疯狂的和路人发生性.关系！");
		map.put("img", "http://qqmpwx.duapp.com/sjznat/img/0.gif");
		map.put("link", "");
		list.add(map);
		map = new HashMap<String, String>();
		map.put("title", "让她高潮不断原来这么简单！");
		map.put("img", "http://qqmpwx.duapp.com/sjznat/img/0.jpg");
		map.put("link", "");
		list.add(map);
		map = new HashMap<String, String>();
		map.put("title", "为何20岁青春少女痴迷50岁老男人？");
		map.put("img", "http://qqmpwx.duapp.com/sjznat/img/3.png");
		map.put("link", "");
		list.add(map);
		map = new HashMap<String, String>();
		map.put("title", "夫妻房事被突然中断，影响男性健康!");
		map.put("img", "http://qqmpwx.duapp.com/sjznat/img/1.gif");
		map.put("link", "");
		list.add(map);
		Collections.shuffle(list);
		return list.get(0);
	}

	public static String getTitle() {
		if (getRandNum(3) / 2 == 0) {
			return "曾经的陈老师自己吃肉，也不忘给大家喝汤，这就是一种贡献！";
		}
		return "当你有肉吃的时候，别忘记给别人喝点汤！";
	}
	
	public static int getRandNum(int num){
		Random rand = new Random();
		return rand.nextInt(num);
	}
	
	public static boolean isBlank(String str) {
		if (str == null || "".equals(str))
			return true;
		return false;
	}
	
	public static String getAnswer(String inputTxt) {
		if(inputTxt.length() > 3) {
			String type = inputTxt.startsWith("猜图") ? "2" : "7";
			inputTxt = inputTxt.replace("猜图", "").replace("成语", "");
			Integer num = null;
			try {
				num = Integer.parseInt(inputTxt);
				if("2".equals(type) && num > Const.GUESS_IMG_COUNT) {
					return "当前猜图总关数为：" + Const.GUESS_IMG_COUNT;
				}
				if("7".equals(type) && num > Const.GUESS_CY_COUNT) {
					return "当前成语总关数为：" + Const.GUESS_CY_COUNT;
				}
				return GuessImgService.getAnswer(type, num.toString());
			} catch (Exception e) {
				return "请回复 猜图+数字 或 成语+数字，如：猜图1";
			}
		}
		return null;
	}
	
	public static String getShortStr(String fromUserName) {
		BigInteger bigInteger = new BigInteger(Math.abs(fromUserName.hashCode()) + "");
		return bigInteger.toString(36).toUpperCase();
	}
	
}
