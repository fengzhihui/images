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
		function chk(){
			var appid = $('#appid').val();
			var appsecret = $('#appsecret').val();
			var public_no = $('#public_no').val();
			if(public_no =="" || appid =="" || appsecret ==""){
				alert('请填写完整！');
				return false;
			}else return true;
		}
		function save(){
			if(chk()){
				$.post(
					'${path}/wxpubno/saveJSSDK',
					{   "appid": $("#appid").val(),
						"appsecret": $("#appsecret").val(),
						"publicNo": $("#public_no").val()
					},
					function(data){
						if(data.data == "ok"){
							alert("保存成功！");
							$(".btn-mine").attr("disabled", true);
							$(".btn-mine").addClass("btn");
						}
					}
				,"json");
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
			<legend>添加分享配置信息</legend>
		</fieldset>
		<table align="center" border="0" cellspacing="10" cellpadding="10">
			<tr>
				<td>账号名称：</td>
				<td>
					<input style="height:30px; border-radius: 5px" type="text" id="public_no"><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td>APPID：</td>
				<td>
					<input style="height:30px; border-radius: 5px" type="text" id="appid"><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td>APPSECRET：</td>
				<td>
					<input style="height:30px; border-radius: 5px" type="text" id="appsecret"><font color="red">*</font>
				</td>
			</tr>
			<tr id="tr2">
			    <td></td>
				<td><button class="btn-mine" onclick="save();">保 存</button>
					&nbsp;<button class="btn" type="reset">重 置</button>
				</td>
			</tr>
		</table>
		</div>
	</div>
	<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
