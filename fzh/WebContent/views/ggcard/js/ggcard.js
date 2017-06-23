$(function(){
	var iii=0;
	//优惠券活动封面设置
	$("#pic").omFileUpload({
		action:"image.upload",
		fileExt:"*.jpg;*.png;*.gif;*.jpeg;*.bmp",
		fileDesc:"Image Files",
		fileDataName:"myFile",
		sizeLimit:200*1024,
		onError:function(ID,fileObj,errorObj,event){
			if(errorObj.type=="File Size"){
				alert("上传图片的大小不能超过200KB")
			}
		},
		multi:false,
		autoUpload:true,
		swf:window.allBasePath+"operamasks-ui-2.0/swf/om-fileupload.swf",
		method:"GET",
		actionData:{"pic_type":"ggcard",uid:$("#uid").val()},
		onComplete:function(ID,fileObj,response,data,event){
			iii++;
			var jsonData=eval("("+response+")");
			if(jsonData.error=="filetype"){
				alert("文件上传格式只能支持："+jsonData.allowtype);
				return false
			}
			$("#fengmianArea").html('<li class="items">'+
					'<img src="'+window.allBasePath + jsonData.fileUrl+'">'+
					'<input type="text" name="picurl" style="display:none" value="ggcard/'+jsonData.fileName+'" />'+
					'<a href="javascript:void(0);" onclick="$(this).parent().remove();" class="vb cover-del">删除</a>'+
					'</li>');
			$("#fengmianArea").show();
			$(".cover .i-img").attr("src",window.allBasePath + jsonData.fileUrl).show();
			$(".default-tip").hide();
		}
	});
					
	var validator=$("#ggcard-form").validate({
		rules:{
			title:{required:true,maxlength:32},
			content:{required:true,maxlength:2000},
			starttime:{required:true,date:true},
			endtime:{required:true,date:true},
			keyword:{required:true,maxlength:10},
			daynum:{required:true,number:true},
			maxnum:{required:true,number:true},
			exptime:{required:true,date:true}
		},
		messages:{
			title:{required:"请输入[标题]",maxlength:"[标题]不能超过32个字"},
			content:{required:"请输入[活动说明]",maxlength:"[活动说明]不能超过2000个字"},
			starttime:{required:"必须输入[开始时间]",date:"请输入正确的[开始时间]"},
			endtime:{required:"必须输入[结束时间]",date:"请输入正确的[结束时间]"},
			keyword:{required:"请输入[关键字]",maxlength:"[关键字]不能超过10个字"},
			daynum:{required:"请输入[用户每天可参与次数]",number:"[用户每天可参与次数]必须是数字"},
			maxnum:{required:"请输入[用户可参与总次数]",number:"[用户可参与总次数]必须是数字"},
			exptime:{required:"必须输入[兑换截止日期]",date:"请输入正确的[兑换截止日期]"}
		},
		showErrors:function(errorMap,errorList){
			if(errorList&&errorList.length>0){
				$.each(errorList,function(index,obj){
					var item=$(obj.element);
					item.css("border","red 1px solid");
					item.attr("title",obj.message);
					tips(obj.message);
				})
			}else{
				var item=$(this.currentElements);
				item.css("border","#CCC 1px solid");
				item.removeAttr("title");
			}
		},
		submitHandler:function(){
		 var bl=true;
		 //检查选项数量
		 if($("#fengmianArea>li").length<1){
			 $("#fengmianArea").parent().parent().parent().addClass("error");
			 $("#fengmianArea").parent().parent().parent().attr("title","必须设置封面图片");
			 tips("必须设置封面图片");
			 bl=false;
		 }
		 else{
			 $("#fengmianArea").parent().parent().parent().removeClass("error");
			 $("#fengmianArea").parent().parent().parent().removeAttr("title");
		 }
		 //判断时间选择框
		 if(1>0){
			 var starttime=parseInt($("#starttime").val().replace(/\-| |:/g,""));
			 var endtime=parseInt($("#endtime").val().replace(/\-| |:/g,""));
			 var exptime=parseInt($("#exptime").val().replace(/\-| |:/g,""));
			 var d = new Date();
			 var nowtime = parseInt(d.getFullYear()+""+(d.getMonth()+1)+""+d.getDate()+""+d.getHours()+""+d.getMinutes()+""+d.getSeconds());
			 if(endtime<=starttime){
				 $("#endtime").css("border","red 1px solid");
				 $("#endtime").attr("title","[结束时间]不能在[开始时间]之前");
				 tips("[结束时间]不能在[开始时间]之前");
				 bl=false;
			 }
			 else if(endtime<nowtime){
				 $("#endtime").css("border","red 1px solid");
				 $("#endtime").attr("title","[结束时间]不能在当前时间之前");
				 tips("[结束时间]不能在当前时间之前");
				 bl=false;
			 }
			 else if(endtime>=exptime){
				 $("#exptime").css("border","red 1px solid");
				 $("#exptime").attr("title","[兑换截止日期]不能在当前时间之前");
				 tips("[兑换截止日期]不能在[结束时间]之前");
				 bl=false;
			 }
			 else{
				 $("#endtime").css("border","#CCC 1px solid");
				 $("#endtime").removeAttr("title");
			 }
		 }
		 var mxdata=[];
		 if(1>0){
			 var rnames=$(".jp-table .rname");
			 var rcontents=$(".jp-table input[name='rcontent']");
			 var rnums=$(".jp-table input[name='rnum']");
			 var probabilitys=$(".jp-table input[name='probability']");
			 var temp;
			 for(var i=0;i<rnames.length;i++){
				 temp=rcontents[i];
				 if($(temp).val()==""){
					 $(temp).css("border","red 1px solid");
					 $(temp).attr("title","[奖品名称]不能为空");
					 tips("[奖品名称]不能为空");
					 bl=false;
				 }
				 else if($(temp).val().length>50){
					 $(temp).css("border","red 1px solid");
					 $(temp).attr("title","[奖品名称]不能超过50个字符");
					 tips("[奖品名称]不能超过50个字符");
					 bl=false;
				 }
				 else{
					 $(temp).css("border","#CCC 1px solid");
					 $(temp).removeAttr("title");
				 }
				 temp=rnums[i];
				 if($(temp).val()==""){
					 $(temp).css("border","red 1px solid");
					 $(temp).attr("title","[奖品数量]不能为空");
					 tips("[奖品数量]不能为空");
					 bl=false;
				 }
				 else if(!/^[1-9]{1}[0-9]*$/.test($(temp).val())){
					 $(temp).css("border","red 1px solid");
					 $(temp).attr("title","[奖品数量]请输入大于0的整数");
					 tips("[奖品数量]请输入大于0的整数");
					 bl=false;
				 }
				 else{
					 $(temp).css("border","#CCC 1px solid");
					 $(temp).removeAttr("title");
				 }
				 temp=probabilitys[i];
				 if($(temp).val()==""){
					 $(temp).css("border","red 1px solid");
					 $(temp).attr("title","[中奖概率]不能为空");
					 tips("[中奖概率]不能为空");
					 bl=false;
				 }
				 else if(!/^0.[0-9]+$/.test($(temp).val())){
					 $(temp).css("border","red 1px solid");
					 $(temp).attr("title","[中奖概率]请输入大于0且小于1的小数");
					 tips("[中奖概率]请输入大于0且小于1的小数");
					 bl=false;
				 }
				 else{
					 $(temp).css("border","#CCC 1px solid");
					 $(temp).removeAttr("title");
				 }
				 mxdata[i]='{"rname":"'+rnames[i].innerHTML+'","rcontent":"'+rcontents[i].value+'","rnum":"'+rnums[i].value+'","probability":"'+probabilitys[i].value+'"}';
			 }
		 }
		 if(!bl)
			 return;
		var $form=$("#ggcard-form");
		var $btn=$("#save-btn");
		
		if($btn.hasClass("disabled")){
			return
		}
		
		var submitData={
			"vo.title":$("input[name='title']",$form).val(),
			"vo.picurl":$("input[name='picurl']",$form).val(),
			"vo.content":$("textarea[name='content']",$form).val(),
			"vo.starttime":$("input[name='starttime']",$form).val(),
			"vo.endtime":$("input[name='endtime']",$form).val(),
			"vo.exptime":$("input[name='exptime']",$form).val(),
			"vo.keyword":$("input[name='keyword']",$form).val(),
			"vo.daynum":$("input[name='daynum']",$form).val(),
			"vo.maxnum":$("input[name='maxnum']",$form).val(),
			"vo.researchfk":$("input[name='researchfk']",$form).val(),
			"vo.status2":1,
			"vo.isshownum":$("input[name='isshownum']",$form).attr("checked")=="checked"?1:0,
			"mxdata":"["+mxdata+"]"
		};
		$btn.addClass("disabled");
		//添加和修改操作 成功或者失败 提示信息区分
		var tipsstr="添加";
		if($("input[name='id']",$form).val()!=""){
			submitData["vo.id"]=$("input[name='id']",$form).val();
			submitData["vo.status"]=0;
			tipsstr="修改";
		}
		$.post(
			window.allBasePath+"wx/ggcard_doNew.do",
			submitData,
			function(data){
				$btn.removeClass("disabled");
				if(data.success=="true"){
					$.dialog.tips(tipsstr+'成功！',2,'success.gif',function(){
						location.href=window.allBasePath+"wx/ggcard_doList.do";
					});
				}else{
					$.dialog.tips(tipsstr+'失败！'+data.tips,2,'error.gif',function(){
						$btn.removeClass("disabled");
					});
				}
			},
			"json");
		return false;
	}
	});
	function tips(msg){
		$.dialog({
		    id: 'err-msg',
		    title: false,
		    content: msg, 
		    left: '100%',
		    top: '100%',
		    fixed: true,
		    drag: false,
		    resize: false,
		    time:2
		});
	}
	$('#starttime').datetimepicker({
	    showSecond: true,
	    timeFormat: 'hh:mm:ss'
    });
	$('#endtime').datetimepicker({
	    showSecond: true,
	    timeFormat: 'hh:mm:ss'
    });
	$('#exptime').datetimepicker({
		showSecond: true,
		timeFormat: 'hh:mm:ss'
	});
	//取消按钮对应js事件
	$("#cancel-btn").click(function(event){
		event.stopPropagation();
		location.href=window.allBasePath+"wx/ggcard_doList.do";
		return;
	});
	//绑定title文本框事件
	$(".msg-editer #title").bind("keyup",function(){
		$(".i-title").text($(this).val())
	});
	//绑定活动说明事件
	$("#content").keyup(function(){
		$(".msg-text").html($("#content").val());
		$("#contenttips").html($(this).val().length);
		if(this.scrollHeight>$(this).height()&&this.scrollHeight-$(this).height()!=8)
			$(this).height(this.scrollHeight);
	});
	
	//新增一行点击事件
	$("#addone").click(function(){
		var str=new Array("一等奖","二等奖","三等奖","四等奖","五等奖","六等奖","七等奖","八等奖","九等奖","十等奖");
		var trs=$(".jp-table>tbody>tr");
		if(trs.length>9){
			$.dialog.tips('最多只能设置10个奖项！',2);
			return;
		}
		$(".jp-table>tbody").append('<tr><td class="rname">'+str[trs.length]+'</td><td><input type="text" name="rcontent"></td><td><input type="text" name="rnum"></td><td><input type="text" name="probability"></td></tr>');
	});
	
	//删除一行点击事件
	$("#removeone").click(function(){
		var trs=$(".jp-table>tbody>tr");
		if(trs.length<1){
			$.dialog.tips('最少需要设置1个奖项！',2);
			return;
		}
		$(".jp-table>tbody>tr").last().remove();
	});
}
);