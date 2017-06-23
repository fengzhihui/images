<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>内容页</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript">
	     $(function(){
		     var wxno = $('#wxno').val();
		     if(wxno !=null && wxno !=""){
		 		$('#root').show();
			 }
	     });
	</script>
    <style type="text/css">
    </style>
	</head>

	<body>
	   <input type="hidden" value="${user.wxpublicno}" id="wxno">
       <ul><li>欢迎 <span style="color: red">${user.username}</span> 登录!</li></ul>
       <ul style="margin-left: 20">
       		<li><a href="<%=request.getContextPath()%>/wx/wxpubNo_addWxno.action" target="content">配置接口</a></li> 
       </ul>
       <div id="root" style="display: none">
       		<ul style="margin-left: 20">
       			<li><a href="<%=request.getContextPath()%>/wx/firstjoinkey_doFirstAnswerInput.action" target="content">首次关注回复</a></li>
       		</ul>
       		<ul style="margin-left: 20">
       			<li><a href="<%=request.getContextPath()%>/wx/keyset_doList.action" target="content">关键字回复</a></li>
       		</ul>
       		<ul style="margin-left: 20">
       			<li><a href="<%=request.getContextPath()%>/wx/imagetextinfo_doList.action" target="content">图文素材</a></li>
       		</ul>
       		<ul style="margin-left: 20">
       			<li><a href="<%=request.getContextPath()%>/menuInfoUI" target="content">自定义菜单</a></li> 
       		</ul>
			<ul style="margin-left: 20">
       			<li><a href="<%=request.getContextPath()%>/wx/msginfo_calcUserMsg.action" target="content">消息统计</a></li> 
       		</ul>
			<ul style="margin-left: 20">
       			<li><a href="<%=request.getContextPath()%>/wx/msginfo_calcKeyword.action" target="content">关键字统计</a></li> 
       		</ul>
			<ul style="margin-left: 20">
       			<li><a href="<%=request.getContextPath()%>/wx/msginfo_calcMenuMsg.action" target="content">自定义菜单统计</a></li> 
       		</ul>
       </div>
	</body>
</html>
