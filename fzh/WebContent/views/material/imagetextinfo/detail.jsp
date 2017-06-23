<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width,minimum-scale=1,maximum-scale=1,initial-scale=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<style>
body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,code,form,fieldset,legend,input,textarea,p,blockquote,th,td
	{
	margin: 0;
	padding: 0;
}

body {
	padding: 15px 10px 0;
	position: relative;
	height: 100%;
	color: #444;
	font-family: Microsoft YaHei, Helvetica, STHeiti STXihei,
		Microsoft JhengHei, Tohoma, Arial;
	background: #F6F6F6;
}

#article-name {
	font-size: 20px;
	color: black;
	font-weight: 200;
	word-break: normal;
	word-wrap: break-word;
}

.summary {
	margin: 20px auto;
	text-indent: 2em;
}

#create_time {
	font-size: 11px;
	color: #8C8C8C;
	margin: 0;
}

.cover {
	text-align: center;
	margin-bottom: 20px;
}

.cover img {
	max-width: 100%;
	height: auto;
}

.content {
	margin-bottom: 30px;
}

.page-url {
	margin-top: 10px;
	border-top: 1px solid #E5E5E5;
	text-align: center;
}

.page-url-link {
	font-size: 14px;
	line-height: 2.5;
	text-decoration: none;
	text-shadow: 0 1px white;
	-webkit-text-shadow: 0 1px #ffffff;
	-moz-text-shadow: 0 1px #ffffff;
	color: #CACACA;
}

img {
	max-width: 100%;
}

.source {
	color: #607FA6;
	font-size: 14px;
	text-decoration: none;
	text-shadow: 0 1px #ffffff;
}

.activity-meta {
	display: inline-block;
	margin-left: 8px;
	padding-top: 2px;
	padding-bottom: 2px;
	color: #8c8c8c;
	font-size: 11px;
}

a.activity-meta {
	text-decoration: none;
	outline: 0;
	color: #607FA6;
}
</style>
<title>${imageText.title}</title>
</head>
<body>

	<div>
		<div class="header" style="position: relative;">
			<h1 id="article-name">${imageText.title}</h1>
			<span id="create_time">${imageText.createTime}</span>

		</div>
		<div class="summary">
			<blockquote>${imageText.digest}</blockquote>
		</div>
		<div class="cover">
			<img src="${path}/images/upload/${imageText.imageUrl}">
		</div>

		<div class="content">${imageText.mainText}</div>

		<c:if test="${imageText.sourceUrl !=null && imageText.sourceUrl != ''}">
			<div class="source">
				<a href="${imageText.sourceUrl}">阅读原文</a>
			</div>
		</c:if>

	</div>

</body>
</html>