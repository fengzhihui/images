<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>股市直播室管理平台</title>
<link rel="shortcut icon" href="${path}/images/wx/wally.jpg"/>
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/theme.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/appmsg.css">
<link rel="stylesheet" href="<%=basePath %>lib/font-awesome/css/font-awesome.css">
<script type="text/javascript" src="<%=basePath %>lib/jquery-1.7.2.min.js" ></script>
<script type="text/javascript" src="<%=basePath %>js/bootstrap.js" ></script>
<style type="text/css">
.block {background: none repeat scroll 0 0 white; border: 1px solid #ccc; border-radius: 5px;}
</style>

</head>

<body>

<div class="navbar">
	<div class="navbar-inner">
		<a class="brand" target="_blank"
			href="${path}/wx.jsp"> <span class="first">fzhwx开发者</span>
			<span class="second">股市直播室管理平台</span>
		</a>
	</div>
</div>
	
<!-- 登录 -->
<div class="row-fluid" style="margin-top: -50px;">
    <div class="dialog">
        <div class="block">
            <p class="block-heading">登 录</p>
            <div class="block-body">
                <form action="<%=request.getContextPath()%>/wx/user!login.action" method="post">
                    <label>用户名</label>
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
        <p class="pull-right" style=""><a href="#" target="blank">注 册</a></p>
        <p><a href="#">忘记密码?</a></p>
    </div>
</div>
</body>
</html>

