<%@ page import="com.fzh.wx.util.DBManager"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String cityid = request.getParameter("cityid");
   String typeid = request.getParameter("typeid");
   String data = DBManager.getItineraries(cityid, typeid);
%>
<!DOCTYPE html>
<html>
<head>
<title>景点导航</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
     
<link rel="stylesheet" href="http://code.jquery.com/mobile/1.1.0-rc.2/jquery.mobile-1.1.0-rc.2.min.css" />
<script src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
<script src="http://code.jquery.com/mobile/1.1.0-rc.2/jquery.mobile-1.1.0-rc.2.min.js"></script>

<style type="text/css">
   .bg{
      background:#19B919; text-shadow:none; border-color:#19B919
   }
</style>
<script type="text/javascript">
	$(function(){
		showData(<%=data%>);
	});
	
	//展示数据
	function showData(data){
		var li = "";
		var description = data[0].description;
		var info = data[0].info.split("&");
		for(var i=0; i<info.length; i++){
			var str = info[i].split(",");
			if(str != ""){
				var id = str[1];
				if(li.indexOf(id) < 0){
					li += '<li><a href="http://fzh2014.duapp.com/fzh/scenicinfo.jsp?'+id+'">' + str[0] + '</a> </li>';
				}
			}
		}
		$("#listview").append(li);
		$("#listview").listview("refresh");
		if(description !==null && description !="" && description !="null"){
			$("#cont").html("导游：<br/>" + description);
		}
	}
	
	//AJAX请求
	function ajax(id){
		$.ajax({
            url: "http://fzh2014.duapp.com/fzh/travel?jsoncallback=?",
            type: "GET",
            data: {
				id: id
			},
            dataType: "jsonp",
            success: function(data) {
				showInfo(data);
            },
            error: function(a, b, c) {
				alert('请再试一次！');
                /*alert("error.a==" + a);
				alert("error.b==" + b);
				alert("error.c==" + c);
				*/
            }
        });
	}
	
	//定位
	function clkCos(e, id){
		//ajax(id);
	    var obj = e.offsetTop;
		$(document).scrollTop(obj-50);
	}
	
</script>
</head>

<body>
	<div data-role="page" >
		<div data-role="header" class="bg" data-position="fixed" data-tap-toggle="false">
			<h1>景点导航</h1>
		</div>
		
		<div data-role="content" >
			<div id="cont" style="color: #449934; line-height: 20pt;"></div>
			<br><br>
			<ol data-role="listview" id="listview" data-inset="true"></ol>
		</div>
		
		<div align="center">
		<!-- 
				<a href="http://mp.weixin.qq.com/s?__biz=MjM5MjM3OTY1Mw==&mid=200137399&idx=1&sn=df847d7c79a06d2b27628778d008ab8b&scene=1#rd">关注fzhwx开发者，获取更多服务</a>
		 -->
				<p>更多服务，微信搜索<strong><a href="weixin://contacts/profile/f_zhihui">fzhwx开发者（f_zhihui）</a></strong>加关注
				<p><img alt="fzhwx开发者"  style="width: 100%" src="http://fzh2014.duapp.com/uploadimages/wx.jpg">
				<p><img alt="fzhwx开发者"  style="width: 100%" src="http://fzh2014.duapp.com/uploadimages/share.jpg">
		</div>
		<div data-role="footer" class="bg" data-position="fixed" data-tap-toggle="false">
			<h1>Copyright&nbsp;©&nbsp;fzh2014</h1>
		</div>
	</div>
</body>
</html>