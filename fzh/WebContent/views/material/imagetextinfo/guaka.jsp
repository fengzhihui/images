<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>刮刮卡设置</title>
<link rel="stylesheet" href="<%=basePath %>css/bootstrap.min.css">
<link rel="stylesheet" href="<%=basePath %>css/admin.css">
<link rel="stylesheet" href="<%=basePath %>css/appmsg.css">
<link rel="stylesheet" href="<%=basePath %>operamasks-ui-2.0/css/default/om-fileupload.css">

<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath %>operamasks-ui-2.0/js/operamasks-ui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/html-helper.js"></script>
<script type="text/javascript" src="<%=basePath %>js/appmsg.js"></script>
<script type="text/javascript">
		window.UEDITOR_HOME_URL = "<%=basePath %>ueditor/";
		window.fixedImagePath = "<%=basePath %>ueditor/jsp/";
		window.allBasePath = "<%=basePath %>";
		var charLimit = 80000;
</script>

<script type="text/javascript" src="<%=basePath %>ueditor/editor_config.js"></script>
<script type="text/javascript" src="<%=basePath %>ueditor/ueditor.all.js"></script>
<link rel="stylesheet" href="<%=basePath %>ueditor/themes/default/css/ueditor.css">

<style>
label {
	display: inline-block;
}

.help-inline {
	vertical-align: top;
}

.row {
	padding-top: 20px;
	padding-bottom: 20px;
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
</style>
</head>
<body>
<input type="hidden" id="ctx" value="<%=request.getContextPath()%>">
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

		<div class="span7">
			<div class="msg-editer-wrapper">
				<div class="msg-editer">
					<form id="appmsg-form" class="form">
						<div class="control-group">
							<label class="control-label">活动名称</label><span class="maroon">*</span><span
								class="help-inline">(必填，不能超过64个字)</span>
							<div class="controls">
								<input type="text" value="" id="title" class="span5"
									style="width: 482px;" name="title">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">封面图片</label><span class="maroon">*</span><span
								class="help-inline">(必须上传一张图片)</span>
							<div class="controls">
								<div class="cover-area">
									<div class="cover-hd">
										<input id="file_upload" name="file_upload" type="file"
											style="display: none;" width="120" height="30"> <input
											id="coverurl" value="" name="coverurl" type="hidden">
									</div>
									<p id="upload-tip" class="upload-tip">大图片建议尺寸：360像素 * 200像素</p>
									<p id="imgArea" class="cover-bd" style="display: none;">
										<img src="" id="img"> <a href="javascript:;" class="vb cover-del" id="delImg">删除</a>
									</p>
								</div>
							</div>
						</div>
						<a id="desc-block-link" href="javascript:void(0);"
							class="url-link block">添加摘要</a>
						<div id="desc-block" style="display: none;" class="control-group">
							<label class="control-label">摘要描述</label> <span class="help-inline">(不能超过120个字)</span>
							<div class="controls">
								<textarea name="summary" id="summary" class="msg-txta"></textarea>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">触发关键词</label><span class="maroon">*</span><span
								class="help-inline">(必填，不能超过10个字)</span>
							<div class="controls">
								<input type="text" value="" id="keyword" class="span5"
									style="width: 482px;" name="keyword">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">开始时间</label><span class="maroon">*</span><span
								class="help-inline">(必填)</span>
							<div class="controls">
								<input type="text" value="" id="begin" class="span5"
									style="width: 482px;" name="begin">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">结束时间</label><span class="maroon">*</span><span
								class="help-inline">(必填)</span>
							<div class="controls">
								<input type="text" value="" id="end" class="span5"
									style="width: 482px;" name="end">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">概率设置</label><span class="maroon">*</span><span
								class="help-inline">(必填，默认一等奖个数为1，二等奖个数为2，三等奖个数为3)</span>
							<div class="controls">
								<input type="text" value="" id="rate" class="span5"
									style="width: 482px;" name="rate">
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<button id="save-btn" type="submit"
									class="btn btn-primary btn-large">保存</button>
								<button id="cancel-btn" type="button" class="btn btn-large">取消</button>
							</div>
						</div>
						<input id="uid" name="uid" type="hidden" value="886">
						<input id="action" name="action" type="hidden" value="add">
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
				<iframe
					style="position: absolute; z-index: -1; left: 0; top: 0; background-color: transparent;"
					frameborder="0" width="100%" height="100%" src="javascript:"
					class=" edui-default"> </iframe>
				<div class="edui-shadow edui-default"></div>
				<div id="edui43_content" class="edui-popup-content edui-default">
				</div>
			</div>
		</div>
	</div>
</body>
</html>