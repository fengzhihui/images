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
<script type="text/javascript">var path = "${path}";</script>
<link rel="stylesheet" type="text/css" href="${path}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${path}/css/theme.css">
<link rel="stylesheet" href="${path}/lib/font-awesome/css/font-awesome.css">
<script type="text/javascript" src="${path}/lib/jquery-1.7.2.min.js" ></script>
<script type="text/javascript" src="${path}/js/bootstrap.js" ></script>
<link rel="stylesheet" href="${path}/css/appmsg.css">
<link rel="stylesheet" href="${path}/css/emotion.css">
<script type="text/javascript" src="${path}/js/emotion.js"></script>
<script type="text/javascript" src="${path}/js/html-helper.js"></script>
<script type="text/javascript" src="${path}/js/omvalidate.min.js"></script>

<script type="text/javascript">
var basePath = "${path}/upload/images/";
$(document).ready(function(){
	// 处理select的事件
	$("form").delegate("#answertype", "change", function() {
		var type = $("#answertype").val();
		if (type == 2) {
			$("div#textContainer").hide();
			$("div#preViewDiv").show();
			$("div#picContainer").show();
		} else {
			$("div#picContainer").hide();
			$("div#preViewDiv").hide();
			$("div#textContainer").show();
		}
	});

	//alert("运行了");
	// 点击取消则恢复
	 $(".form-actions button:last").click(function() {
		 //$("form.form-horizontal").hide();
		 //$("div#questionsContainer").show();
		 location.href = path + "/keyset/list";
		 return false;
	 });
	
	 $("#back").click(function() {
		 location.href = path + "/keyset/list";
		 return false;
	 });

	 // 处理添加/修改提交
	 $("#anform").validate({
		 rules : {
			keyword : {required:true},
			description : {required:true},
			picandtext : {required:true}
	 	 }, 
		 messages : {
			 keyword : {
				required : "关键字不能为空"
			},
			description : {
				required : "回复不能为空"
			}, 
			picandtext : {
				required: "请选择一条图文素材，如果没有素材请先添加素材"
			}
		 },
		 ignore : ":hidden",
		 showErrors: function(errorMap, errorList) {
				if (errorList && errorList.length > 0) {
					$.each(errorList, function(index, obj) {
						var item = $(obj.element);
						// 给输入框添加出错样式
						item.closest(".control-group").addClass('error');
						item.attr("title", obj.message);
					});
				} else {
					var item = $(this.currentElements);
					item.closest(".control-group").removeClass('error');
					item.removeAttr("title");
				}
		 },
		 submitHandler : function() {
			 var $form = $("#anform");
			 $("#submitbtn").attr("disabled", true);
			 var submitData = {};
			 submitData.keyWord = $("input[name='keyword']").val();
			 submitData.reType = $("select[name='answertype']").val();
			 if ($("select[name='answertype']").val() == 2) {
				 submitData.refImageTextId = $("#picandtext").val();//选择图文后赋值
			 } else {
				 submitData.refText = $("textarea[name='description']", $form).val();
			 }
			 //alert("提交参数：" + submitData.refText);
			 $.post('${path}/keyset/saveKeyset', submitData, function(data) {
					$("#submitbtn").removeAttr("disabled");
					if (data == true) {
						alert("操作成功");
						location.href = "${path}/keyset/list";
					} else {
						alert("操作失败");
					}
				}, "json");
			 	return false;
		 }
	 });
	 $("#answertype").trigger("change");
});

//选择图文
function sltImg(){
	var url = '${path}/imagetextmore/selectList';
	var attr = 'width=1000px,height=500px,status=no,scroll=yes,top=150px,left=200px';
	window.open(url, window, attr);
	$("#preViewDiv").show();
}
</script>

<!-- Demo page code -->
<style type="text/css">
#file_uploadUploader {
	visibility: hidden
}
.brand {
	font-family: georgia, serif;
}
.brand .first {
	color: #ccc;
	font-style: italic;
}
.brand .second {
	color: #fff;
	font-weight: bold;
}
</style>

</head>

