<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>刮刮卡设置</title>
<link rel="stylesheet" href="<%=basePath %>bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=basePath %>css/admin.css">
<link rel="stylesheet" href="<%=basePath %>operamasks-ui-2.0/css/default/om-fileupload.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>timepicker/css/jquery-ui.css" />
<link rel="stylesheet" href="<%=basePath %>bootstrap/iCheck-master/skins/all.css?v=1.0.2">
<link rel="stylesheet" href="<%=basePath %>/wx/ggcard/css/ggcard.css">
	<script type="text/javascript">
	window.UEDITOR_HOME_URL = "<%=basePath %>ueditor/";
	window.fixedImagePath="<%=basePath %>ueditor/jsp/";
	window.allBasePath = "<%=basePath %>";
	</script>
	<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>operamasks-ui-2.0/js/operamasks-ui.min.js"></script>
	<script src="<%=basePath%>lhgdialog/lhgdialog.min.js?skin=idialog"></script>
	<script src="<%=basePath %>bootstrap/iCheck-master/icheck.js?v=1.0.2"></script>
	<script type="text/javascript" src="<%=basePath %>wx/ggcard/js/ggcard.js"></script>

	<script type="text/javascript" src="<%=basePath %>bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>timepicker/js/jquery-ui.js"></script>
	<script type="text/javascript" src="<%=basePath %>timepicker/js/jquery-ui-slide.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>timepicker/js/jquery-ui-timepicker-addon.js"></script>
