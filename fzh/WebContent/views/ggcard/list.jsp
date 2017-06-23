<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>刮刮卡活动列表</title>
<link rel="stylesheet" href="<%=basePath%>bootstrap/css/bootstrap.min.css">
<style type="text/css">
.icon{margin:3px;}
.icon:hover{cursor: pointer;}
</style>
<script type="text/javascript" src="<%=basePath%>js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>lhgdialog/lhgdialog.min.js?skin=idialog"></script>
</head>
<body>
	<table class="table table-bordered table-hover" style="width: 98%;margin: 0 auto;">
		<col /><col /><col /><col /><col /><col /><col style="width:150px;">
		<thead>
		<tr> 
			<td colspan="7"><button id="addbtn" class="btn" title="新增刮刮卡活动"><span class="icon-plus"></span>新增刮刮卡活动</button></td>
		</tr>
		<tr>
			<th>活动名称</th>
			<th>关键字</th>
			<th>有效参与人数</th>
			<th>总浏览数</th>
			<th>开始时间/结束时间</th>
			<th>状态</th>
			<th>操作</th>
		</tr>
		</thead>
		<tbody>
		<s:iterator value="dp.datas" id="id" status="c">
		<tr>
			<td>
				<s:if test="title.length()>20">
					<s:property value="title.substring(0,20)" />
				</s:if>
				<s:else>
					<s:property value="title" />
				</s:else>
			</td>
			<td>${keyword}</td>
			<td>${cpnum}</td>
			<td>${bpnum}</td>
			<td><s:property value="starttime.substring(0,19)" />～<s:property value="endtime.substring(0,19)" /></td>
			<td>
				<s:if test="%{status==3}">
					<span title="进行中" class="btn btn-success btn-mini i-start" rid="${id}">进行中</span>                 
				</s:if>
				<s:elseif test="%{status==9}">
					<span title="已结束" class="btn btn-warning btn-mini" rid="${id}">已结束</span>                 
				</s:elseif>
				<s:else>
					<span title="未开始" class="btn btn-mini" rid="${id}">未开始</span>
				</s:else>
			</td>
			<td>
				<span title="删除" class="icon icon-remove" rid="${id}"></span>
				<%-- <s:if test="%{status==0}">
					<span title="设置" class="icon icon-cog" rid="${id}"></span>
				</s:if> --%>
				<s:if test="%{status==3}">
					<span title="暂停" class="icon icon-stop" rid="${id}"></span>
				</s:if>
				<span title="SN码管理" class="icon icon-eye-open" rid="${id}"></span>
				<span title="复制活动URL" class="icon icon-globe" rid="${id}"></span>
			</td>
		</tr>
		</s:iterator>
		</tbody>
	</table>
</body>
<script type="text/javascript">
$(document).ready(function(){
	$("tbody tr").click(function(){
		if(lhgdialog.list['delbtn']||lhgdialog.list['stopbtn'])return;
		$("tbody tr").css("background-color","#FFF");
		$(this).css("background-color","#66E4FE");
	});
	//绑定增加按钮事件
	$("#addbtn").click(function(){
		location.href="<%=basePath%>wx/ggcard/content.jsp";
	});
	//绑定设置按钮事件
	$(".icon-cog").click(function(){
		location.href="<%=basePath%>wx/ggcard_doUpdateList.do?id="+$(this).attr("rid");
	});
	//绑定sn码按钮事件
	$(".icon-eye-open").click(function(){
		location.href="<%=basePath%>wx/ggcard_doSnList.do?pageno=1&pagesize=10&id="+$(this).attr("rid");
	});
	//复制url
	$(".icon-globe").click(function(){
		if(lhgdialog.list['delbtn']||lhgdialog.list['stopbtn'])return;
		clickfun($(this));
		
    	var rid=$(this).attr("rid");
    	
    	var content='<div><input style="width:380px" type="text" value="<%=basePath%>wx/ggcard_doResult.do?id='+rid+'"></div>';
		$.dialog({
		    id: 'globebtn',
			title:"复制活动URL",
			fixed: true,
			max:false,
			min:false,
			lock: false,
			width:400,
			resize: false,
			content:content
		});
	});
	//删除方法
	function delfun(obj){
		if(lhgdialog.list['delbtn'])return;
		clickfun(obj);
		var rid=$(obj).attr("rid");
		$.dialog({
		    id: 'delbtn',
		    icon: 'confirm.gif',
			title: false,
			cancel: false,
			fixed: true,
			lock: false,
			resize: false,
			content:'你确定要删除这个消息吗？',
			padding:"0px",
			ok:function(){
				$.post(
						"wx/ggcard_doRemove.do",
						{id:rid},
						function(data){
							if(data.success=="true"){
								$.dialog.tips('删除成功！',2,'success.gif',function(){
									location.href="wx/ggcard_doList.do";
								});
							}else{
								$.dialog.tips('删除失败！',2,'error.gif');
							}
				},"json");
			},
			cancel:true
		});
	}
	//停止活动
	function stopfun(obj){
		if(lhgdialog.list['delbtn']||lhgdialog.list['stopbtn'])return;
		clickfun(obj);
		
    	var rid=$(obj).attr("rid");
    	$.dialog({
		    id: 'stopbtn',
		    icon: 'confirm.gif',
			title: false,
			cancel: false,
			fixed: true,
			lock: false,
			resize: false,
			content:'你确定要停止该活动吗？',
			padding:"0px",
			ok:function(){
				$.post(
						"wx/ggcard_doUpdateForStatus.do",
						{id:rid},
						function(data){
							if(data.success=="true"){
								$.dialog.tips('停止活动成功！',2,'success.gif',function(){
									location.href="wx/ggcard_doList.do";
								});
							}else{
								$.dialog.tips('停止活动失败！',2,'error.gif');
							}
				},"json");
			},
			cancel:true
		});
	}
	//单击改变背景色
	function clickfun(obj){
		$("tbody tr").css("background-color","#FFF");
		$(obj).parent().parent().css("background-color","#66E4FE");
	}
	//单击事件绑定
	$(document).click(function (e) {
	    var target = e.target;
	    //删除
	    if($(target).hasClass("icon-remove")){
	    	delfun(target);
	    }
	    else if($(target).hasClass("icon-stop")){
	    	stopfun(target);
    	}
	    else{
	    	if(lhgdialog.list['delbtn']){
	    		$.dialog({id: 'delbtn'}).time(0.01);
	    	}
	    	else if(lhgdialog.list['stopbtn']){
	    		$.dialog({id: 'stopbtn'}).time(0.01);
	    	}
	    }
	});
});
</script>
</html>