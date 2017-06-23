<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base target="_self">
<link rel="stylesheet" href="${path}/css/bootstrap.min.css">
<link rel="stylesheet" href="${path}/css/appmsg.css">

<script type="text/javascript" src="${path}/js/jquery-1.7.2.min.js"></script>

<style>
body {
	padding-bottom: 20px;
}

.pagination {
	margin: 0 70px;
	float: right;
}

#first_col {
	display: inline-block;
	zoom: 1; *
	display: inline;
}

#second_col {
	margin-left: 15px;
	display: inline-block;
	zoom: 1; *
	display: inline;
}

.add-btn {
	height: 90px;
	margin: 0 18px;
	color: #b5b5b5;
	background: transparent url('${path}/images/wx/appmsg-icon.png')
		no-repeat 50% -242px;
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

.msg-selected-tip {
	background: url("${path}/images/wx/selected-icon.png") no-repeat scroll
		50% 50% rgba(0, 0, 0, 0);
	height: 100%;
	position: absolute;
	width: 100%;
}

.sltpic {
	background-color: #82FF8C;
}
</style>
<title>选择图文消息</title>
<script>
	//点击选中图文
	function showImg(idx, rid, f) {
		var html = $('#' + f + rid).html(), picid = $("#picid").val(), id = f
				+ idx;
		$("#picid").val(id);
		if (picid != id) {
			$('#' + picid).removeClass("sltpic");
		}
		$('#' + id).addClass("sltpic");
		$("#html").val(html);
		$("#rid").val(rid);
		//alert('请点击图文后再点击【确定】按钮!');
		//alert('id = '+id+'<br> html = '+html);
	}
	//确定选中图文
	function slt() {
		/*var array = new Array();
		array[0] = $("#html").val();
		array[1] = $("#rid").val();
		window.returnValue = array;*/
		//弹窗改为window.open()解决谷歌浏览器不支持弹窗的特性（原做法是windin.showModalDialog()）
		this.opener.document.getElementById("preViewDiv").innerHTML = $("#html").val();
		this.opener.document.getElementById("picandtext").value = $("#rid").val();
		this.window.close();
	}
</script>
</head>

<body>
	<input type="hidden" id="html" />
	<input type="hidden" id="rid" />
	<div class="container">
		<div class="containerBox">
			<div class="boxHeader">
				<h4>选择图文消息</h4>
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
							<input type="hidden" id="picid">
							<c:forEach items="${list}" var="entry" varStatus="st">
								<c:if test="${st.count%2 == 0}">
									<c:if test="${entry.imageTextType == 2}">
										<div class="msg-item-wrapper" id="evmore${entry.imageTextNo}"
											onclick="showImg('${st.index}','${entry.imageTextNo}','evmore')">
											<div class="msg-item multi-msg" id="evmore${st.index}">
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
														<img
															src="${path}/images/upload/${entry.imageUrl}" class="i-img" style="">
													</div>
													<p class="msg-text"></p>
												</div>
												<!-- 子图文：封面url,id -->
												<c:forEach items="${entry.subVoList}" var="entry">
													<div class="rel sub-msg-item appmsgItem">
														<span class="thumb">
															<img src="${path}/images/upload/${entry.imageUrl}" class="i-img" style="">
														</span>
														<h4 class="msg-t">
															<a
																href="${path}/imagetextinfo/detail?type=2&id=${entry.moreImageTextNo}"
																target="_blank" class="i-title">${entry.title}</a>
														</h4>
													</div>	
												</c:forEach>
											</div>
										</div>
									</c:if>
									<c:if test="${entry.imageTextType == 1}">
										<!-- 详细地址id，创建时间，封面url,删除编辑的data-rid -->
										<div class="msg-item-wrapper" id="evone${entry.imageTextNo}"
											onclick="showImg('${st.index}','${entry.imageTextNo}','evone')">
											<div class="msg-item" id="evone${st.index}">
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
										<div class="msg-item-wrapper" id="odmore${entry.imageTextNo}"
											onclick="showImg('${st.index}','${entry.imageTextNo}','odmore')">
											<div class="msg-item multi-msg" id="odmore${st.index}">
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
														<img src="${path}/images/upload/${entry.imageUrl}" class="i-img" style="">
													</div>
													<p class="msg-text"></p>
												</div>

												<!-- 子图文：封面url,id -->
												<c:set var="imageTextNo" value="0" />
												<c:forEach items="${entry.subVoList}" var="entry">
													<c:set var="imageTextNo" value="${entry.imageTextNo}" />
													<div class="rel sub-msg-item appmsgItem">
														<span class="thumb">
															<img src="${path}/images/upload/${entry.imageUrl}" class="i-img" style="">
														</span>
														<h4 class="msg-t">
															<a
																href="${path}/imagetextinfo/detail?type=2&id=${entry.moreImageTextNo}"
																target="_blank" class="i-title">${entry.title}</a>
														</h4>
													</div>
												</c:forEach>
											</div>
										</div>
									</c:if>
									<c:if test="${entry.imageTextType == 1}">
										<div class="msg-item-wrapper" id="odone${entry.imageTextNo}"
											onclick="showImg('${st.index}','${entry.imageTextNo}','odone')">
											<div class="msg-item" id="odone${st.index}">
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

	<div style="position: fixed; top: 90%; left: 80%;">
		<input type="button" class="btn btn-primary" value="确定"
			onclick="slt();" /> <input type="button" class="btn" value="取消"
			onclick="javascript:window.close();" />
	</div>
</body>
</html>