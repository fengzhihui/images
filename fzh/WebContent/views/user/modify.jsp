<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<jsp:include page="/common/global.jsp" flush="true" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>fzhwx开发者-公众号管理平台</title>
<link rel="shortcut icon"
	href="${path}/images/wx/wally.jpg" />
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="stylesheet" type="text/css" href="${path}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${path}/css/theme.css">
<link rel="stylesheet" href="${path}/lib/font-awesome/css/font-awesome.css">
<script type="text/javascript" src="${path}/lib/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${path}/js/bootstrap.js"></script>

<script type="text/javascript">
		function chk(){
			var username = $('#username').val();
			var password = $('#password').val();
			if(username ==null || username ==""){
				alert('请填写用户名！');
				return false;
			}else if(password ==null || password ==""){
				alert('请填写密码！');
				return false;
			}else return true;
		}
		function save(){
			if(chk()){
				$.ajax({
				       url: "${path}/user/updatePassword",
				       dataType: "json",
				       data: {
				           "username": $("#username").val(),
				           "password": $("#password").val()
				       },
		               success: function(data) {
				           if(data.flag == "ok"){
				        	   alert("修改成功，请重新登录!");
							   location.href = "${path}/logout";
						   }else if(data.flag == "no"){
							   alert("修改失败，请稍后再试!");
						   }
				       },
				       error: function(e) {
				           //登录失效 
				           if(e.responseText == 'unlogin'){
				               location.href = '${path}/login';
				           }
				       }
				    });
			}
		}
</script>
</head>

<body class="">
	<jsp:include page="/common/header.jsp" flush="true" />
	<jsp:include page="/common/leftbar.jsp" flush="true" />
	<!-- <div id="bg" title="完成配置接口即可使用全部功能" class="bgcover"></div>  -->
	<div class="content">
		<div class="container-fluid">
			<fieldset>
				<legend>修改账户信息</legend>
			</fieldset>
			<table align="center" border="0" cellspacing="10" cellpadding="10">
				<tr>
					<td>用户名：</td>
					<td>
						<input readonly="readonly" style="height: 30px; border-radius: 5px"
						type="text" id="username" value="${user.username }"><font
						color="red">*</font>
					</td>
				</tr>
				<tr>
					<td>密 码：</td>
					<td>
						<input style="height: 30px; border-radius: 5px"
						type="password" id="password" value="${user.password }"><font
						color="red">*</font>
					</td>
				</tr>
				<tr id="tr2">
					<td></td>
					<td>
						<button class="btn-mine" onclick="save();">保 存</button>
						<button class="btn" type="reset">重 置</button></td>
				</tr>
			</table>
		</div>
	</div>
	<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
