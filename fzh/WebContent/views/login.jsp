<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>fzhwx开发者-公众号管理平台</title>
<link rel="shortcut icon" href="<%=path%>/images/wx/wally.jpg"/>
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="<%=path%>/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/css/theme.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/css/appmsg.css">
<link rel="stylesheet" href="<%=path%>/lib/font-awesome/css/font-awesome.css">
<script type="text/javascript" src="<%=path%>/lib/jquery-1.7.2.min.js" ></script>
<script type="text/javascript" src="<%=path%>/js/bootstrap.js" ></script>
<style type="text/css">
.block {background: none repeat scroll 0 0 white; border: 1px solid #ccc; border-radius: 5px;}
</style>

</head>

<body>

<div class="navbar">
	<div class="navbar-inner">
		<a class="brand" target="_blank"
			href="http://zhihui2014.sinaapp.com/wx.jsp"> <span class="first">fzhwx开发者</span>
			<span class="second">公众号管理平台</span>
		</a>
	</div>
</div>
	
<div align="center" style="display: none; line-height: 30px; font-size: 16px; margin: 20px 0 0 0;">
	<span style="font-size: 20px; color: green;">注 册 流 程</span>
	<br/><p>
	关注微信公众号<font size="5px" color="red">fzhwx开发者</font>，通过发送“ MF ”获取登录名和密码（已关注用户直接发送“ MF ”），
	<br>
	即可长期免费使用“ 天气预报 ”、“ 短信阅读 ”、“ 电影搜索 ”、“ 地方搜索 ”、“ 关键字回复 ”功能！
	<br/>
	<br/>
	<img style="margin-right: 20px;width:200px;height:220px;" title="公众号: f_zhihui二维码" src="<%=path%>/images/wx/wx.jpg">
	<img style="width:200px;height:220px;" title="个人微信号: fzhihui二维码" src="<%=path%>/wx/images/fzh.jpg">
</div>
    
<!-- 登录 -->
<div class="row-fluid" style="margin-top: -50px;">
    <div class="dialog">
        <div class="block">
            <p class="block-heading">用 户 登 录</p>
            <div class="block-body">
                <form action="<%=request.getContextPath()%>/login" method="post">
                    <label>账 号</label>
                    <input id="username" name="username" type="text" class="span12" style="border-radius: 5px;">
                    <label>密 码</label>
                    <input name="pwd" type="password" class="span12" style="border-radius: 5px;">
                    <input type="submit" class="btn-mine pull-right" value="登 录"/>
                    <font id="msg" color="red">${msg}</font>
                    <label class="remember-me"><input type="checkbox"> 记住账号</label>
                    <div class="clearfix"></div>
                </form>
            </div>
        </div>
        <p class="pull-right" style=""><a href="javascript:showQrcode();" target="blank">注 册</a></p>
        <p><a href="#">忘记密码?</a></p>
        <div align="center" style="display:none;" id="Qrcode">
        	<img style="margin-top:20px;width:200px;height:200px;" title="公众号: f_zhihui二维码" src="<%=path%>/images/wx/wx.jpg">
        	<p style="padding:5px;">扫码关注，回复"登录"获取</p>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
	function showQrcode(){
		$("#Qrcode").show();
	}
</script>
</html>
