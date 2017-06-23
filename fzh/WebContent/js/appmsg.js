$( function() {
	//文件上传
	$("#file_upload").omFileUpload({
		action : window.allBasePath + "image.upload",
		fileExt :"*.jpg;*.png;*.gif;*.jpeg;*.bmp",
		fileDesc :"Image Files",
		fileDataName :"myFile",
		sizeLimit :200 * 1024,
		onError : function(ID, fileObj, errorObj, event) {
			if (errorObj.type == "File Size") {
				alert("上传图片的大小不能超过200KB！");
				return;
			}
		},
		autoUpload :true,
		swf :window.allBasePath + "operamasks-ui-2.0/swf/om-fileupload.swf",
		method :"GET",
		actionData : {
			uid :$("#uid").val()
		},
		onComplete : function(ID, fileObj, response, data, event) {
			var jsonData = JSON.parse(response);
			if(jsonData.error == "fileType"){
				alert("上传图片的格式只支持：jpg、jpeg、png、bmp！");
				return;
			}
			$(".cover .i-img").attr("src", window.allBasePath + jsonData.fileUrl).show();
			$("#imgArea").show().find(" #img").attr("src", window.allBasePath + jsonData.fileUrl);
			$("input[name='imageUrl']").val(jsonData.fileName);
			$(".default-tip").hide();
		}
	});

	$(".msg-editer #title").bind("keyup", function() {
		$(".i-title").text($(this).val());
	});

	$(".msg-editer #digest").bind("keyup", function() {
		$(".msg-text").html($.htmlEncode($(this).val()));
	});

	$("#desc-block-link").click( function() {
		$("#desc-block").show();
		$(this).hide();
	});

	$("#url-block-link").click( function() {
		$("#url-block").show();
		$(this).hide();
	});

	$("#chain-block-link").click( function() {
		$("#chain-block").show();
		$(this).hide();
	});

	$("#delImg").click( function() {
		$(".default-tip").show();
		$("#imgArea").hide();
		$("#imageUrl").val("");
		$(".cover .i-img").hide();
	});

	$("#cancel-btn").click( function(event) {
		event.stopPropagation();
		location.href = window.allBasePath + "imagetextinfo/list";
		return;
	});
	
	$("#picType").change(function() {
		var type = $("#picType").val();
		if(type == 1){
			$("#picUrl").hide();
			$("#cover").show();
		} else {
			$("#cover").hide();
			$("#picUrl").show();
		}
	});

	$("input[name='picUrl']").blur(function() {
		$(".cover .i-img").attr("src", $("input[name='picUrl']").val()+"?"+Math.random()).show();
	});
	
	var validator = $("#appmsg-form").validate({
		rules : {
			title : {
				required : true,
				maxlength :64
			},
			digest : {
				maxlength :120
			},
			sourceUrl : {
				url :true
			},
			clickOutUrl : {
				url :true
			}
		},

		messages : {
			title : {
				required :"请输入标题",
				maxlength :"标题不能超过64个字"
			},
			digest : {
				maxlength :"标题不能超过120个字"
			},
			sourceUrl : {
				url :"必须输入正确的url格式"
			},
			clickOutUrl : {
				url :"必须输入正确的url格式"
			}
		},

		showErrors : function(errorMap, errorList) {
			if (errorList && errorList.length > 0) {
				$.each(errorList, function(index, obj) {
					var item = $(obj.element);
					item.closest(".control-group").addClass("error");
					item.attr("title", obj.message);
				});
			} else {
				var item = $(this.currentElements);
				item.closest(".control-group").removeClass("error");
				item.removeAttr("title");
			}
		},

		submitHandler : function() {
			if ($("#picType").val() == 1 && $("#imageUrl").val() == "") {
				alert("必须上传一张图片");
				return false;
			}

			var editorContent = msg_editor.getContent();
			var clickOutUrl = $("#clickOutUrl").val();
			var len = editorContent.length;
			if (clickOutUrl == "" && len == 0) {
				alert("正文内容或者外链至少要填写一个。");
				msg_editor.focus();
				return false;
			}

			if (len > charLimit) {
				alert("正文的内容不能超过" + charLimit + "个字");
				msg_editor.focus();
				return false;
			}

			var $form = $("#appmsg-form");
			var $btn = $("#save-btn");

			if ($btn.hasClass("disabled")) {
				return;
			}

			var submitData = {
				title : $("input[name='title']", $form).val(),
				digest : $("textarea[name='digest']", $form).val(),
				imageUrl : $("input[name='imageUrl']", $form).val(),
				sourceUrl : $("input[name='sourceUrl']", $form).val(),
				clickOutUrl : $("#clickOutUrl", $form).val(),
				imageTextNo : $("#imageTextNo", $form).val(),
				mainText : editorContent,
				picUrl : $("input[name='picUrl']", $form).val(),
				flag : $("#flag", $form).val() // 标识新增或修改
			};

			$btn.addClass("disabled");

			$.post(
				window.allBasePath + "imagetextinfo/saveOrUpdate",
				submitData, 
				function(data) {
					$btn.removeClass("disabled");
					if (data !=null && data == true) {
						location.href = window.allBasePath + "imagetextinfo/list";
					} else {
						alert("保存失败");
					}
				}, "json");
			return false;
		}
	});

	window.msg_editor = new UE.ui.Editor({
		initialFrameWidth : 530
	});
	window.msg_editor.render("editor");

	function computeChar() {
		var len = msg_editor.getContent().length;
		if (len > charLimit) {
			$(".editor-bottom-bar").html(
					"<span style='color:red;'>你输入的字符个数（" + len
							+ "）已经超出最大允许值！</span>");
		} else {
			$(".editor-bottom-bar").html(
					"当前已输入<span class='char_count'>" + len
							+ "</span>个字符, 您还可以输入<span class='char_remain'>"
							+ (charLimit - len) + "</span>个字符。");
		}
	}

	window.msg_editor.addListener("keyup", function(type, evt) {
		computeChar();
	});

});