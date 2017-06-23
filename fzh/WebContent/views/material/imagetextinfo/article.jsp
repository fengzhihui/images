<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>fzhwx开发者-公众号管理平台</title>
<link rel="shortcut icon" href="images/wx/wally.jpg" />
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="stylesheet" type="text/css"
	href="${path}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${path}/css/appmsg.css">
<link rel="stylesheet" type="text/css" href="${path}/css/theme.css">
<link rel="stylesheet"
	href="${path}/lib/font-awesome/css/font-awesome.css">
<script type="text/javascript" src="${path}/lib/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${path}/js/bootstrap.js"></script>

<!-- Demo page code -->
<style type="text/css">
.pagination {
	margin: 0 70px;
	float: right;
}

#first_col {
	display: inline-block;
	zoom: 1;
	*display: inline;
}

#second_col {
	margin-left: 15px;
	display: inline-block;
	zoom: 1;
	*display: inline;
}

.add-btn {
	height: 90px;
	margin: 0 18px;
	color: #b5b5b5;
	background: transparent url('../images/wx/appmsg-icon.png') no-repeat
		50% -242px;
}

.multi-access {
	background-position: 50% -342px;
}

ul {
	padding: 0;
	margin: 0;
}

li {
	list-style-type: none;
}

.sub-msg-item {
	padding: 12px 14px;
	overflow: hidden;
	zoom: 1;
	border-top: 1px solid #c6c6c6;
}

.thumb {
	float: right;
	font-size: 0;
}

.thumb .default-tip {
	font-size: 16px;
	color: #c0c0c0;
	width: 70px;
	line-height: 70px;
	border: 1px solid #b2b8bd;
}

.thumb img {
	width: 70px;
	height: 70px;
	border: 1px solid #b2b8bd;
}

.sub-msg-item .msg-t {
	margin-left: 0;
	margin-right: 85px;
	margin-top: 0;
	padding-left: 4px;
	padding-top: 12px;
	line-height: 24px;
	max-height: 48px;
	font-size: 14px;
	overflow: hidden;
}
</style>
<script type="text/javascript">
	$(function() {
		//绑定删除事件
		$(".del-btn").click(
				function() {
					if (confirm("确定删除本图文?")) {
						var $delTarget = $(this);
						$.post("${path}/imagetextinfo/remove/" + $(this).attr("data-rid"), 
						function(data) {
							if (data != null && data == true) {
								$delTarget.closest(".msg-item-wrapper").remove();
								$("#total_count").text(parseInt($("#total_count").text()) - 1);
							} else {
								alert("此图文在问答回复中已经被关联，请先解除绑定。");
							}		
						}, "json");
					}
				});

		//绑定编辑事件
		$(".edit-btn").click(
		  function() {
			//多图文
			if ($(this).attr("data-mul") == "true") {
				location.href = "${path}/imagetextmore/updateUI/" + $(this).attr("data-rid");
			} else {
				location.href = "${path}/imagetextinfo/updateUI/" + $(this).attr("data-rid");
		    }
		});
	 });
