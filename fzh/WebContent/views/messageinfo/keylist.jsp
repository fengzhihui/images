<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
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
<link rel="stylesheet" type="text/css"
	href="${path}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${path}/css/theme.css">
<link rel="stylesheet"
	href="${path}/lib/font-awesome/css/font-awesome.css">
<script type="text/javascript" src="${path}/lib/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${path}/js/bootstrap.js"></script>

<script type="text/javascript">
	function query() {
		document.formList.action = '${path}/messageinfo/calcKeyword';
		document.formList.submit();
	}

	//下拉框保值
	$(function() {
		var dt = $('#dt').val();
		$("#date").attr("value", dt);

		var slt = $("#date").get(0);
		if (dt != null && dt.length != 0) {
			slt[dt - 1].selected = true;
		}
	});
</script>
</head>
<body class="">
	<jsp:include page="/common/header.jsp" flush="true" />
	<jsp:include page="/common/leftbar.jsp" flush="true" />
	<div class="content">
		<div class="container-fluid">
			<form key="formList" name="formList" theme="simple" cssClass="form-horizontal" method="POST">
				<fieldset>
					<legend>关键字统计列表</legend>
					<div class="control-group">
						关键字 <input style="border-radius: 5px" id="keyword" name="keyword"
							type="text" value="${keyword}" /> &nbsp;&nbsp;&nbsp;
						日期 <input id="dt" type="hidden" value="${date}" />
						<select name="date" id="date">
							<option value="1">按日</option>
							<option value="2">按周</option>
							<option value="3">按月</option>
						</select>
						<button type="button" class="btn" onclick="query()">查询</button>
					</div>

					<table class="table table-bordered table-hover table-condensed">
						<thead>
							<tr>
								<th class="span2" style="text-align: center"><a
									href="javascript:void(0);">统计时间</a></th>
								<th class="span2" style="text-align: center"><a
									href="javascript:void(0);">关键字</a></th>
								<th class="span2" style="text-align: center"><a
									href="javascript:void(0);">输入次数</a></th>
							</tr>
						</thead>
						<c:if test="${fn:length(list)==0}">
							<tr> 
								<td colspan="3" style="text-align: center">未查询到相关记录！</td>
							</tr>
						</c:if>
						<c:forEach items="${list}" var="entry">
							<tr>
								<td style="text-align: center">${entry.msg_send_time}</td>
								<td style="text-align: center">${entry.msg_content}</td>
								<td style="text-align: center">${entry._count}</td>
							</tr>
						</c:forEach>
					</table>
				</fieldset>
			</form>
		</div>
	</div>
	<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
