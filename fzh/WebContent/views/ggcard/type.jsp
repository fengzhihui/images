<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	session.setAttribute("redirectURL", basePath+"wx/ggcrad_doResult.do?id="+request.getParameter("id")+"&openId="+request.getParameter("openId"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=yes">
<link rel="stylesheet" href="<%=basePath%>bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=basePath%>wx/ggcard/css/style.css">
<style type="text/css">
.fontsize13{font-size: 13px;list-style: decimal;margin-top: 5px;margin-left: 15px;}
</style>
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>wx/ggcard/js/wScratchPad.js"></script>
<script type="text/javascript" src="<%=basePath%>lhgdialog/lhgdialog.min.js?skin=idialog"></script>
<script type="text/javascript">
	window.allBasePath = "<%=basePath %>";
	window.id="<%=request.getParameter("id")%>";
	window.openId="<%=request.getParameter("openId")%>";
</script>
<title>大转盘</title>
</head>
<body class="activity-scratch-card-winning" screen_capture_injected="true"><div style="left: 0px; top: 0px; visibility: hidden; position: absolute;" class=""><table class="ui_border"><tbody><tr><td class="ui_lt"></td><td class="ui_t"></td><td class="ui_rt"></td></tr><tr><td class="ui_l"></td><td class="ui_c"><div class="ui_inner"><table class="ui_dialog"><tbody><tr><td colspan="2"><div class="ui_title_bar"><div class="ui_title" unselectable="on" style="cursor: move;"></div><div class="ui_title_buttons"><a class="ui_min" href="javascript:void(0);" title="最小化" style="display: inline-block;"><b class="ui_min_b"></b></a><a class="ui_max" href="javascript:void(0);" title="最大化" style="display: inline-block;"><b class="ui_max_b"></b></a><a class="ui_res" href="javascript:void(0);" title="还原"><b class="ui_res_b"></b><b class="ui_res_t"></b></a><a class="ui_close" href="javascript:void(0);" title="关闭(esc键)" style="display: inline-block;">×</a></div></div></td></tr><tr><td class="ui_icon" style="display: none;"></td><td class="ui_main" style="width: auto; height: auto;"><div class="ui_content" style="padding: 10px;"></div></td></tr><tr><td colspan="2"><div class="ui_buttons" style="display: none;"></div></td></tr></tbody></table></div></td><td class="ui_r"></td></tr><tr><td class="ui_lb"></td><td class="ui_b"></td><td class="ui_rb" style="cursor: se-resize;"></td></tr></tbody></table></div>
	<div class="main">
		<div id="outercont">
			<div class="cover">
				<div class="bg">
					<img src="<%=basePath%>wx/ggcard/images/bannerbg.png">
				</div>
				<img class="gjq" src="<%=basePath%>wx/ggcard/images/gjq.png">
				<div id="prize">&nbsp;</div>
				<div id="scratchpad">
					<div style="position: relative; width: 120px; height: 40px; cursor: default;">
						<canvas width="120" height="40" style="cursor: default;"></canvas>
					</div>
				</div>
			</div>
		</div>
		<div class="content">
			<div class="boxcontent boxyellow" id="result" style="display:none">
				<div class="box">
					<div class="title-orange"><span>恭喜你中奖了</span></div>
					<div class="Detail">
						<p>你中了：<span class="red" id="prizetype">一等奖</span></p>
						<p>你的兑奖SN码：<span class="red" id="sncode"></span></p>
						<p class="red">本次兑奖码已经关联你的微信号，请确认你的手机号码作为领奖的凭证!</p>
						               
						<p>
						<input id="rid" type="text" value="" style="display: none;">
						<input name="" class="px" id="tel" type="text" maxlength="11" placeholder="输入您的手机号码">
						</p>
						<p>
						<input class="pxbtn" id="save-btn" name="提 交" type="button" value="提 交">
						</p>
					</div>
				</div>
			</div>
			<div class="boxcontent boxwhite">
				<div class="box">
					<div class="title-green"><span>奖项设置：</span></div>
						<div class="Detail  jp-detail"></div>
				</div>
			</div>
			<div class="boxcontent boxwhite">
				<div class="box">
					<div class="title-green">活动说明：</div>
						<div class="Detail hd-content"></div>
				</div>
			</div>
			<div class="boxcontent boxwhite">
			<div class="box">
				<div class="title-green">温馨提示：</div>
					<div class="Detail">
						想快点获得优惠券吗？快完成下面步骤吧!
						<ul id="wxtips">
						<li class="fontsize13" id="wxtips1">成为我们的会员，
							<a id="regeditA" href="#regeditAF">立即注册</a>
							<div id="regeditAF" style="display: none;">
							    <form id="regform" class="form-horizontal">
							    	<h3 style="font-size: 16px;font-weight: 700;">成为我们的会员</h3>
							    	<div style="margin: 5px;">
										<label class="control-label" style="width:60px;display: inline;">用&nbsp;&nbsp;户&nbsp;&nbsp;名:</label>
							            <input type="text" name="username" id="username" value="" maxlength="10" placeholder="用户名" style="width:120px;margin-left: 10px;display: inline;">
									</div>
							    	<div style="margin: 5px;">
										<label class="control-label" style="width:60px;display: inline;">手机号码:</label>
								        <input type="text" name="mobile" id="mobile" value="" maxlength="11" placeholder="手机号码" style="width:120px;margin-left: 10px;display: inline;">
									</div>
						            <div class="btns" style="text-align: center;">
							            <button type="button" class="btn btn-info" id="regbtn">注册</button>
							            <button type="button" class="btn" onclick="hidediv('regeditAF');">取消</button>
						            </div>
						   		</form>
							</div>
							<font id="isviptips" style="color:green;">√</font>
						</li>
						<li class="fontsize13" id="wxtips2">完成该调研活动，
							<a id="researchB" href="#researchBF"></a>
							<a id="resbtn" href="javascript:void(0);" researchfk="">进入调研</a>
							<div id="researchBF" style="display: none;">
						        <div style="padding:10px 20px;">
						            <h3>参加该调研活动才能领券哟？</h3>
						            <div class="btns" style="text-align: center;">
							             <button type="button" class="btn btn-info" id="resbtn">参加</button>
							            <button type="button" class="btn" onclick="hidediv('researchBF');">取消</button>
							        </div>
						        </div>
							</div>
							<font id="iscantips" style="color:green;">√</font>							
						</li>
					</ul>
				</div>
			</div>
		</div>
		</div>
	</div>
	<script type="text/javascript" src="<%=basePath%>wx/ggcard/js/tt.js"></script>
<s:iterator value="dp.datas" id="id" status="c">
	<s:if test="#c.first">
		<script type="text/javascript">
			if(1>0){
				document.title="${title}";
				$(".hd-content").append('<p>${content}</p>');
				$("#scratchpad").attr({"isvip":"${isvip}","iscan":"${iscan}","starttime":"${starttime}","endtime":"${endtime}"});
				if("${isvip}"=="no"){
					$("#isviptips").text("╳");
					$("#isviptips").css("color","red");
				}
				if("${researchfk}"==0){
					$("#wxtips2").css("display","none");
				}else{
					if("${iscan}"=="no"){
						$("#iscantips").text("╳");
						$("#iscantips").css("color","red");
					}
				}
				if(parseInt("${starttime}")>0){
					$("#wxtips").append('<li class="fontsize13">请耐心等待活动开始</li>');
				}
			}
		</script>
	</s:if>
	<script type="text/javascript">
		if(1>0){
			var isshownum='${isshownum }'
			$(".jp-detail").append('<p>${rname }：${rcontent}。'+(isshownum=="1"?'奖品数量：${rnum}':"")+'</p>');
		}
	</script>
</s:iterator>
</body>
</html>