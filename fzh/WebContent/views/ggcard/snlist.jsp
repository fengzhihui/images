<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String id=request.getParameter("id");
	String type=request.getParameter("type")==null?"":request.getParameter("type");
	String qrystr=request.getParameter("qrystr")==null?"":new String(request.getParameter("qrystr").getBytes("ISO-8859-1"),"UTF-8");
	String status=request.getParameter("status")==null?"":request.getParameter("status");
	
	List<HashMap> listhm=(List<HashMap>)session.getAttribute("ggcard_slist");
	String tipshtml="";
	int countsum=0;
	HashMap<String,String> hm=new HashMap<String,String>();
	for(int i=0;i<listhm.size();i++){
		tipshtml+="<span style='margin: 2px 15px;'>"+listhm.get(i).get("rname").toString()+"：<a href='javascript:void(0);' class='changstatus' status='"+listhm.get(i).get("rid").toString()+"'>"+listhm.get(i).get("count").toString()+"</a>张</span>";
		countsum+=Integer.parseInt(listhm.get(i).get("count").toString());
		hm.put(listhm.get(i).get("rid").toString(), listhm.get(i).get("rname").toString());
	}
	tipshtml="<span style='margin: 2px 15px;'>记录总数：<a href='javascript:void(0);' class='changstatus' status='%'>"+countsum+"</a>张</span>"+tipshtml;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>刮刮卡-抽奖记录</title>