</script>
</head>
<body class="">
	<jsp:include page="/common/header.jsp" flush="true" />
	<jsp:include page="/common/leftbar.jsp" flush="true" />
	<div class="content">
		<div class="container-fluid">
			<div class="container">
				<div class="containerBox">
					<div class="boxHeader">
						<h4>素材管理</h4>
					</div>
					<div class="content">
						<h3 class="page-sub-hd">
							图文列表(共<span id="total_count">${totalCount}</span>条)
						</h3>
						<div class="group page-nav">
							<div class="pagination">
								<ul>
									<%
										Integer currentPage = (Integer) request.getAttribute("page");
										Integer totalCount = (Integer) request.getAttribute("totalCount");
										Integer totalPage = totalCount / 10;
										//System.out.println("currentPage: " + currentPage + " == totalCount: " + totalCount);
										if (currentPage == 1) {
									%>
									<li class="disabled"><span>上一页</span></li>
									<%
										} else {
									%>
									<li><a
										href="${path}/imagetextinfo/list?page=<%=currentPage-1%>">上一页</a></li>
									<%
										}
										for (int i = 1; i < totalPage + 1; i++) {
											if (i == currentPage) {
									%>
									<li class="active"><span><%=i%></span></li>
									<%
										} else {
									%>
									<li><a
										href="${path}/imagetextinfo/list?page=<%=i%>"><%=i%></a></li>
									<%
										}
										}

										if (currentPage < totalPage) {
									%>
									<li><a
										href="${path}/imagetextinfo/list?page=<%=currentPage + 1%>">下一页</a></li>
									<%
										} else {
									%>
									<li class="disabled"><span>下一页</span></li>
									<%		
										}
									%>
								</ul>
							</div>
							<div class="clr"></div>
						</div>
						
						<div class="page-bd">
							<div class="tj msg-list">
								<!-- 偶数内容 -->
								<div id="first_col" class="b-dib vt msg-col">
									<div id="addAppmsg" class="tc add-access">
										<a href="${path}/imagetextinfo/addUI"
											class="dib vm add-btn">+单图文消息</a> <a
											href="${path}/imagetextmore/addUI"
											class="dib vm add-btn multi-access">+多图文消息</a>
									</div>
									
									<c:forEach items="${list}" var="entry" varStatus="st">
										<c:if test="${st.count%2 == 0}">
											<c:if test="${entry.imageTextType == 2}">
												<div class="msg-item-wrapper">
													<div class="msg-item multi-msg">
														<div class="appmsgItem">
															<h4 class="msg-t">
																<a
																	href="${path}/imagetextinfo/detail?type=1&id=${entry.imageTextNo}"
																	class="i-title" target="_blank">${entry.title}</a>
															</h4>
															<p class="msg-meta">
																<span class="msg-date">${entry.createTime}</span>
															</p>
															<div class="cover">
																<p class="default-tip" style="display: none">封面图片</p>
																<img src="${path}/images/upload/${entry.imageUrl}"
																	class="i-img" style="">
															</div>
															<p class="msg-text"></p>
														</div>
														
														<c:forEach items="${entry.subVoList}" var="entry_">
															<!-- 子图文：封面url,id -->
															<div class="rel sub-msg-item appmsgItem">
																<span class="thumb"> <img
																	src="${path}/images/upload/${entry_.imageUrl}"
																	class="i-img" style="">
																</span>
																<h4 class="msg-t">
																	<a
																		href="${path}/imagetextinfo/detail?type=2&id=${entry_.moreImageTextNo}"
																		target="_blank" class="i-title">${entry_.title}</a>
																</h4>
															</div>
														</c:forEach>
													</div>

													<div class="msg-opr">
														<ul class="f0 msg-opr-list">
															<li class="b-dib opr-item"><a data-mul="true"
																class="block tc opr-btn edit-btn"
																href="javascript:void(0)"
																data-rid="${entry.imageTextNo}"><span
																	class="th vm dib opr-icon edit-icon">编辑</span></a></li>
															<li class="b-dib opr-item"><a
																class="block tc opr-btn del-btn"
																href="javascript:void(0);"
																data-rid="${entry.imageTextNo}"><span
																	class="th vm dib opr-icon del-icon">删除</span></a></li>
														</ul>
													</div>
												</div>
											</c:if>
											
											<c:if test="${entry.imageTextType == 1}">
												<div class="msg-item-wrapper">
														<div class="msg-item">
															<!-- 详细地址id，创建时间，封面url,删除编辑的data-rid -->
															<h4 class="msg-t">
																<a
																	href="${path}/imagetextinfo/detail?type=1&id=${entry.imageTextNo}"
																	class="i-title" target="_blank">${entry.title}</a>
															</h4>
															<p class="msg-meta">
																<span class="msg-date">${entry.createTime}</span>
															</p>
															<div class="cover">
																<p class="default-tip" style="display: none">封面图片</p>
																<c:choose>
																	<c:when test="${fn:contains(entry.imageUrl, 'http')}">
																		<img class="i-img" src="${entry.imageUrl}" style="">
																	</c:when>
																	<c:otherwise>
																		<img class="i-img" src="${path}/images/upload/${entry.imageUrl}" style="">
																	</c:otherwise>
																</c:choose>
															</div>
															<p class="msg-text">
																${entry.digest}
															</p>
														</div>
	
														<div class="msg-opr">
															<ul class="f0 msg-opr-list">
																<li class="b-dib opr-item"><a data-mul="false"
																	class="block tc opr-btn edit-btn"
																	href="javascript:void(0)"
																	data-rid="${entry.imageTextNo}"><span
																		class="th vm dib opr-icon edit-icon">编辑</span></a></li>
																<li class="b-dib opr-item"><a
																	class="block tc opr-btn del-btn"
																	href="javascript:void(0);"
																	data-rid="${entry.imageTextNo}"><span
																		class="th vm dib opr-icon del-icon">删除</span></a></li>
															</ul>
														</div>
													</div>
											</c:if>
										</c:if>
									</c:forEach>
								</div>

								<!-- 奇数内容 -->
								<div id="second_col" class="b-dib vt msg-col">
									<c:forEach items="${list}" var="entry" varStatus="st">
										<c:if test="${st.count%2 != 0}">
											<c:if test="${entry.imageTextType == 2}">
												<div class="msg-item-wrapper">
													<div class="msg-item multi-msg">
														<div class="appmsgItem">
															<h4 class="msg-t">
																<a
																	href="${path}/imagetextinfo/detail?type=1&id=${entry.imageTextNo}"
																	class="i-title" target="_blank">${entry.title}</a>
															</h4>
															<p class="msg-meta">
																<span class="msg-date">${entry.createTime}</span>
															</p>
															<div class="cover">
																<p class="default-tip" style="display: none">封面图片</p>
																<img src="${path}/images/upload/${entry.imageUrl}"
																	class="i-img" style="">
															</div>
															<p class="msg-text"></p>
														</div>
		
														<c:forEach items="${entry.subVoList}" var="entry_">
															<!-- 子图文：封面url,id -->
															<div class="rel sub-msg-item appmsgItem">
																<span class="thumb">
																	<img src="${path}/images/upload/${entry_.imageUrl}"
																	class="i-img" style="">
																</span>
																<h4 class="msg-t">
																	<a
																		href="${path}/imagetextinfo/detail?type=2&id=${entry_.moreImageTextNo}"
																		target="_blank" class="i-title">${entry_.title}</a>
																</h4>
															</div>
														</c:forEach>
													</div>

													<div class="msg-opr">
														<ul class="f0 msg-opr-list">
															<li class="b-dib opr-item">
																<a data-mul="true"
																	class="block tc opr-btn edit-btn"
																	href="javascript:void(0)" data-rid="${entry.imageTextNo}"><span
																		class="th vm dib opr-icon edit-icon">编辑</span></a></li>
															<li class="b-dib opr-item">
																<a class="block tc opr-btn del-btn"
																	href="javascript:void(0);" data-rid="${entry.imageTextNo}"><span
																		class="th vm dib opr-icon del-icon">删除</span></a></li>
														</ul>
													</div>
												</div>
											</c:if>
											
											<c:if test="${entry.imageTextType == 1}">
												<div class="msg-item-wrapper">
													<div class="msg-item">
														<!-- 详细地址id，创建时间，封面url,删除编辑的data-rid -->
														<h4 class="msg-t">
															<a
																href="${path}/imagetextinfo/detail?type=1&id=${entry.imageTextNo}"
																class="i-title" target="_blank">${entry.title}</a>
														</h4>
														<p class="msg-meta">
															<span class="msg-date">${entry.createTime}</span>
														</p>
														<div class="cover">
															<p class="default-tip" style="display: none">封面图片</p>
															<c:choose>
																<c:when test="${fn:contains(entry.imageUrl, 'http')}">
																	<img class="i-img" src="${entry.imageUrl}" style="">
																</c:when>
																<c:otherwise>
																	<img class="i-img" src="${path}/images/upload/${entry.imageUrl}" style="">
																</c:otherwise>
															</c:choose>
														</div>
														<p class="msg-text">${entry.digest}</p>
													</div>

													<div class="msg-opr">
														<ul class="f0 msg-opr-list">
															<li class="b-dib opr-item"><a data-mul="false"
																class="block tc opr-btn edit-btn"
																href="javascript:void(0)" data-rid="${entry.imageTextNo}"><span
																	class="th vm dib opr-icon edit-icon">编辑</span></a></li>
															<li class="b-dib opr-item"><a
																class="block tc opr-btn del-btn"
																href="javascript:void(0);" data-rid="${entry.imageTextNo}"><span
																	class="th vm dib opr-icon del-icon">删除</span></a></li>
														</ul>
													</div>
												</div>
											</c:if>
										</c:if>
										
									</c:forEach>

								</div>
								<!-- 奇数内容结束 -->
							</div>
						</div>
						<!-- page_bg 结束 -->

					</div>
					<!-- content 结束 -->
					<!--  	
			<div class="sideBar">
			   	<div class="menu">
			   		<ul class="nav nav-list">
					  <li class="active"><a href="/admin/content/article?action=list" target="main">图文消息(8)</a></li>
					  <li><a href="/admin/content/voice?action=list" target="main">语音(120)</a></li>
					  <li><a href="account/pay.jsp" target="main">视频(0)</a></li>
					</ul>
			   	</div>
			</div>
-->
					<div class="clr"></div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
