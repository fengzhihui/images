<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<jsp:include page="/common/global.jsp" flush="true" />
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
		$(function(){
			var wxno = $('#id').val();
			if(wxno !=null && wxno !=""){
				$('#tr1').show();
				$('#tr2').hide();
			}
		});
		function chk(){
			var name = $('#name').val();
			var id = $('#id').val();
			var token = $('#token').val();
			if(name ==null || name ==""){
				alert('请填写公众号名称！');
				return false;
			}else if(id ==null || id ==""){
				alert('请填写公众号原始ID！');
				return false;
			}else if(token ==null || token ==""){
				alert('请填写TOKEN！');
				return false;
			}else return true;
		}
		function save(){
			if(chk()){
				var url = '${path}/wxpubno/saveWxno';
				$.post(url,
					{"wxname":$("#name").val(),"wxpubno":$("#id").val(),"token":$("#token").val()},
					function(data){
						if(data != null){
							$('#url').val("${basePath}" + data);
							$('#tr1').show();
							$('#tr2').hide();
							$('#tip').show();
						}
					}
				,"text");
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
			<legend>配置公众账号信息</legend>
		</fieldset>
		<table align="center" border="0" cellspacing="10" cellpadding="10">
			<tr>
				<td>公众号名称：</td>
				<td>
					<input style="height:30px; border-radius: 5px" type="text" id="name" value="${wxpnlist.companyName }"><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td>公众号原始ID：</td>
				<td>
					<input style="height:30px; border-radius: 5px" type="text" id="id" value="${wxpnlist.weixinPublicNo}"><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td>TOKEN：</td>
				<td>
					<input style="height:30px; border-radius: 5px" type="text" id="token" value="${wxpnlist.token}"><font color="red">*</font>
				</td>
			</tr>
			<tr id="tr1" style="display: none;">
				<td>URL：</td>
				<td>
					<input id="url" style="height:30px;width: 600px; border-radius: 5px" type="text" value="${basePath}${wxpnlist.interfaceUrl}">
					<p>
					<span id="tip" style="display:none;color:red;font-size:16px;font-weight:bold;">
						请将URL和TOKEN配置到你的公众账号！！！
						<a href="https://mp.weixin.qq.com/" target="_blank">登录配置</a>
					</span>
				</td>
			</tr>
			<tr id="tr2">
			    <td></td>
				<td><button class="btn-mine" onclick="save();">保&nbsp;存</button>
					&nbsp;<button class="btn" type="reset">重&nbsp;置</button>
				</td>
			</tr>
		</table>
		</div>
	</div>
	<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
