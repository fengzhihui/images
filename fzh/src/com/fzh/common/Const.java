package com.fzh.common;

public class Const {
	/**主图文URL*/
	public static final String MAIN_CLICK_URL = "http://itwx01.duapp.com/fzh/wx/user!doDetail.action?type=1&id=";
	/**子图文URL*/
	public static final String MORE_CLICK_URL = "http://itwx01.duapp.com/fzh/wx/user!doDetail.action?type=2&id=";
	/**用户登录属性*/
	public static final String LOGIN_USER_ATTRIBUTE = Const.class.getName() + "_USER_ATTRIBUTE";
	/**上传文件的保存路径，相对于应用的根目录*/
	public static final String UPLOAD_PIC_PATH = "/wx/uploadimages/";
	/**猜图关数*/
	public static final int GUESS_IMG_COUNT = 476;
	/**成语关数*/
	public static final int GUESS_CY_COUNT = 300;
	/**猜图图片*/
	public static final String GUESS_IMG_URL = "http://ikanav.duapp.com/images/%s.png";
	/**猜图详情页*/
	public static final String GUESS_IMG_DETAIL_URL = "http://ikanav.duapp.com/fzh/views/crz/cy.htm?type=%s&openid=%s";
	
	/**阿伯域名*/
	public static final String ABYY_DOMAIN = "http://ikanav.duapp.com/fzh/";
	/**阿伯影院数据 */
	public static final String MOVIE_DATA_URL = ABYY_DOMAIN + "abyy/movie.html";
	/**阿伯影院AV数据*/
	public static final String MOVIE_AV_URL = ABYY_DOMAIN + "abyy/movie-av.html";
	/**
	 * 阿伯影院详情页
	 */
	public static final String ABYY_JSP_URL = ABYY_DOMAIN + "news/page/abyy.jsp?mv=";
	/**
	 * 阿伯影院播放页
	 */
	public static final String VIDEO_HTML_URL = ABYY_DOMAIN + "video.htm?url=";
	/**
	 * 阿伯影院首页
	 */
	public static final String MVLIST_JSP_URL = ABYY_DOMAIN + "news/movie/mvlist.jsp?type=mvlist&page=";
	/**
	 * 阿伯影院AV首页
	 */
	public static final String AVLIST_JSP_URL = ABYY_DOMAIN + "news/movie/mvlist.jsp?type=avlist&page=";
	/**
	 * 阿伯影院视频链接
	 */
	public static final String VIDEO_URL = "http://zhuanfa.weixin.guazige.cn/film1/MOVIEID.html";
	/**
	 * 阿伯影院图片链接
	 */
	public static final String IMAGE_URL = ABYY_DOMAIN + "images/upload/";
	/**
	 * VIDEO视频链接资源
	 */
	public static final String VIDEO_HTML = "<video src=\"VIDEO_URL\" controls=\"controls\" width=\"100%\" height=\"200\" type=\"video/mp4\"></video>";
	/**
	 * IFRAME视频链接资源
	 */
	public static final String IFRAME_HTML = "<iframe src=\"IFRAME_HTML\" scrolling=\"no\" frameborder=\"0\"  id=\"mainFrame\" width=\"100%\" height=\"200\"></iframe>";
	/**
	 * 搜库资源搜索
	 */
	public static final String SOKU_SEARCH = "http://www.soku.com/m/y/video?q=";

}
