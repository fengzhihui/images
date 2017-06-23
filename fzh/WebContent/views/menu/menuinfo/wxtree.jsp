<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ include file="/common/inc.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>fzhwx开发者-公众号管理平台</title>
<link rel="shortcut icon" href="${path}/images/wx/wally.jpg"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="stylesheet" href="${path}/css/wxmenu.css" type="text/css">
<link rel="stylesheet" href="${path}/css/appmsg.css">
<link rel="stylesheet" href="${path}/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${path}/js/wxtree.js"></script>
<script type="text/javascript" src="${path}/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${path}/js/jquery.ztree.exedit-3.5.js"></script>

<script type="text/javascript">
var path = "${path}";
var array = new Array();
//页面初始加载树节点
$(document).ready(function(){
	//菜单节点数组
	var zNodes = ${treedata};
	var i = 0;
	for(i==0 && zNodes !=null;i<zNodes.length;i++){
		if(zNodes[i]!=null)array.push(zNodes[i]);
	}
	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	$("#selectAll").bind("click", selectAll);
	
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	for(var j=0;nodes.length ==1 && j<nodes.length;j++){
		addHoverDom(null, nodes[j]);
	}
	
	$("#dbc").val(array.length);
	$("#cur").val(array.length);
	
	var ok = "${ok}";
	if(ok !=null && ok !="") alert(ok);
});
</script>

</head>

<body leftmargin="0" topmargin="0" onload="showmsg();">
	<jsp:include page="/common/header.jsp" flush="true" />
	<jsp:include page="/common/leftbar.jsp" flush="true" />
	
	<div class="content">
		<div class="container-fluid">
			<fieldset>
				<legend>自定义菜单管理</legend>
			</fieldset>
		<form name="menuform" method="post">
			<input type="hidden" id="treedata" name="treedata" value='${treedata}'>
			<input id="tid" type="hidden">
			<input id="trid" type="hidden">
			<input id="duiying" type="hidden" name="str">
			<input id="picandtext" type="hidden" value="">
			<br>
			<input type="hidden" id="dbc">
			<input type="hidden" id="cur">
			<input type="hidden" id="rid">
			<input type="hidden" id="rs" value="${rs}">
			<input type="hidden" id="jsondata" name="jsondata">
		</form>

		<!-- 菜单区 -->
		<div class="msg-preview" style="float: left">
			<div class="msg-item" style="width:220px;height:400px; border: #c8c8cb 1px solid; padding-top: 1px; 
				padding-left: 1%; margin-top: -20px; margin-left: 20px;">
				<ul id="treeDemo" class="ztree"></ul>
			</div>
			<div style="margin: 10px 0 0 35px;">
				<button type="button" class="btn-mine" onclick="saveMenu();">保&nbsp;存</button>
				<button type="button" class="btn"  onclick="previewMenu();">预&nbsp;览</button>
				<button type="button" class="btn" onclick="createMenu();">发&nbsp;布</button>
			</div>
		</div>

		<!--编辑区-->
		<div id="rndiv" style="margin: -10px 0 0 320px; width: 700px; display: none;">
				<div class="msg-editer-wrapper">
					<div class="msg-editer">
						<div style="line-height: 50px">
							菜单名称：<input id="mname" type="text" style="margin-left: 4px; border-radius: 5px; height: 30px;">
							<span class="maroon">*</span><span class="help-inline">(必填，不能超过10个字)</span>
						</div>
						
						<div style="line-height: 40px">
							菜单类型：
							<select name="type"
								id="type" style="width: 153px" onchange="showdiv(this)">
								<option value="1">文本</option>
								<option value="2">图文</option>
								<option value="3">外链</option>
							</select>
						</div>

						<p>
						<div id="cont" class="control-group">
							文本内容：
							<p><p/>
							<textarea style="width: 450px; margin-left: 74px; resize: none;" rows="5" id="content"></textarea>
						</div>

						<div id="pic" style="display: none; line-height: 60px">
							图文预览：
							<input type="button" class="btn" value="选择图文" onclick="sltImg();">
							<input type="hidden" name="imghtml" id="imghtml">
							<div id="preViewDiv" style="display: none; border: none"></div>
						</div>
						
						<div id="lnk" style="display: none; line-height: 50px">
							页面URL：
							<input id="url" type="text" style="width: 400px; border-radius: 5px; height: 30px;">
							<span class="maroon">*</span><span class="help-inline">(必须以http://开头)</span>
						</div>
						
						<p>
						<button style="margin-top: 10px;" class="btn-mine" type="button"  onclick="saveNode();">保存</button>
						
					</div>
					<span class="abs msg-arrow a-out" style="margin-top: 0px;"></span>
					<span class="abs msg-arrow a-in" style="margin-top: 0px;"></span>
				</div>
			</div>
		
		<!--自定义菜单预览-->
		<div id="preview_box" class="dialogBox" style="display: none;">
			<div class="background"></div>
			<div class="pre_wrapper">
				<div class="pre_hd">
					<h4 class="pre_nav_name">${operator.wxpName}</h4>
				</div>
				<div class="pre_bd" id="previewAction"></div>
				<div class="pre_ft">
					<div id="pre_nav_wrapper" class="pre_nav_wrapper group screen1"></div>
				</div>
				<span class="pre_windows_opr">
					<i id="pre_close" class="opr_icon closed" onclick="clo();"></i>
				</span>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