<link rel="stylesheet" href="<%=basePath%>bootstrap/css/bootstrap.min.css"> 
<script type="text/javascript" src="<%=basePath%>js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>bootstrap/js/bootstrap.min.js"></script>
<script src="<%=basePath%>lhgdialog/lhgdialog.min.js?skin=idialog"></script>
</head>
<body style="width: 98%;margin: 0 auto;">
	<div>
		<h3>刮刮卡-抽奖记录</h3>
		<div class="table table-bordered" style="background: #FFEFAF;line-height: 40px;">
			<a class="close" data-dismiss="alert" href="#">&times;</a>
			<%=tipshtml %>
			<%-- <span style="margin: 2px 15px;">记录总数：<a href="javascript:void(0);" class="changstatus" status="%"><%=session.getAttribute("ggcard_s")%></a>张</span>
			<span style="margin: 2px 15px;">一等奖：<a href="javascript:void(0);" class="changstatus" status="1"><%=session.getAttribute("ggcard_s1") %></a>张</span>
			<span style="margin: 2px 15px;">二等奖：<a href="javascript:void(0);" class="changstatus" status="2"><%=session.getAttribute("ggcard_s2") %></a>张</span>
			<span style="margin: 2px 15px;">三等奖：<a href="javascript:void(0);" class="changstatus" status="3"><%=session.getAttribute("ggcard_s3") %></a>张</span>
 --%>		</div>
		<form class="form-search">
			<div class="btn-group" style="width:73px;display: inline-block;">
                <button class="btn" data-toggle="dropdown" name="queryway" queryway="sn">SN码查询<span class="caret"></span></button>　
                <ul class="dropdown-menu">
                  <li><a href="javascript:void(0);" queryway="sn">SN码查询</a></li>
                  <li><a href="javascript:void(0);" queryway="tel">手机查询</a></li>
                </ul>
				<input type="text" class="input-medium" value="<%=qrystr %>" maxlength="11">　
				<button type="button" class="btn" id="submitbtn">查询</button>　
				<button type="reset" class="btn">重置</button>
            </div>
		</form>
	</div>
	<table class="table table-bordered table-hover" >
		<col style="width:80px;"><col /><col /><col /><col /><col /><col /><col style="width:60px;">
		<thead>
		<tr>
			<th>奖项</th>
			<th>SN码</th>
			<th>状态</th>
			<th>姓名</th>
			<th>手机号码</th>
			<th>抽奖时间</th>
			<th>使用时间</th>
			<th>操作</th>
		</tr>
		</thead>
		<tbody>
		<script type="text/javascript">
			var jsonstr={};
			<%
			Iterator iter = hm.entrySet().iterator();
			Entry entry=null;
			while (iter.hasNext()) {
				entry = (Entry) iter.next();
				if(entry.getKey().toString().equals("9999")){
			%>
				jsonstr["<%=entry.getKey()%>"]="";
			<%
				}
				else{
			%>
				jsonstr["<%=entry.getKey()%>"]="<%=entry.getValue()%>";
			<%
				}
			}
			%>

		</script>
		<s:iterator value="dp.datas" id="id" status="c">
		<tr>
			<td><script type="text/javascript">document.write(jsonstr["<s:property value="rid" />"]);</script></td>
			<td><s:property value="snno" /></td>
			<td>
				<s:if test="%{rid!=9999 && status2==0}"><span title="未确认" class="btn btn-mini">未确认</span></s:if>
				<s:elseif test="%{status2==1 && status==0}"><span title="已确认" class="btn btn-mini btn-success">已确认</span></s:elseif>
				<s:elseif test="%{status==1}"><span title="已使用" class="btn btn-warning btn-mini">已使用</span></s:elseif>
			</td>
			<td><s:property value="username" />&nbsp;</td>
			<td><s:property value="telno" />&nbsp;</td>
			<td><s:date name="cretime" format="yyyy-MM-dd HH:mm:ss"/>&nbsp;</td>
			<td><s:date name="usetime" format="yyyy-MM-dd HH:mm:ss"/>&nbsp;</td>
			<td><s:if test="%{status2==1 && status==0}">[<a class="usesn" rid="<s:property value="id" />" href="javascript:void(0);">使用</a>]</s:if>&nbsp;</td>
		</tr>
		</s:iterator>
		</tbody>
	</table>
	<div>
		<% String url="wx/ggcard_doSnList.do?id="+id+"&type="+type+"&qrystr="+qrystr+("".equals(status)?"":("&status="+status)); %>
   </div>
	<script type="text/javascript">
	$(document).ready(function(){
		$(".dropdown-menu a").click(function(){
			$("button[name='queryway']").html($(this).text()+'<span class="caret"></span>');
			$("button[name='queryway']").attr("queryway",$(this).attr("queryway"));
		});
		$("#submitbtn").click(function(){
			if($(".input-medium").val()!=""){
				var url="type="+$("button[name='queryway']").attr("queryway")+"&qrystr="+$(".input-medium").val();
				location.href="<%=basePath%>wx/ggcard_doSnList.do?pageno=1&pagesize=<%=request.getAttribute("pagesize")%>&id=<%=request.getParameter("id")%>"+"&"+url;
			}
		});
		
		$(".changstatus").click(function(){
			location.href="<%=basePath%>wx/ggcard_doSnList.do?pageno=1&pagesize=<%=request.getAttribute("pagesize")%>&id=<%=request.getParameter("id")%>&status="+$(this).attr("status");
		});
		$("button[type='reset']").click(function(){
			location.href="<%=basePath%>wx/ggcard_doSnList.do?pageno=1&pagesize=<%=request.getAttribute("pagesize")%>&id=<%=request.getParameter("id")%>";
		});
		//单击改变背景色
		function clickfun(obj){
			$("tbody tr").css("background-color","#FFF");
			$(obj).parent().parent().css("background-color","#66E4FE");
		}
		function usefun(obj){
			if(lhgdialog.list['usebtn'])return;
			clickfun(obj);
			
	    	var rid=$(obj).attr("rid");
	    	$.dialog({
			    id: 'usebtn',
			    icon: 'confirm.gif',
				title: false,
				cancel: false,
				fixed: true,
				lock: false,
				resize: false,
				content:'你是否确定使用该sn码？',
				padding:"0px",
				ok:function(){
					$.dialog.tips('请稍后...',300,'loading.gif'); 
					$.post(
							"wx/ggcard_doUseSn.do",
							{id:rid},
							function(data){
								if(data.success=="true"){
									$.dialog.tips('使用sn码成功！',2,'success.gif',function(){
										location.href="<%=basePath%>wx/ggcard_doSnList.do?pageno=<%=request.getAttribute("pageno")%>&pagesize=<%=request.getAttribute("pagesize")%>&id=<%=request.getParameter("id")%>";
									});
								}else{
									$.dialog.tips('使用sn码失败！',2,'error.gif');
								}
					},"json");
				},
				cancel:true
			});
		}
		//单击事件绑定
		$(document).click(function (e) {
		    var target = e.target;
		    //删除
		    if($(target).hasClass("usesn")){
		    	usefun(target);
		    }
		    else{
		    	if(lhgdialog.list['usebtn']){
		    		$.dialog({id: 'usebtn'}).time(0.01);
		    	}
		    }
		});
	<%
	if(type!=null){
	%>
		$(".dropdown-menu a[queryway='<%=type%>']").click();
	<%
	}
	%>
	});
	</script>
</body>
</html>