</head>
<body>
	<div class="row">
		<div class="span5 msg-preview">
			<div class="msg-item-wrapper">
				<div class="msg-item">
					<h4 class="msg-t">
						<span class="i-title">标题</span>
					</h4>
					<div class="cover">
						<!-- <p class="default-tip">封面图片</p> -->
						<img class="i-img" src="<%=basePath %>images/upload/ggcard/ggcard-bg.png"> 
					</div>
					<p class="msg-text">活动说明</p>
				</div>
			</div>
		</div>
		<div class="span7">
			<div class="msg-editer-wrapper">
				<div class="msg-editer">
					<form id="ggcard-form" class="form">
						<input id="id" name="id" type="hidden" value="">
						<div class="control-group">
							<label class="control-label">标题</label><span class="maroon">*</span><span class="help-inline">(必填,不能超过32个字)</span>
							<div class="controls">
						    	<input type="text" value="" id="title" class="span5" style="width: 485px;" name="title">
						    </div>
					    </div>
					    <div class="control-group">
							<label class="control-label">封面图片</label><span class="maroon">*</span><span class="help-inline">(只可选一张图片,显示于微信端图文信息封面图片)</span>
							<div class="controls">
								<div class="cover-area">
									<div class="cover-hd">
										<input id="pic" name="pic" type="file" style="display: none;" width="120" height="30">
									</div>
									<p id="upload-tip" class="upload-tip">大图片建议尺寸：500像素 * 300像素</p>
									<p id="fengmianArea" class="fm-cover-bd" style="display: none;"></p>
									<script type="text/javascript">
									$(document).ready(function(){
										$("#fengmianArea").html('<li class="items">'+
												'<img src="<%=basePath %>images/upload/ggcard/ggcard-bg.png">'+
												'<input type="text" name="picurl" style="display:none" value="ggcard/ggcard-bg.png" />'+
												'<a href="javascript:void(0);" onclick="$(this).parent().remove();" class="vb cover-del">删除</a>'+
												'</li>');
										$("#fengmianArea").show();
									});
									</script>
								</div>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">活动说明</label><span class="maroon">*</span><span class="help-inline">(必填,不能超过<span id="contenttips">0</span>/2000个字)</span>
							<div class="controls">
							    <textarea id="content" class="span5" style="width: 485px;min-height: 60px;" name="content" maxlength="2000"></textarea>
							</div>
					    </div>
					    <div class="control-group">
							<label class="control-label">活动时间</label><span class="maroon">*</span><span class="help-inline">(结束时间必须在开始时间之后)</span>    
							<div class="controls">
								<div class="cover-area">
									<label class="control-label">&nbsp;&nbsp;从&nbsp;&nbsp;</label>
									<input type="text" id="starttime" name="starttime" style="width:160px;margin: 10px;" readonly="readonly"/>
									<label class="control-label">&nbsp;&nbsp;至&nbsp;&nbsp;</label>
									<input type="text" id="endtime" name="endtime" style="width:160px;margin: 10px;" readonly="readonly"/>
								</div>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">兑换截止日期</label><span class="maroon">*</span><span class="help-inline"></span>
							<div class="controls">
						    	<input type="text" value="" id="exptime" class="span5" style="width: 485px;" name="exptime" readonly="readonly">
						    </div>
					    </div>
					    <div class="control-group">
							<label class="control-label">关键字</label><span class="maroon">*</span><span class="help-inline">(必填,不能超过10个字)</span>
							<div class="controls">
						    	<input type="text" value="" id="keyword" class="span5" style="width: 485px;" name="keyword">
						    </div>
					    </div> 
					    <div class="control-group">
							<label class="control-label">用户每天可参与次数</label><span class="maroon">*</span><span class="help-inline">(大于0的整数)</span>
							<div class="controls">
						    	<input type="text" class="span5" style="width: 485px;" name="daynum" value="1">
						    </div>
					    </div> 
					    <div class="control-group">
							<label class="control-label">用户可参与总次数</label><span class="maroon">*</span><span class="help-inline">(大于0的整数)</span>
							<div class="controls">
						    	<input type="text" class="span5" style="width: 485px;" name="maxnum" value="1">
						    </div>
					    </div> 
					    <div class="control-group">
						<label class="control-label">关联活动</label><span class="help-inline"></span>
							<div class="controls">
						    	<div class="btn-group">
								  <a id="dropdown" class="btn dropdown-toggle" data-toggle="dropdown" href="javascript:void(0);">
								    <span id="dropdown-text"></span>
								    <span class="caret"></span>
								  </a>
								  <ul id="dropdown-menu" class="dropdown-menu"></ul>
								  <input type="hidden" name="researchfk">
								</div>
						    </div>
					     </div> 
					     <div class="control-group">
							<label class="control-label">奖项设置</label><span class="maroon">*</span><span class="help-inline"></span>    
							<div class="controls">
								<div class="cover-area">
								<table class="table table-bordered table-hover jp-table" style="width: 98%;margin: 5px auto;">
								<col width="16%"><col width="44%"><col width="20%"><col width="20%">
								<thead>
									<tr><th>奖项</th><th>奖品名称(小于50个字)</th><th>奖品数量</th><th>中奖概率</th></tr>
								</thead>
								<tbody>
									<tr><td class="rname">一等奖</td><td><input type="text" name="rcontent"></td><td><input type="text" name="rnum"></td><td><input type="text" name="probability"></td></tr>
									<tr><td class="rname">二等奖</td><td><input type="text" name="rcontent"></td><td><input type="text" name="rnum"></td><td><input type="text" name="probability"></td></tr>
									<tr><td class="rname">三等奖</td><td><input type="text" name="rcontent"></td><td><input type="text" name="rnum"></td><td><input type="text" name="probability"></td></tr>
								</tbody>
								</table>
								<div style="text-align: right;padding-right: 5px;padding-bottom: 5px;">
									<a id="addone" class="btn btn-mini" title="增加一行"><span class="icon-plus"></span>增加一行</a>
									<a id="removeone" class="btn btn-mini" title="删除一行"><span class="icon-remove"></span>删除一行</a>
								</div>
								</div>
							</div>
						</div>
					    <div class="control-group">
							<label class="control-label">是否显示奖品数量</label><span class="maroon">*</span>
							<span class="help-inline">
								<input type="checkbox" name="isshownum" id="minimal-checkbox-2" checked="checked" />
					            <label for="minimal-checkbox-2" class="">取消选择后在活动页面中将不会显示奖品数量</label>
							</span>
					        <script>
								$(document).ready(function(){
								  $('#minimal-checkbox-2').on('ifClicked', function(event){
										$(this).attr("checked")=="checked"?$(this).removeAttr("checked"):$(this).attr("checked","checked");
								    }).iCheck({
								    checkboxClass: 'icheckbox_minimal checked',
								    radioClass: 'iradio_minimal',
								    increaseArea: '20%' // optional
								  });
								});
							</script>
					    </div> 
						
					  	<div class="control-group">
						    <div class="controls">
						      <button id="save-btn" type="submit" class="btn btn-primary btn-large">保存</button>
						      <button id="cancel-btn" type="button" class="btn btn-large">取消</button>
						    </div>
					    </div>
					</form> 
				</div>
				<span class="abs msg-arrow a-out" style="margin-top: 0px;"></span>
				<span class="abs msg-arrow a-in" style="margin-top: 0px;"></span>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
function setText(obj){
	$("#dropdown-text").html($(obj).text().length>20?$(obj).text().substring(0,17)+"...":$(obj).text());
	$("input[name='researchfk']").val($(obj).attr("rid"));
}
$(document).ready(function(){
	$("#dropdown").click(function(){
		if($("#dropdown-menu").html()!="")return;
		$.post("wx/coupon_doFindURList.do",null,
			function(data){
				for(var i=0;i<data.length;i++){
					$("#dropdown-menu").append('<li rid="'+data[i].rid+'" onclick="setText(this)"><a href="javascript:void(0);">'+data[i].name+'</a></li>');
				}
				if($("#dropdown-menu").html()=="")$("#dropdown-menu").html("没有找到相关活动");
				$("#dropdown").click();
		},"json");
		return false;
	});
});
</script>
</html>