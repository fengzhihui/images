$("#save-btn").click(function() {
    var btn = $(this), tel = $("#tel").val();
    if (tel == "") {
        alert("请输入手机号码");
        return;
    }
    var regu = /^[1][0-9]{10}$/, re = new RegExp(regu);
    if (!re.test(tel)) {
        alert("请输入正确手机号码");
        return;
    }
    var submitData = {
        "rid": $("#rid").val(),
        "openId": window.openId,
        "mobile": tel
    };
    $.post(window.allBasePath+"wx/ggcard_doConfirm.do", submitData, function(data) {
        if (data.success == "true") {
            alert("提交成功，谢谢您的参与");
            try{
            	WeixinJSBridge.call('closeWindow');
            }catch(e){
            	location.href=location.href;
            }
            return;
        }
    }, "json");
	}
);
function init(){
	var iii=0,jjj=0;
	$("#scratchpad").html("");
	$('#scratchpad').wScratchPad('reset');
	var mm=$("#scratchpad").wScratchPad({
		width: 120,
		height: 40,
		color: "#a9a9a7",
		scratchDown: function() {
			if(iii==0){
				// 判断先前条件
				if($("#scratchpad").attr("isvip")=="no"){
					$("#regeditA").click();
					init();
					return;
				}
				if($("#scratchpad").attr("iscan")=="no"){
					$("#researchB").click();
					init();
					return;
				}
				if(parseInt($("#scratchpad").attr("starttime"))>0){
					alert("活动还没有开始哟！");
					init();
					return;
				}
				if(parseInt($("#scratchpad").attr("endtime"))<0){
					alert("活动已经结束了，请关注我们其他活动！");
					init();
					return;
				}
				// 从后台获取刮奖结果
				$.post(
				window.allBasePath+"wx/ggcard_doDo.do",
				{"id": window.id,"openId": window.openId,"t": Math.random()},
				function(data){
					if(data.success=="true"){
						$("#tel").val(data.mobile);
	            		$("#rid").val(data.rid);
						$("#prize").html(data.result);
						$("#sncode").text(data.sn);
						$("#prizetype").text(data.result);
					
	            		if(data.type=="getsn"){
	            			alert("您之前已经中奖啦，请查看并确认您中奖的信息！");
	                        $("#result").slideToggle(500);
	                        $("#outercont").slideUp(500);
	                        return;
	            		}
					}
				},"json");
			}
			iii++;
		},
		scratchMove: function() {
			if(jjj==0&&this.scratchPercentage(this)>50){
				var content;
				if($("#prize").html()=="谢谢参与"){
					content='<div><font style="font-size:16px;color:red;">很遗憾,你没有中奖！</font></div>';
					content+='<div id="tip_tools"><button id="tryone" class="btn btn-info">再来一次</button><button id="closeweb" style="margin-left:10px;" class="btn btn-danger">关闭页面</button></div>';
					$.dialog({
						id: 'result_tips',
						title:false,
						fixed: true,
						max:false,
						min:false,
						lock: false,
						resize: false,
						content:content
					});
				}
				else{
					$("#result").slideToggle(500);
                    $("#outercont").slideUp(500);
				}
				jjj++;
			}
		}
	});
}
function hidediv(id){
	$.dialog({id: id}).time(0.01);
}
$(function() {
	init();
});
$(document).click(function (e) {
	var target = e.target;
	if($(target).attr("id")=="tryone"){
		init();
		$.dialog({id: 'result_tips'}).time(0.01);
	}
	else if($(target).attr("id")=="regeditA"){
		$.dialog({
		    id: 'regeditAF',
			title:false,
			fixed: true,
			max:false,
			min:false,
			width:250,
			lock: false,
			resize: false,
			content:$("#regeditAF").html()
		});
	}
	else if($(target).attr("id")=="researchB"){
		$.dialog({
		    id: 'researchBF',
			title:false,
			fixed: true,
			max:false,
			min:false,
			width:250,
			lock: false,
			resize: false,
			content:$("#researchBF").html()
		});
	}
	else if($(target).attr("id")=="resbtn"){
		location.href=window.allBasePath+"wx/userresearch_doAttendResearch2.do?researchId="+$(target).attr("researchfk")+"&wxOpenId=null"

	}
	else if($(target).attr("id")=="regbtn"){
		var bl=true;
		var username=$("#regform input[name='username']").val();
		var mobile=$("#regform input[name='mobile']").val();
		if(mobile==""){
			$("#regform input[name='mobile']").css("border","red 1px solid");
			$.dialog.tips("手机号码不能为空！",2,'error.gif',function(){
				$("#regform input[name='mobile']").focus();
			});
			bl=false;
		}
		else if(!(/^1\d{10}$/.test(mobile))){ 
			$("#regform input[name='mobile']").css("border","red 1px solid");
			$.dialog.tips("请输入正确的手机号码！",2,'error.gif',function(){
				$("#regform input[name='mobile']").focus();
			});
			bl=false;
		}
		else{
			$("#regform input[name='mobile']").css("border","#ccc 1px solid");
		}
		if(username==""){
			$("#regform input[name='username']").css("border","red 1px solid");
			$.dialog.tips("姓名不能为空！",2,'error.gif',function(){
				$("#regform input[name='username']").focus();
			});
			bl=false;
		}
		else{
			$("#regform input[name='username']").css("border","#ccc 1px solid");
		}
		if(!bl||$(this).hasClass("disabled")){
			return
		}
		$(this).addClass("disabled");
		$.post(
				window.allBasePath+"wx/clientinfo_doRegVipAjax.do",
				{name:username,mobile:mobile,openId:window.openId},
				function(data){
					$(this).removeClass("disabled");
					$.dialog.tips("注册成功！",2,'success.gif',function(){
						location.reload();
					});
				},"json");
	}
});