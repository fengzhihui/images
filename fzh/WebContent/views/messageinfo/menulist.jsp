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
function query(){
	document.formList.action = '${path}/messageinfo/calcMenuMsg';
	document.formList.submit();
}
//下拉框保值
$(function(){
     var dt = $('#dt').val();
     $("#date").attr("value", dt);
     
     var slt = $("#date").get(0);
     if(dt !=null && dt.length !=0){
     	slt[dt-1].selected = true;
     }
});
</script>
</head>
<body class="">
	<jsp:include page="/common/header.jsp" flush="true" />
	<jsp:include page="/common/leftbar.jsp" flush="true" />
	<div class="content">
		<div class="container-fluid">
			<form key="formList" cssClass="form-horizontal" name="formList" theme="simple">
			<fieldset>
				<legend>菜单点击量统计列表</legend>
			</fieldset>
			<div class="control-group">
				日期
				<input id="dt" type="hidden" value="${date}"/>
				<select name="date" id="date">
					<option value="1">按日</option>
					<option value="2">按周</option>
					<option value="3">按月</option>
				</select>
				<button type="button" class="btn" onclick="query();">查询</button>
			</div>

			<table class="table table-bordered table-hover table-condensed">
				<thead>
					<tr class="thead-text">
						<th style="text-align: center">
							<a href="javascript:void(0);">菜单名称</a>
						</th>
						<th style="text-align: center" colspan="5">
							<a href="javascript:void(0);">一级菜单</a>
						</th>
						<th style="text-align: center" colspan="5">
							<a href="javascript:void(0);">二级菜单</a>
						</th>
						<th style="text-align: center" colspan="5">
							<a href="javascript:void(0);">三级菜单</a>
						</th>
					</tr>
		
					<tr>
						<th style="text-align: center">
							<a href="javascript:void(0);">统计时间</a>
						</th>
						<!-- 一级菜单 -->
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单1</a>
						</th>
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单2</a>
						</th>
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单3</a>
						</th>
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单4</a>
						</th>
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单5</a>
						</th>
						<!-- 二级菜单 -->
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单1</a>
						</th>
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单2</a>
						</th>
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单3</a>
						</th>
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单4</a>
						</th>
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单5</a>
						</th>
						<!-- 三级菜单 -->
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单1</a>
						</th>
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单2</a>
						</th>
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单3</a>
						</th>
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单4</a>
						</th>
						<th style="text-align: center">
							<a href="javascript:void(0);">子菜单5</a>
						</th>
					</tr>
				</thead>
				<c:if test="${fn:length(list)==0}">
					<tr> 
						<td colspan="16" style="text-align: center">未查询到相关记录！</td>
					</tr>
				</c:if>
				
				<c:forEach items="${list}" var="entry">
					<tr>
						<c:forEach items="${entry}" var="_entry" varStatus="status">
							<c:if test="${_entry != '0'}">
								<c:if test="${status.index == 0}">
									<td style="text-align:center;">${_entry}</td>
								</c:if>
								
								<c:if test="${status.index > 0}">
									<td style="text-align:center;color:red;font-weight:bold;">${_entry}</td>
								</c:if>
							</c:if>
							<c:if test="${_entry == '0'}">
								<td style="text-align:center;">${_entry}</td>
							</c:if>
						</c:forEach>
					</tr>
				</c:forEach>
			</table>
			</form>
		</div>
	</div>
	<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
