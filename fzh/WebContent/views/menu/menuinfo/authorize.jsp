<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>fzhwx开发者-公众号管理平台</title>
<link rel="shortcut icon" href="${path}/images/wx/wally.jpg"/>
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="stylesheet" type="text/css" href="${path}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${path}/css/theme.css">
<link rel="stylesheet" href="${path}/lib/font-awesome/css/font-awesome.css">
<script type="text/javascript" src="${path}/lib/jquery-1.7.2.min.js" ></script>
<script type="text/javascript" src="${path}/js/bootstrap.js" ></script>
<script type="text/javascript">
	function chkVal(){
		var appid = $("#appid").val();
		 var secret = $("#secret").val();
		 if(secret=="" || appid==""){
			 alert("应用ID或应用密钥不能为空!");
			 return false;
		 }else if(secret.length<32 || appid.length<18){
			 alert("应用id或应用密钥长度不对!");
			 return false;
		 } else {
			 return true;
		 }
	}
	
	function save(){
		if(chkVal()){
	        document.form.action = "${path}/wxpubno/authorizeMenu";
	        document.form.submit();
	    }
	}
</script>

</head>

<body class="">
	<jsp:include page="/common/header.jsp" flush="true" />
	<jsp:include page="/common/leftbar.jsp" flush="true" />
	<div class="content">
		<div class="container-fluid">
		  <fieldset>
			 <legend>自定义菜单授权</legend>
		  </fieldset>
		<p></p>
		<div align="center">
			<h3>在公众平台获取申请到的AppId和AppSecret，将这两个参数填入下面的表单中，请保存。</h3>
			<a href="https://mp.weixin.qq.com/" target="_blank">立即登录公众平台获取</a>
		</div>
		<p></p>
		<form name="form" method="post">
		<table cellspacing="10" cellpadding="10" align="center">
			<tr>
				<td>应用ID：</td>
				<td>
					<input id="appid" name="appid" style="width:300px; border-radius:5px;">
					<font color="red">*</font>公众平台申请到的AppId
				</td>
			</tr>
			<tr>
				<td>应用密钥：</td>
				<td>
					<input id="secret" name="secret" style="width: 300px;border-radius: 5px;">
					<font color="red">*</font>公众平台申请到的AppSecret
				</td>
			</tr>
			<tr align="center">
				<td colspan="2">
					<button type="button" class="btn-mine" onclick="save();">保&nbsp;存</button>
					<button id="cancel-btn" type="button" class="btn" onclick="javascript: history.go(-1)">取消</button>
				</td>
			</tr>
		</table>
	</form>
	</div>
	</div>
	<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
