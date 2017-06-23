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
			var url = $('#url').val();
			var appid = $('#appid').val();
			var scope = $("input[name='scope']:checked").val();
			if(url =="" || scope ==null || appid==""){
				alert('请填写完整！');
				return false;
			}else return true;
		}
		function encrypt(){
			$.getJSON("${path}/info", 
				{ method: "encrypt", secret: $("#secret").val() },
				function(data){
					$("#secret").val(data.secret);
				}
			);
		}
		function createURL(){
			var scope = $("input[name='scope']:checked").val();
			var oauth_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECTURI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
			if(chk()){
				oauth_url = oauth_url.replace("SCOPE", scope).replace("APPID", $("#appid").val()).replace("REDIRECTURI", encodeURIComponent($("#url").val()));
				$("#showurl").html(oauth_url);
				$("#code").show();
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
			<legend>网页授权配置</legend>
		</fieldset>
		<table align="center" border="0" cellspacing="10" cellpadding="10">
			<tr>
				<td>SCOPE：</td>
				<td>
					<input name="scope" type="radio" value="snsapi_base"/><font color="red">snsapi_base</font>&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="scope" type="radio" value="snsapi_userinfo" checked="checked"/><font color="red">snsapi_userinfo</font>
				</td>
			</tr>
			<tr>
				<td>APPID：</td>
				<td>
					<input style="height:25px; width:500px; border-radius:5px" type="text" id="appid"><font color="red"> *</font>
				</td>
			</tr>
			<tr>
				<td>SECRET：</td>
				<td>
					<input style="height:25px; width:500px; border-radius:5px" type="text" id="secret" placeholder="JS调用加密使用"/>
					<button style="margin-bottom:10px;" class="btn-mine" onclick="encrypt();">加&nbsp;密</button>
				</td>
			</tr>
			<tr>
				<td>REDIRECTURI：</td>
				<td>
					<input style="height:25px; width:500px; border-radius: 5px" type="text" id="url"/><font color="red"> *</font>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div id="showurl" style="word-break:break-all;"></div>
					<div id="code" style="display:none;">
					<br/>调用方法：<br/>
					$.ajax({
				    	url: "http://fzh2014.duapp.com/fzh/info?jsoncallback=?",
				    	type: "GET",
				    	data: {
				    		code: getParam("code"),
				    		method: "userinfo",
				    		type: "jsonp",
							appid: "",
							secret: "",
							scope: ""
						},
				    	dataType: "jsonp",
				    	success: function(data) {
				    		//data.nickname data.headimgurl
				    	},
				    	error: function(a, b, c) {
							alert('请再试一次！');
				    	}
					});
					</div>
				</td>
			</tr>
			<tr id="tr2">
			    <td></td>
				<td><button style="width:100px;" class="btn-mine" onclick="createURL();">生成URL</button>
				</td>
			</tr>
		</table>
		</div>
	</div>
	<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
