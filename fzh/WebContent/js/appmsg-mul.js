$(function() {
	$("#file_upload").omFileUpload({
		action :"image.upload",
		fileExt :"*.jpg;*.png;*.gif;*.jpeg;*.bmp",
		fileDesc :"Image Files",
		sizeLimit :200 * 1024,
		onError : function(ID, fileObj, errorObj, event) {
			if (errorObj.type == "File Size") {
				alert("上传图片的大小不能超过200KB！");
				return;
			}
		},
		autoUpload :true,
		swf : window.allBasePath + "operamasks-ui-2.0/swf/om-fileupload.swf",
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

			$(".default-tip", window.appmsg).hide();
			$(".i-img", window.appmsg).attr("src", window.allBasePath + jsonData.fileUrl).show();
			$("#imgArea").show().find(" #img").attr("src", window.allBasePath + jsonData.fileUrl);
			$(".cover", window.appmsg).val(jsonData.fileName);
			$(".coverurl", window.appmsg).val(window.allBasePath + jsonData.fileUrl);
		}
	});

	$(".msg-editer #title").bind("keyup", function() {
		$(".i-title", window.appmsg).text($(this).val());

		$(".title", window.appmsg).val($(this).val());
	});

	$(".msg-editer #source_url").bind("keyup", function() {
		$(".sourceurl", window.appmsg).val($(this).val());
	});

	$(".msg-editer #chain").bind("keyup", function() {
		$(".chain", window.appmsg).val($(this).val());
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
		$(".default-tip", window.appmsg).show();
		$("#imgArea").hide();
		$(".cover,.coverurl", window.appmsg).val("");
		$(".i-img", window.appmsg).hide();
	});

	$("#cancel-btn").click( function(event) {
		event.stopPropagation();
		location.href = window.allBasePath + "imagetextinfo/list";
		return;
	});

	$("#appmsgItem1,.sub-msg-item").live( {
		mouseover : function() {
			$(this).addClass("sub-msg-opr-show");
		},
		mouseout : function() {
			$(this).removeClass("sub-msg-opr-show");
		}
	});

	$(".sub-add-btn").click( function() {
		var len = $(".sub-msg-item").size();
		if (len >= 7) {
			alert("最多只能加入8条图文信息");
			return;
		}
		var $lastItem = $(".sub-msg-item:last");
		var $newItem = $lastItem.clone();
		$("input,textarea", $newItem).val("");
		$(".i-title", $newItem).text("");
		$(".default-tip", $newItem).css("display", "block");
		$(".cover,.coverurl", $newItem).val("");
		$(".i-img", $newItem).hide();
		$(".rid", $newItem).remove();
		$lastItem.after($newItem);
	});

	$(".sub-msg-opr .edit-icon").live("click", function() {
		window.appmsg.find(".content").val(window.msg_editor.getContent());
		var $msgItem = $(this).closest(".appmsgItem");
		var index = $(".appmsgItem").index($msgItem);
		window.appmsgIndex = index;
		window.appmsg = $msgItem;
		$("#title").val($(".title", $msgItem).val());
		if ($(".coverurl", $msgItem).val() == "") {
			$("#imgArea").hide();
		} else {
			$("#imgArea").show().find("#img").attr("src", $(".coverurl", $msgItem).val());
		}

		if ($(".sourceurl", $msgItem).val() == "") {
			$("#url-block-link").show();
			$("#url-block").hide().find("#source_url").val("");
		} else {
			$("#url-block-link").hide();
			$("#url-block").show().find("#source_url").val($(".sourceurl", $msgItem).val());
		}

		if ($(".chain", $msgItem).val() == "") {
			$("#chain-block-link").show();
			$("#chain-block").hide().find("#chain").val("");
		} else {
			$("#chain-block-link").hide();
			$("#chain-block").show().find("#chain").val($(".chain", $msgItem).val());
		}

		window.msg_editor.setContent($(".content", $msgItem).val());
		computeChar();

		if (index == 0) {
			$(".msg-editer-wrapper").css("margin-top", "0px");
		} else {
			var top = 110 + $(".sub-msg-item").eq(0).outerHeight(true) * index;
			$(".msg-editer-wrapper").css("margin-top", top + "px");
		}
	});

	$(".sub-msg-opr .del-icon").live("click", function() {
		var len = $(".appmsgItem").size();
		if (len <= 2) {
			alert("无法删除，多条图文至少需要2条消息。");
			return;
		}
		if (confirm("确认删除此消息？")) {
			var $msgItem = $(this).closest(".sub-msg-item");
			if ($(".rid", $msgItem).size() > 0) {
				window.delResId.push($(".rid", $msgItem).val());
			}
			$msgItem.remove();
		}
	});

	window.appmsgIndex = 0;
	window.appmsg = $("#appmsgItem1");
	window.delResId = [];
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

	$("#save-btn").click(function() {
		var $btn = $(this);
		if ($btn.hasClass("disabled")) {
			return;
		}
		window.appmsg.find(".content").val(window.msg_editor.getContent());
		var valid = true;
		var $msgItem;
		var jsonData = [];
		$(".appmsgItem").each( function(index, msgItem) {
			$msgItem = $(msgItem);
			var title = $("input.title", $msgItem).val();
			var cover = $("input.cover", $msgItem).val();
			var content = $("textarea.content", $msgItem).val();
			var sourceurl = $("input.sourceurl", $msgItem).val();
			var chain = $("input.chain", $msgItem).val();
			if (title == "") {
				alert("标题不能为空");
				valid = false;
				return false;
			}
			if (cover == "") {
				alert("必须上传一个封面图片");
				valid = false;
				return false;
			}
			if (chain == "" && content == "") {
				alert("正文内容或者外链至少要填写一个。");
				valid = false;
				return false;
			}

			jsonData[index] = {
				title : title,
				imageUrl : cover,
				mainText : content,
				sourceUrl : sourceurl,
				clickOutUrl : chain
			};
			if ($(".rid", $msgItem).size() > 0) {
				jsonData[index].rid = $(".rid", $msgItem).val();
			} else {
				jsonData[index].rid = '';
			}
		});

		if (!valid) {
			$(".edit-icon", $msgItem).click();
			return false;
		}

		var sumbitData = {
			jsonData :$.toJSON(jsonData),
			action :$("#action").val()
		};

		if (window.delResId.length > 0) {
			sumbitData.delResId = $.toJSON(window.delResId);
		}

		$btn.addClass("disabled");

		$.post(window.allBasePath + "imagetextmore/saveOrUpdate", sumbitData, function(data) {
			$btn.removeClass("disabled");
			if (data != null && data == true) {
				location.href = window.allBasePath + "imagetextinfo/list";
			} else {
				alert("保存失败");
			}
		}, "json");
	});
});