<%@ page import="net.sf.json.JSONObject"%>
<%@ page import="com.fzh.wx.util.WeixinUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	//ilegend www.wenjuan.com
	//http%3A%2F%2Fmpwxq0.duapp.com%2Ffzh%2Fhb%2Fpage%2Findex.jsp
	String headimgurl = "", nickname = "";
	String code = request.getParameter("code"), appid = "wxdd5dc1ca73d8986d", secret = "c2a8207ecc93d9cccc5485b6bf9be085";
	String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	JSONObject jsonObject = WeixinUtil.httpRequest(url.replaceAll("CODE", code).replaceAll("APPID", appid).replaceAll("SECRET", secret), "GET", null);
	if(null != jsonObject){
		nickname = jsonObject.getString("nickname");
		headimgurl = jsonObject.getString("headimgurl");
	}

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no, minimal-ui">
<meta name="viewport" content="width=320.1,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no, minimal-ui">
<meta name="apple-mobile-web-app-capable" content="yes">
<title>恭喜发财，大吉大利！</title>
<style> body{ font:normal 14px Tahoma,"Lucida Grande",Verdana,"Microsoft Yahei",STXihei,hei; background:#080808; background-size:100%; color:#353535; } body,div,a,ul,li,p,span{ padding:0; margin:0; } a{ text-decoration:none; color:#000; } .first-box{ width:81.67%; position:absolute; left:9.165%; top:10%; } img.body{ width:100%; } .user-headimg{ position: absolute; border-radius: 10000px; left: 50%; top: 9.75%; width: 20.65%; margin-left: -10.5%; } .hot-click{ position: absolute; border-radius: 10000px; left: 50%; top: 61%; width: 36%; height: 26%; margin-left: -18%; } .nickname{ width: 100%; position: absolute; text-align: center; color: #FFF; top: 28.5%; font-size:1.15em; } .second-box{ width:100%; height:100%; position:absolute; overflow:hidden; top:0; left:0; } .second-wrapper{ overflow:hidden; width:auto; position:relative; } .second-bg{ width:100%; } .second-headimg{ position: absolute; border-radius: 10000px; left: 50%; top: 13%; width: 23%; margin-left: -11.5%; } .second-nickname{ width: 100%; position: absolute; text-align: center; top: 26.8%; font-size:1.25em; } .money{ position:absolute; text-align:center; width:100%; top:37%; color:#000; } .money-number{ font-size:4.5em; padding-left:0.25em; } .yuan{ font-size:1.1em; position:relative; top:-1px; } .tips-box{ position:absolute; top:50%; width:100%; height:100px; margin-top:-50px; background:#fff; overflow:hidden; z-index:9999; text-align:center; } .explain{ font-size:18px; padding:5px 8px; margin-top:4px; margin-bottom:8px; } .zhenggu-btn{ background-color:#FE8007; color:#fff; padding:6px 14px; border-radius:3px; } .bg-cover{ width:100%; height:100%; top:0; left:0; position:fixed; background:#000; opacity:0.6; z-index:999; }
</style>
<script>
var _czc = _czc || [];
_czc.push(["_setAccount", "1254894674"]);
if(window.location.search.indexOf('from=singlemessage') != -1) {
	refer = "单聊";
}
else if(window.location.search.indexOf('from=groupmessage') != -1) {
	refer = "群聊";
}
else if(window.location.search.indexOf('from=qrcode') != -1) {
	refer = "二维码";
}
else if(window.location.search.indexOf('from=timeline') != -1) {
	refer = "朋友圈";
}
else if(window.location.search.indexOf('from=') != -1) {
	refer = "其他来路";
}
else if(window.location.search.indexOf('preview=1') != -1) {
	refer = "预览红包";
}
else
{
	refer = "没有来路";
}
_czc.push(﻿["_trackEvent","红包","查看红包",refer]);
</script>
</head>
<body>
<div style="display:none">
    <img src="../dn-h6lab.qbox.me/static/images/hongbao_icon.jpg" tppabs="https://dn-h6lab.qbox.me/static/images/hongbao_icon.jpg">
</div>


<div class="first-box">
    <img class="body" src="../dn-h6lab.qbox.me/static/images/fools_1.png" tppabs="https://dn-h6lab.qbox.me/static/images/fools_1.png">
    <img class="user-headimg" src="<%=headimgurl %>">
    <div class="hot-click"></div>
    <div class="nickname"><%=nickname %></div>
</div>
<div class="second-box" style="display:none">
    <div class="second-wrapper">
        <img class="second-bg" src="../dn-h6lab.qbox.me/static/images/fools_2.png" tppabs="https://dn-h6lab.qbox.me/static/images/fools_2.png">
        <img class="second-headimg" src="<%=headimgurl %>">
        <div class="second-nickname"><%=nickname %>的红包</div>
        <div class="money">
            <span class="money-number">1888</span>
            <span class="yuan">元</span>
        </div>
    </div>
</div>
<div class="tips-box" style="display:none" id="tips">
    <div class="explain">别看了，没有天上掉下来的馅饼！<br>快去整回别人吧→_→
    </div>
    <a class="zhenggu-btn" href="#" tppabs="http://hb1.greenmasterfit.cn/tricky/create?from=direct" onClick="javascript:_czc.push(﻿['_trackEvent','点击事件','创建红包']);">我也要整蛊</a>
<!--    <a class="zhenggu-btn" href="http://eksx9n.chinajinjun.cn/tricky2/qrcode">我也要整蛊</a>-->
	 	
</div>

<div class="tips-box" style="display:none" id="qunfaTips">
    <div class="explain">一个一个发好友不够痛快？<br/>新增群发利器→_→
    </div>
    <a class="zhenggu-btn" href="tricky/qunfa-tid=1253249.htm" tppabs="http://eksx9n.chinajinjun.cn/tricky/qunfa?tid=1253249" onclick="javascript:_czc.push(﻿['_trackEvent','点击事件','群发利器']);">群发整蛊好友</a>
</div>

<div class="tips-box" style="display:none" id="adTips">
    <div class="explain">您是整蛊达人！我们邀请您参与活动（可继续整蛊好友，过瘾后参与）：
    </div>
    <a class="zhenggu-btn" href="javascript:if(confirm('http://m2xqx4.shanghaidongzhe.cn/cps/baidu_redirect  \n\n���ļ��޷��� Teleport Ultra ����, ��Ϊ ����һ�����·���ⲿ������Ϊ�����ʼ��ַ�ĵ�ַ��  \n\n�����ڷ������ϴ���?'))window.location='http://m2xqx4.shanghaidongzhe.cn/cps/baidu_redirect'" tppabs="http://m2xqx4.shanghaidongzhe.cn/cps/baidu_redirect" onClick="javascript:_czc.push(﻿['_trackEvent','点击事件','广告-百度']);">100M-4G流量随便领</a>
</div>

<div class="bg-cover" style="display:none"></div>
<script type="text/javascript" src="https://dn-h6lab.qbox.me/static/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
    var times_f = 0, times_t = 0;
    $(function(){
        var url = encodeURIComponent(window.location.href);
		$.ajax({
			type: "GET",
			dataType: "jsonp",
			url: "http://weixin.ok38.com/Weixin/jssdk/kantianshang?url=" + url + "&callback=?",
			data: {},
			success: function (result) {
				callback(result);
			}
		});

        function callback(config){
            wx.config(config);
            wx.ready(function(){
                wx.showOptionMenu();
                wx.onMenuShareAppMessage({
                    title: '恭喜发财，大吉大利！',
                    desc: '点击领取红包',
                    link: 'http://upbalance.com.cn/tricky?tid=1253249',
                    imgUrl: '../lab.h6app.com/static/images/hongbao_icon.jpg'/*tpa=http://lab.h6app.com/static/images/hongbao_icon.jpg*/,
					success: function () {
						_czc.push(﻿["_trackEvent","红包-发送给好友","成功"]);
                        times_f++;
                        _czc.push(﻿["_trackEvent","红包-发送给好友-第"+times_f+"次","成功"]);
                        if(times_f >= 3)
                            $('#adTips').show();
					},
					cancel: function () { 
						_czc.push(﻿["_trackEvent","红包-发送给好友","取消"]);
						//$('#qunfaTips').show();
					}
                });
                wx.onMenuShareTimeline({
                    title: '恭喜发财，大吉大利！',
                    link: 'http://upbalance.com.cn/tricky?tid=1253249',
                    imgUrl: '../lab.h6app.com/static/images/hongbao_icon.jpg'/*tpa=http://lab.h6app.com/static/images/hongbao_icon.jpg*/,
					success: function () {
						_czc.push(﻿["_trackEvent","红包-分享到朋友圈","成功"]);
                        times_t++;
                        _czc.push(﻿["_trackEvent","红包-发送给好友-第"+times_t+"次","成功"]);
                    },
					cancel: function () { 
						_czc.push(﻿["_trackEvent","红包-分享到朋友圈","取消"]);
					}
                });
            });
        }
    });
</script>
<div style="display:none">
    <script src="http://s95.cnzz.com/z_stat.php?id=1254894674&web_id=1254894674" language="JavaScript"></script>
</div><script type="text/javascript">
    var fools_show=function(){
        $(".bg-cover").show();
        $("#tips").show();
		$('#h6appLogo').show();
    };
    $(function(){
        $(".hot-click").click(function(){
            $(".first-box").hide();
            $(".second-box").show();
            var t=setTimeout('fools_show()',1750);
        });
        $(".second-box").click(fools_show);
    });
</script>

<div style="position:fixed;width:100%;bottom:0;text-align:center;display:none;z-index:10000;" id="h6appLogo">
	<img src="../www.h6app.cn/static/images/bot_logo.png-a.png" tppabs="http://www.h6app.cn/static/images/bot_logo.png?a">
</div>
</body>
</html>