<body class="">
	<jsp:include page="/common/header.jsp" flush="true" />
	<jsp:include page="/common/leftbar.jsp" flush="true" />
	<div class="content">
		<div class="container-fluid">
		   <div class="row">
		<div class="span6">
			<form class="form-horizontal" style="display: block;" id="anform">
				<fieldset>
					<legend>回复添加</legend>
					<div class="control-group">
						<label class="control-label" for="keyword">关键词:</label>
						<div class="controls">
							<input style="border-radius: 5px" id="keyword" type="text" name="keyword"/>
							<span class="maroon">*</span>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="answertype">回复类型:</label>
						<div class="controls">
							<select class="span2" id="answertype" name="answertype">
								<option value="1">文字</option>
								<option value="2">图文</option>
							</select>
						</div>
					</div>
					<div class="control-group" id="textContainer">
						<label class="control-label" for="textarea">回复:</label>
						<div class="controls">
							<div class="txtArea">
								<div class="functionBar">
									<div class="opt">
										<a class="icon18C iconEmotion" href="javascript:;">表情</a>
									</div>
									<div class="tip"></div>
										<div class="emotions">
										<table cellspacing="0" cellpadding="0">
											<tbody>
												<tr>
													<td>
														<div class="eItem" style="background-position: 0px 0;"
															title="微笑"
															data-gifurl="${path}/images/emotion/0.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -24px 0;"
															title="撇嘴"
															data-gifurl="${path}/images/emotion/1.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -48px 0;"
															title="色" data-gifurl="${path}/images/emotion/2.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -72px 0;"
															title="发呆"
															data-gifurl="${path}/images/emotion/3.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -96px 0;"
															title="得意"
															data-gifurl="${path}/images/emotion/4.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -120px 0;"
															title="流泪"
															data-gifurl="${path}/images/emotion/5.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -144px 0;"
															title="害羞"
															data-gifurl="${path}/images/emotion/6.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -168px 0;"
															title="闭嘴"
															data-gifurl="${path}/images/emotion/7.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -192px 0;"
															title="睡" data-gifurl="${path}/images/emotion/8.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -216px 0;"
															title="大哭"
															data-gifurl="${path}/images/emotion/9.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -240px 0;"
															title="尴尬"
															data-gifurl="${path}/images/emotion/10.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -264px 0;"
															title="发怒"
															data-gifurl="${path}/images/emotion/11.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -288px 0;"
															title="调皮"
															data-gifurl="${path}/images/emotion/12.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -312px 0;"
															title="呲牙"
															data-gifurl="${path}/images/emotion/13.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -336px 0;"
															title="惊讶"
															data-gifurl="${path}/images/emotion/14.gif"></div>
													</td>
												</tr>
												<tr>
													<td>
														<div class="eItem" style="background-position: -360px 0;"
															title="难过"
															data-gifurl="${path}/images/emotion/15.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -384px 0;"
															title="酷"
															data-gifurl="${path}/images/emotion/16.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -408px 0;"
															title="冷汗"
															data-gifurl="${path}/images/emotion/17.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -432px 0;"
															title="抓狂"
															data-gifurl="${path}/images/emotion/18.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -456px 0;"
															title="吐"
															data-gifurl="${path}/images/emotion/19.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -480px 0;"
															title="偷笑"
															data-gifurl="${path}/images/emotion/20.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -504px 0;"
															title="可爱"
															data-gifurl="${path}/images/emotion/21.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -528px 0;"
															title="白眼"
															data-gifurl="${path}/images/emotion/22.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -552px 0;"
															title="傲慢"
															data-gifurl="${path}/images/emotion/23.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -576px 0;"
															title="饥饿"
															data-gifurl="${path}/images/emotion/24.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -600px 0;"
															title="困"
															data-gifurl="${path}/images/emotion/25.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -624px 0;"
															title="惊恐"
															data-gifurl="${path}/images/emotion/26.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -648px 0;"
															title="流汗"
															data-gifurl="${path}/images/emotion/27.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -672px 0;"
															title="憨笑"
															data-gifurl="${path}/images/emotion/28.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -696px 0;"
															title="大兵"
															data-gifurl="${path}/images/emotion/29.gif"></div>
													</td>
												</tr>
												<tr>
													<td>
														<div class="eItem" style="background-position: -720px 0;"
															title="奋斗"
															data-gifurl="${path}/images/emotion/30.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -744px 0;"
															title="咒骂"
															data-gifurl="${path}/images/emotion/31.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -768px 0;"
															title="疑问"
															data-gifurl="${path}/images/emotion/32.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -792px 0;"
															title="嘘"
															data-gifurl="${path}/images/emotion/33.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -816px 0;"
															title="晕"
															data-gifurl="${path}/images/emotion/34.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -840px 0;"
															title="折磨"
															data-gifurl="${path}/images/emotion/35.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -864px 0;"
															title="衰"
															data-gifurl="${path}/images/emotion/36.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -888px 0;"
															title="骷髅"
															data-gifurl="${path}/images/emotion/37.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -912px 0;"
															title="敲打"
															data-gifurl="${path}/images/emotion/38.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -936px 0;"
															title="再见"
															data-gifurl="${path}/images/emotion/39.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -960px 0;"
															title="擦汗"
															data-gifurl="${path}/images/emotion/40.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -984px 0;"
															title="抠鼻"
															data-gifurl="${path}/images/emotion/41.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1008px 0;"
															title="鼓掌"
															data-gifurl="${path}/images/emotion/42.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1032px 0;"
															title="糗大了"
															data-gifurl="${path}/images/emotion/43.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1056px 0;"
															title="坏笑"
															data-gifurl="${path}/images/emotion/44.gif"></div>
													</td>
												</tr>
												<tr>
													<td>
														<div class="eItem" style="background-position: -1080px 0;"
															title="左哼哼"
															data-gifurl="${path}/images/emotion/45.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1104px 0;"
															title="右哼哼"
															data-gifurl="${path}/images/emotion/46.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1128px 0;"
															title="哈欠"
															data-gifurl="${path}/images/emotion/47.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1152px 0;"
															title="鄙视"
															data-gifurl="${path}/images/emotion/48.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1176px 0;"
															title="委屈"
															data-gifurl="${path}/images/emotion/49.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1200px 0;"
															title="快哭了"
															data-gifurl="${path}/images/emotion/50.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1224px 0;"
															title="阴险"
															data-gifurl="${path}/images/emotion/51.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1248px 0;"
															title="亲亲"
															data-gifurl="${path}/images/emotion/52.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1272px 0;"
															title="吓"
															data-gifurl="${path}/images/emotion/53.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1296px 0;"
															title="可怜"
															data-gifurl="${path}/images/emotion/54.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1320px 0;"
															title="菜刀"
															data-gifurl="${path}/images/emotion/55.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1344px 0;"
															title="西瓜"
															data-gifurl="${path}/images/emotion/56.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1368px 0;"
															title="啤酒"
															data-gifurl="${path}/images/emotion/57.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1392px 0;"
															title="篮球"
															data-gifurl="${path}/images/emotion/58.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1416px 0;"
															title="乒乓"
															data-gifurl="${path}/images/emotion/59.gif"></div>
													</td>
												</tr>
												<tr>
													<td>
														<div class="eItem" style="background-position: -1440px 0;"
															title="咖啡"
															data-gifurl="${path}/images/emotion/60.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1464px 0;"
															title="饭"
															data-gifurl="${path}/images/emotion/61.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1488px 0;"
															title="猪头"
															data-gifurl="${path}/images/emotion/62.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1512px 0;"
															title="玫瑰"
															data-gifurl="${path}/images/emotion/63.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1536px 0;"
															title="凋谢"
															data-gifurl="${path}/images/emotion/64.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1560px 0;"
															title="示爱"
															data-gifurl="${path}/images/emotion/65.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1584px 0;"
															title="爱心"
															data-gifurl="${path}/images/emotion/66.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1608px 0;"
															title="心碎"
															data-gifurl="${path}/images/emotion/67.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1632px 0;"
															title="蛋糕"
															data-gifurl="${path}/images/emotion/68.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1656px 0;"
															title="闪电"
															data-gifurl="${path}/images/emotion/69.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1680px 0;"
															title="炸弹"
															data-gifurl="${path}/images/emotion/70.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1704px 0;"
															title="刀"
															data-gifurl="${path}/images/emotion/71.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1728px 0;"
															title="足球"
															data-gifurl="${path}/images/emotion/72.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1752px 0;"
															title="瓢虫"
															data-gifurl="${path}/images/emotion/73.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1776px 0;"
															title="便便"
															data-gifurl="${path}/images/emotion/74.gif"></div>
													</td>
												</tr>
												<tr>
													<td>
														<div class="eItem" style="background-position: -1800px 0;"
															title="月亮"
															data-gifurl="${path}/images/emotion/75.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1824px 0;"
															title="太阳"
															data-gifurl="${path}/images/emotion/76.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1848px 0;"
															title="礼物"
															data-gifurl="${path}/images/emotion/77.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1872px 0;"
															title="拥抱"
															data-gifurl="${path}/images/emotion/78.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1896px 0;"
															title="强"
															data-gifurl="${path}/images/emotion/79.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1920px 0;"
															title="弱"
															data-gifurl="${path}/images/emotion/80.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1944px 0;"
															title="握手"
															data-gifurl="${path}/images/emotion/81.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1968px 0;"
															title="胜利"
															data-gifurl="${path}/images/emotion/82.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -1992px 0;"
															title="抱拳"
															data-gifurl="${path}/images/emotion/83.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2016px 0;"
															title="勾引"
															data-gifurl="${path}/images/emotion/84.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2040px 0;"
															title="拳头"
															data-gifurl="${path}/images/emotion/85.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2064px 0;"
															title="差劲"
															data-gifurl="${path}/images/emotion/86.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2088px 0;"
															title="爱你"
															data-gifurl="${path}/images/emotion/87.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2112px 0;"
															title="NO"
															data-gifurl="${path}/images/emotion/88.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2136px 0;"
															title="OK"
															data-gifurl="${path}/images/emotion/89.gif"></div>
													</td>
												</tr>
												<tr>
													<td>
														<div class="eItem" style="background-position: -2160px 0;"
															title="爱情"
															data-gifurl="${path}/images/emotion/90.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2184px 0;"
															title="飞吻"
															data-gifurl="${path}/images/emotion/91.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2208px 0;"
															title="跳跳"
															data-gifurl="${path}/images/emotion/92.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2232px 0;"
															title="发抖"
															data-gifurl="${path}/images/emotion/93.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2256px 0;"
															title="怄火"
															data-gifurl="${path}/images/emotion/94.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2280px 0;"
															title="转圈"
															data-gifurl="${path}/images/emotion/95.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2304px 0;"
															title="磕头"
															data-gifurl="${path}/images/emotion/96.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2328px 0;"
															title="回头"
															data-gifurl="${path}/images/emotion/97.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2352px 0;"
															title="跳绳"
															data-gifurl="${path}/images/emotion/98.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2376px 0;"
															title="挥手"
															data-gifurl="${path}/images/emotion/99.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2400px 0;"
															title="激动"
															data-gifurl="${path}/images/emotion/100.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2424px 0;"
															title="街舞"
															data-gifurl="${path}/images/emotion/101.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2448px 0;"
															title="献吻"
															data-gifurl="${path}/images/emotion/102.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2472px 0;"
															title="左太极"
															data-gifurl="${path}/images/emotion/103.gif"></div>
													</td>
													<td>
														<div class="eItem" style="background-position: -2496px 0;"
															title="右太极"
															data-gifurl="${path}/images/emotion/104.gif"></div>
													</td>
												</tr>
											</tbody>
										</table>
										<div class="emotionsGif"></div>
									</div>
									<div class="clr"></div>
								</div>
								<div class="editArea">
									<textarea id="description" name="description" style="display: none;"></textarea>
									<div contenteditable="true" style="overflow-y: auto; overflow-x: hidden;"></div>
								</div>
							</div>
