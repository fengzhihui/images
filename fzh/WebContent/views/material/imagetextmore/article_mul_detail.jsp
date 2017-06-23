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
<link rel="stylesheet" href="${path}/css/appmsg-mul.css">
<link rel="stylesheet" href="${path}/operamasks-ui-2.0/css/default/om-fileupload.css">
<script type="text/javascript" src="${path}/operamasks-ui-2.0/js/operamasks-ui.min.js"></script>
<script type="text/javascript" src="${path}/js/html-helper.js"></script>
<script type="text/javascript" src="${path}/js/appmsg-mul.js"></script>
<script type="text/javascript" src="${path}/js/jquery.json-2.4.min.js"></script>
<script type="text/javascript">
		window.UEDITOR_HOME_URL = "${path}/ueditor/";
		window.fixedImagePath = "${path}/ueditor/jsp/";
		window.allBasePath = "${path}/";
		var charLimit = 10000;
</script>
<script type="text/javascript" src="${path}/ueditor/editor_config.js"></script>
<script type="text/javascript" src="${path}/ueditor/ueditor.all.js"></script>
<link rel="stylesheet" href="${path}/ueditor/themes/default/css/ueditor.css">

<!-- Demo page code -->
<style type="text/css">
#file_uploadUploader {
	visibility: hidden
}
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
.editor-bottom-bar {
	white-space: nowrap;
	border: 1px solid #ccc;
	line-height: 20px;
	font-size: 12px;
	text-align: right;
	margin-right: 5px;
	color: #aaa;
	border-top: 0;
	width: 498px;
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
				<legend>添加多图文</legend>
		</fieldset>
		<div class="row">
		<div class="span5 msg-preview">
			<div class="msg-item-wrapper">
				<div id="appmsgItem1" class="appmsgItem msg-item">
					<p class="msg-meta">
						<span class="msg-date">${currDate }</span>
					</p>
					<div class="cover">
						<p class="default-tip">封面图片</p>
						<h4 class="msg-t">
							<span class="i-title">标题</span>
						</h4>
						<ul class="abs tc sub-msg-opr">
							<li class="b-dib sub-msg-opr-item"><a href="javascript:;"
								class="th opr-icon edit-icon">编辑</a></li>
						</ul>
						<img class="i-img" style="">
					</div>
					<p class="msg-text"></p>

					<input type="hidden" value="" class="title">
					<input type="hidden" value="" class="cover">
					<input type="hidden" value="" class="coverurl">
					<textarea class="content" style="display: none;">null</textarea>
					<input type="hidden" value="" class="sourceurl">
					<input type="hidden" value="" class="chain">
				</div>

				<div class="rel sub-msg-item appmsgItem">
					<span class="thumb"> <span class="default-tip" style="">缩略图</span>
						<img src="" class="i-img" style="display: none">
					</span>
					<h4 class="msg-t">
						<span class="i-title">标题</span>
					</h4>
					<ul class="abs tc sub-msg-opr">
						<li class="b-dib sub-msg-opr-item"><a href="javascript:;" class="th opr-icon edit-icon">编辑</a></li>
						<li class="b-dib sub-msg-opr-item"><a href="javascript:;" class="th opr-icon del-icon">删除</a></li>
					</ul>
					<input type="hidden" class="title">
					<input type="hidden" class="cover">
					<input type="hidden" class="coverurl">
					<textarea class="content" style="display: none;">
					</textarea>
					<input type="hidden" class="sourceurl">
					<input type="hidden" class="chain">
				</div>

				<div class="sub-add">
					<a href="javascript:;" class="block tc sub-add-btn"> <span
						class="vm dib sub-add-icon"></span>增加一条
					</a>
				</div>
			</div>
		</div>
		<div class="span6">
			<div class="msg-editer-wrapper">
				<div class="msg-editer">
					<div class="control-group">
						<label class="control-label">标题</label><span class="maroon">*</span><span
							class="help-inline">(必填，不能超过64个字)</span>
						<div class="controls">
							<input type="text" value="" id="title" class="span5"
								style="width: 530px;" name="title">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">封面</label><span class="maroon">*</span><span
							class="help-inline">(必须上传一张图片)</span>
						<div class="controls">
							<div class="cover-area">
								<div class="cover-hd">
									<input id="file_upload" name="file_upload" type="file"
										style="display: none;" width="120" height="30"> <input
										id="coverurl" value="" name="coverurl" type="hidden">
								</div>
								<p id="upload-tip" class="upload-tip">大图片建议尺寸：700像素 * 300像素</p>
								<p id="imgArea" class="cover-bd" style="display: none;">
									<img src="" id="img"> <a href="javascript:;"
										class="vb cover-del" id="delImg">删除</a>
								</p>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">正文</label> <span class="maroon">*</span>
						<span class="help-inline">(必填，不能超过10000个字符)</span>
						<div class="controls">
							<div id="editor" class=" edui-default" style="height: 300px;"></div>
						</div>
					</div>
					<a id="chain-block-link" href="javascript:void(0);">添加外链</a>
					<div id="chain-block" style="display: none;" class="control-group">
						<label class="control-label">外链地址</label>
						<span class="help-inline">(设置后，点击图文消息，不会进入图文详情，而进入外链所设地址。)</span>
						<div class="controls">
							<input type="text" id="chain" class="span5" value=""
								style="width: 482px;" name="chain">
						</div>
					</div>
					<p></p><a id="url-block-link" href="javascript:void(0);">添加来源</a><p>
					<div id="url-block" style="display: none;" class="control-group">
						<label class="control-label">来源</label> <span class="help-inline">(请输入正确的URL链接格式)</span>
						<div class="controls">
							<input type="text" id="source_url" class="span5" value=""
								style="width: 482px;" name="source_url">
						</div>
					</div>

					<div class="control-group">
						<div class="controls">
							<button id="save-btn" type="submit"
								class="btn-mine">保存</button>
							<button id="cancel-btn" type="button" class="btn">取消</button>
						</div>
					</div>

					<input id="uid" name="uid" type="hidden" value="886">
					<input id="action" type="hidden" value="add">
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
				<iframe frameborder="0" width="100%" height="100%" src="javascript:"
					class="iframe-style edui-default"> </iframe>
				<div class="edui-shadow edui-default"></div>
				<div id="edui43_content" class="edui-popup-content edui-default"></div>
			</div>
		</div>
	</div>
	</div>
	</div>
	<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
