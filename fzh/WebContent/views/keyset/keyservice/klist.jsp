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

</head>

<body class="">
	<jsp:include page="/common/header.jsp" flush="true" />
	<jsp:include page="/common/leftbar.jsp" flush="true" />
	<div class="content">
		<div class="container-fluid">
			<form name="form" action="${path}/keyset/list">
		<fieldset>
		<legend>关键字列表</legend>
		<p></p><button type="button" class="btn" onclick="javascript:location.href='${path}/keyset/addUI'">添加关键字</button>
		<p></p>
		<div align="center">
		<table class="table table-bordered table-hover table-condensed">
			<thead align="center">
				<tr>
					<th width="2%" style="text-align: center"><a href="javascript:void(0);">编号</a></th>
					<th width="10%" style="text-align: center"><a href="javascript:void(0);">关键字</a></th>
					<th width="20%" style="text-align: center"><a href="javascript:void(0);">回复内容</a></th>
					<th width="5%" style="text-align: center"><a href="javascript:void(0);">操作</a></th>
				</tr>
			</thead>
			<c:if test="${fn:length(list)==0}">
				<tr> 
					<td colspan="4" style="text-align: center">未查询到相关记录！</td>
				</tr>
			</c:if>
			<c:forEach items="${list}" var="entry" varStatus="idx">
				<tr>
					<td style="text-align: center">${idx.index+1}</td>
					<td style="text-align: center">${entry.keyWord} </td>
					<td style="text-align: center">
						<c:if test="${entry.reType==2}">图文</c:if>
						<c:if test="${entry.reType!=2}">${entry.refText}</c:if>
					</td>
					<td style="text-align: center"><a href="${path}/keyset/updateUI/${entry.keyServiceNo}">修改</a></td>
				</tr>
			</c:forEach>
		</table>
		<jsp:include page="/common/page.jsp" flush="true" />
		</div>
		</fieldset>
	</form>
	</div>
	</div>
	<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
