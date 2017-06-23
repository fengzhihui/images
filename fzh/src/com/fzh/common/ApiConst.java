package com.fzh.common;

/**
 * API接口常量类
 * @author fzh
 *
 */
public class ApiConst {

	/**百度天气*/
	public final static String BAE_WEATHER_URL = "http://api.map.baidu.com/telematics/v3/weather?output=json&ak=6tYzTvGZSOpYB5Oc2YGGOKt8&location=";
	/**百度音乐*/
	public final static String BAE_MUSIC_URL = "http://box.zhangmen.baidu.com/x?op=12&count=1&title=%s$$%s$$$$";
	/**百度公交*/
	public final static String BAE_BUS_URL = "http://api.map.baidu.com/direction/v1?mode=transit&output=xml&ak=860a497074e46a24f7f42388dcf6c540&type=2&origin=%s&destination=%s&region=%s";
	/**有道翻译*/
	public final static String YOUDAO_URL = "http://fanyi.youdao.com/openapi.do?keyfrom=created4you&key=490847661&type=data&doctype=json&version=1.1&q=";
	/**百度音乐*/
	public final static String BAIDU_TING_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.search.catalogSug&query=";
	/**百度音乐播放URL*/
	public final static String BAIDU_TING_PLAY_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.song.play&songid=";
	/**青云客聊天*/
	public final static String TALK_URL = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=";
	
}
