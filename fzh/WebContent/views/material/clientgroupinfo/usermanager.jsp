<%@ page language="java" contentType="text/html;charset=utf-8"%>
<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
<title>用户管理</title>
<meta name="generator" content="editplus">
<meta name="author" content="">
<meta name="keywords" content="">
<meta name="description" content="">

<script>
function doRefreshByGroupId(groupId) {
	document.frames[1].doRefresh(groupId);
}
</script>

</head>

<frameset rows="*" cols="230,*" framespacing="4" frameborder="yes"
	border="4" bordercolor="#D5E5D5">
	<frame src="clientinfo_groupAndCount.do" name="left">
	<frame src="clientinfo_list.do" name="mainContent">
</frameset>
<noframes>
	<body>frame is not supoorted by you browser.
	</body>
</noframes>
</html>