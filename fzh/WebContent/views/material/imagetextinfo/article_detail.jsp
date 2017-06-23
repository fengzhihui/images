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
<link rel="stylesheet" type="text/css" href="${path}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${path}/css/theme.css">
<link rel="stylesheet" href="${path}/lib/font-awesome/css/font-awesome.css">
<script type="text/javascript" src="${path}/lib/jquery-1.7.2.min.js" ></script>
<script type="text/javascript" src="${path}/js/bootstrap.js" ></script>
<link rel="stylesheet" href="${path}/css/appmsg.css">
<link rel="stylesheet" href="${path}/operamasks-ui-2.0/css/default/om-fileupload.css">
<script type="text/javascript" src="${path}/operamasks-ui-2.0/js/operamasks-ui.min.js"></script>
<script type="text/javascript" src="${path}/js/html-helper.js"></script>
<script type="text/javascript">
		window.UEDITOR_HOME_URL = "${path}/ueditor/";
		window.fixedImagePath = "${path}/ueditor/jsp/";
		window.allBasePath = "${path}/";
		var charLimit = 10000;
</script>
<script type="text/javascript" src="${path}/js/appmsg.js"></script>
<script type="text/javascript" src="${path}/ueditor/editor_config.js"></script>
<script type="text/javascript" src="${path}/ueditor/ueditor.all.js"></script>
<link rel="stylesheet" href="${path}/ueditor/themes/default/css/ueditor.css">

<!-- Demo page code -->
<style type="text/css">
label {
	display: inline-block;
}
.help-inline {
	vertical-align: top;
}
.row {
	padding-top: 20px;
	padding-bottom: 20px;
	margin-left: 30px;
}
.iframe-style {
	position: absolute;
	z-index: -1;
	left: 0;
	top: 0;
	background-color: transparent;
}
</style>
</head>
<body class="">
	<jsp:include page="/common/header.jsp" flush="true" />
	<jsp:include page="/common/leftbar.jsp" flush="true" />
	<div class="content">
		<div class="container-fluid">
		<fieldset>
			<legend>添加单图文</legend>
		</fieldset>
		<input type="hidden" id="ctx" value="${path}">
	<div class="row">
		<div class="span5 msg-preview">
			<div class="msg-item-wrapper">
				<div id="appmsgItem1" class="msg-item">
					<h4 class="msg-t">
						<span class="i-title">标题</span>
					</h4>
					<p class="msg-meta">
						<span class="msg-date">${currDate}</span>
					</p>
					<div class="cover">
						<p class="default-tip">封面图片</p>
						<img class="i-img" style="">
					</div>
					<p class="msg-text"></p>
				</div>
				<div class="msg-hover-mask"></div>
				<div class="msg-mask">
					<span class="dib msg-selected-tip"></span>
				</div>
			</div>
		</div>

		<div class="span6">
			<div class="msg-editer-wrapper">
				<div class="msg-editer">
					<form id="appmsg-form" class="form">
						<div class="control-group">
							<label class="control-label">标题</label><span class="maroon">*</span><span
								class="help-inline">(必填，不能超过64个字)</span>
							<div class="controls">
								<input type="text" id="title" name="title" class="span5" style="width: 530px;">
							</div>
							<input type="hidden" id="flag" value="add"/>
						</div>
						<div class="control-group">
							<label class="control-label">封面类型</label><span class="maroon">*</span>
							<select class="span2" id="picType" name="picType">
								<option value="1">上传图片</option>
								<option value="2">添加链接</option>
							</select>
							<div class="controls" id="picUrl" style="display: none;">
								<input type="text" class="span5" style="width: 530px;" name="picUrl">
							</div>
						</div>
						<div class="control-group" id="cover">
							<label class="control-label">封面</label><span class="maroon">*</span><span
								class="help-inline">(必须上传一张图片)</span>
							<div class="controls">
								<div class="cover-area">
									<div class="cover-hd">
										<input id="file_upload" name="file_upload" type="file"
											style="display: none;" width="120" height="30">
											<input id="imageUrl" name="imageUrl" type="hidden">
											<!-- 数据库中保存封面图片名称 -->
									</div>
									<p id="upload-tip" class="upload-tip">大图片建议尺寸：360像素 * 200像素</p>
									<p id="imgArea" class="cover-bd" style="display: none;">
										<img src="" id="img"> <a href="javascript:;"
											class="vb cover-del" id="delImg">删除</a>
									</p>
								</div>
							</div>
						</div>
						<a id="desc-block-link" href="javascript:void(0);">添加摘要</a><p>
						<div id="desc-block" style="display: none;" class="control-group">
							<label class="control-label">摘要</label> <span class="help-inline">(不能超过120个字)</span>
							<div class="controls">
								<textarea id="digest" name="digest" class="msg-txta"></textarea>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">正文</label> <span class="maroon">*</span><span
								class="help-inline">(必填，不能超过10000个字符)</span>
							<div class="controls" style="width:550px;">
								<!--style给定宽度可以影响编辑器的最终宽度-->
								<script id="editor" type="text/plain" style="height:300px;"></script>
							</div>
						</div>
						<a id="chain-block-link" href="javascript:void(0);">添加外链</a>
						<div id="chain-block" style="display: none;" class="control-group">
							<label class="control-label">外链地址</label> <span
								class="help-inline">(设置后，点击图文消息，不会进入图文详情，而进入外链所设地址。)</span>
							<div class="controls">
								<input type="text" class="span5" style="width: 530px;" name="clickOutUrl">
							</div>
						</div>
						<p><p/><a id="url-block-link" href="javascript:void(0);" >添加原文链接</a>
						<div id="url-block" style="display: none;" class="control-group">
							<label class="control-label">原文链接</label> <span
								class="help-inline">(在图文详情页面中会生成“阅读原文”链接)</span>
							<div class="controls">
								<input type="text" class="span5" style="width: 530px;" name="sourceUrl">
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<button id="save-btn" type="submit" class="btn-mine">保存</button>
								<button id="cancel-btn" type="button" class="btn">取消</button>
							</div>
						</div>
						<input id="uid" name="uid" type="hidden" value="886">
					</form>
				</div>
				<span class="abs msg-arrow a-out" style="margin-top: 0px;"></span> <span
					class="abs msg-arrow a-in" style="margin-top: 0px;"></span>
			</div>
		</div>

	</div>

	<div id="edui_fixedlayer" class=" edui-default"
		style="position: fixed; left: 0px; top: 0px; width: 0px; height: 0px;">
		<div id="edui43" class="edui-popup  edui-bubble edui-default"
			style="display: none;">
			<div id="edui43_body" class="edui-popup-body edui-default">
				<iframe width="100%" height="100%" frameborder="0" src="javascript:" class="iframe-style edui-default"> </iframe>
				<div class="edui-shadow edui-default"></div>
				<div id="edui43_content" class="edui-popup-content edui-default">
				</div>
			</div>
		</div>
	</div>
	</div>
	</div>
	<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
