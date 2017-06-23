<%@ page import="com.fzh.wx.intf.BaiduMap"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
		Map<String, String> map = BaiduMap.getScenic(request.getParameter("id"));
%>
<!DOCTYPE html>
<html>
<head>
<title>旅游景点 - <%=map.get("name") %></title>
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
</head>

<body>
	<div data-role="page" >
		<div data-role="header" class="bg" data-position="fixed" data-tap-toggle="false">
			<h1><%=map.get("name") %></h1>
		</div>
		<div data-role="content" data-content-theme="d">
			<font size="4px" style="line-height: 20pt;">
				景点印象：<br>
					<%=map.get("abstract")==null||"".equals(map.get("abstract"))?"无":map.get("abstract")%><br><br>
				景点介绍：<br>
					<%=map.get("description") %><br><br>
				开放时间：<%=map.get("open_time")==null||"".equals(map.get("open_time"))?"无":map.get("open_time") %><br><br>
				联系电话：<%=map.get("telephone")==null||"".equals(map.get("telephone"))?"无":map.get("telephone") %><br><br>
				旅游攻略：<a href="<%=map.get("url") %>"><%=map.get("name") %></a><br><br>
				门票详情：<%=map.get("ticket_info")==null||"".equals(map.get("ticket_info"))?"无":map.get("ticket_info") %><br><br>
			</font>
		</div>
		
<!-- <div class="bdsharebuttonbox"><a href="#" class="bds_more" data-cmd="more"></a><a title="分享到QQ空间" href="#" class="bds_qzone" data-cmd="qzone"></a><a title="分享到新浪微博" href="#" class="bds_tsina" data-cmd="tsina"></a><a title="分享到腾讯微博" href="#" class="bds_tqq" data-cmd="tqq"></a><a title="分享到微信" href="#" class="bds_weixin" data-cmd="weixin"></a></div>
<script>window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"1","bdSize":"16"},"share":{},"image":{"viewList":["qzone","tsina","tqq","renren","weixin"],"viewText":"分享到：","viewSize":"16"},"selectShare":{"bdContainerClass":null,"bdSelectMiniList":["qzone","tsina","tqq","renren","weixin"]}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];</script>
 -->		
		<a href="#"  class="bg" data-role="button"  data-icon="back" data-rel="back"><font color="white">返&nbsp;&nbsp;&nbsp;&nbsp;回</font></a><p>
		<div align="center">
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