<script type="text/javascript">
		var $textarea = $(".editArea textarea");
		var $contentDiv = $(".editArea div");
		$(".functionBar .iconEmotion").click(function(){
			//Emotion.saveRange();
			$(".emotions").show();
		});
		$(".emotions").hover(function(){
			
		},function(){
			$(".emotions").fadeOut();
		});
		$(".emotions .eItem").mouseenter(function(){
			$(".emotionsGif").html('<img src="'+$(this).attr("data-gifurl")+'">');
		}).click(function(){
			Emotion.insertHTML('<img src="' + $(this).attr("data-gifurl") + '"' + 'alt="mo-' + $(this).attr("title") + '"' + "/>");
			$(".emotions").fadeOut();
			$textarea.trigger("contentValueChange");
		});
		$contentDiv.bind("keyup",function(){
			$textarea.trigger("contentValueChange");
			Emotion.saveRange();
		}).bind("keydown",function(e){
		    switch (e.keyCode) {
		    case 8:
		        var t = Emotion.getSelection();
		        t.type && t.type.toLowerCase() === "control" && (e.preventDefault(), t.clear());
		        break;
		    case 13:
		        e.preventDefault(),
		        Emotion.insertHTML("<br/>");
		        Emotion.saveRange();
		    }
		}).bind("mouseup",function(e){
		    Emotion.saveRange();
		    if ($.browser.msie && />$/.test($contentDiv.html())) {
		        var n = Emotion.getSelection();
		        n.extend && (n.extend(cursorNode, cursorNode.length), n.collapseToEnd()),
		        Emotion.saveRange();
		        Emotion.insertHTML(" ");
		    }
		});
		$textarea.bind("contentValueChange",function(){
			$(this).val(Emotion.replaceInput($contentDiv.html()));
		});
		$contentDiv.html(Emotion.replaceEmoji($textarea.html()));
</script>
						</div>
					</div>
					<div class="control-group" id="picContainer" style="display: none;">
						<label class="control-label" for="picandtext">图文资源:</label>
						<div class="controls">
							<input type="hidden" id="picandtext" value="${refImageTextInfoNo }">
							<button class="btn" type="button" onclick="sltImg();">选择图文</button>
							<span class="maroon">*</span> 必填，请选择一条图文素材
						</div>
					</div>
					<div class="form-actions">
						<button type="submit" class="btn-mine" id="submitbtn">保存</button>
						<button class="btn">取消</button>
					</div>
				</fieldset>
			</form>
		</div>
		<br/><div class="span5" id="preViewDiv"></div>
	</div>
	</div>
	</div>
	<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
