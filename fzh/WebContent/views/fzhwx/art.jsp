<%@ page import="com.fzh.wx.intf.ReadNews"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String ids = request.getParameter("ids");
	String type = request.getParameter("type");
	String[] idsarr = ids.split(",");
	String curtMaxId = "";
	for (int j = 0; j < idsarr.length; j++) {
		if (j == 0 && idsarr[j] != null && idsarr[j] != "") {
			curtMaxId = idsarr[j];
			break;
		}
	}
	LinkedHashMap<String, String> map = ReadNews.getContent(ids);
%>
<!DOCTYPE html>
<html>
<head>
<title>fzhwx开发者-新闻阅读</title>
<meta name="viewport" content="width=device-width,initial-scale=1" />
<link rel="stylesheet"
	href="http://code.jquery.com/mobile/1.1.0-rc.2/jquery.mobile-1.1.0-rc.2.min.css" />
<script src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
<script
	src="http://code.jquery.com/mobile/1.1.0-rc.2/jquery.mobile-1.1.0-rc.2.min.js"></script>

<script type="text/javascript">
					function clkCos(id) {
						//var obj = $('#d' + id).position();
						var ht = parseInt(id.offsetTop);
						$(document).scrollTop(ht - 50);
					}
					
					function slt(id) {
						for ( var i = 1; i <= 3; i++) {
							if (i == id)
								$("#cos" + id).show();
							else
								$("#cos" + i).hide();
						}
					}
					/*
						<div data-role="navbar">
					    <ul>
					      <li><a href="javascript:slt('1');" data-icon="info">娱乐</a></li>
					      <li><a href="javascript:slt('2');" data-icon="alert">国际</a></li>
					      <li><a href="javascript:slt('3');" data-icon="gear">股票</a></li>
					    </ul>
					  </div>
					 */
					 
					var count = 0;
					function morearticles() {
						count++;
						if (count > 4) {
							alert('没有更多了!');
							return;
						}
						var curtMaxId = $('#curtMaxId').val();
						if(curtMaxId == ""){curtMaxId = <%=curtMaxId%>;$('#curtMaxId').val(curtMaxId);}
						$.ajax({
									//url: "http://fzh2014.duapp.com/fzh/article?jsoncallback=?",
									url: "http://localhost:8080/fzh/article?jsoncallback=?",
									type : "GET",
									data : {
										curtMaxId : curtMaxId,
										type: <%=type%>
									},
									dataType : "jsonp",
									success : function(data) {
										for (var i = 0; i < data.length; i++) {
											var num = $('#num').val();
											if(num == ""){
												$('#num').val(20);//文章序号
												num = 20;
											}
											num = parseInt(num)+i+1;
											
											var colls = '<div data-role=\"collapsible\" style=\"color: #449934;\" data-content-theme=\"d\" onclick=\"clkCos(this)\">' 
															+ '<h3>'+ num + '. ' + data[i].title+'</h3>'
															+ '<font size=\"4px\" style=\"line-height: 20pt;\">'+data[i].content+'</font></div>';
											$("#c"+count).append(colls);
											if(i==data.length-1){
												$('#curtMaxId').val(curtMaxId-i);
												$('#num').val(num);
											}
										}

										$("#c" + count).collapsibleset("refresh");
									},
									error : function(a, b, c) {
										//alert("error==" + b);
										alert('没有更多了!');
									}
								});
					}
				</script>

</head>
<body>
	<div data-role="page">
		<div data-role="header" data-theme="b" data-position="fixed"
			data-tap-toggle="false">
			<h1>新闻列表</h1>
		</div>
		<input type="hidden" id="num"> <input type="hidden"
			id="curtMaxId">
		<div data-role="content">
			<div data-role="collapsible-set" id="cos0">
				<%
       	int i = 1;
       	for (String s : map.keySet()) {
       %>
				<div data-role="collapsible" data-content-theme="d" id="d<%=i%>"
					onclick="clkCos(this);">
					<h3><%=i%>.<%=s%></h3>
					<font size="4px" style="line-height: 20pt;"><%=map.get(s)%></font>
				</div>
				<%
        	i++;
        	}
        %>
			</div>
			<div data-role="collapsible-set" id="c1"></div>
			<div data-role="collapsible-set" id="c2"></div>
			<div data-role="collapsible-set" id="c3"></div>
			<div data-role="collapsible-set" id="c4"></div>
			<a href="javascript:morearticles();" data-role="button"
				data-icon="info">加载更多</a>
		</div>
		<div data-role="footer" data-theme="b" data-position="fixed"
			data-tap-toggle="false">
			<h4>Copyright © 2013 fzhwx开发者</h4>
		</div>
	</div>
</body>
</html>
