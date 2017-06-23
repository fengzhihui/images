var setting = {
	view: {
		addHoverDom: addHoverDom,
		removeHoverDom: removeHoverDom,
		selectedMulti: false

	},
	edit: {
		enable: true,
		//显示编辑图标
		editNameSelectAll: false,
		//取消节点文本被选中
		showRemoveBtn: showRemoveBtn,
		showRenameBtn: showRenameBtn,
		renameTitle: "编辑",
		removeTitle: "删除"

	},
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "pid",
			rootPId: 0
		}
	},
	callback: {
		beforeDrag: beforeDrag,
		beforeEditName: beforeEditName,
		beforeRemove: beforeRemove,
		beforeRename: beforeRename,
		onRemove: onRemove,
		onRename: onRename,
		onClick: onClick
	}
};

var log, className = "dark";
var newCount = 1;
//节点显示图标
function addHoverDom(treeId, treeNode) {
	//alert("pId="+treeNode.pId+",tId="+treeNode.tId+",treeId="+treeId);
	if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;

	if (treeNode.pid == 0) {
		var sObj = $("#" + treeNode.tId + "_span");
		var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='添加' onfocus='this.blur();'></span>";
		sObj.after(addStr);
	}

	var btn = $("#addBtn_" + treeNode.tId);
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	if (btn) btn.bind("click",
	function() {
		//数据库中存在自定义菜单
		if (array.length > 1) {
			//查找最大节点的id
			for (var i = 0; i < array.length; i++) {
				if (array[i] != null) {
					var max = array[i].id;
					for (var x = 1; x < array.length; x++) {
						if (array[x] != null && array[x].id > max) {
							max = array[x].id;
						}
					}
					newCount = max - 99;
				}
			}
		}

		var sNodes = zTree.getNodesByParam("pid", treeNode.id, null);
		var dbcount = $('#dbc').val(); //数据库中存在的菜单节点数
		var curcount = $('#cur').val(); //当前菜单节点数
		if (dbcount == curcount) {
			//添加一级节点
			if (treeNode.pid == 0 && treeNode.id == 0) {
				if (sNodes.length <= 3) {
					var cur = $('#cur').val();
					$('#cur').val(parseInt(cur) + 1);
					zTree.addNodes(treeNode, {
						id: 100 + newCount,
						pid: treeNode.id,
						name: "node" + (newCount++),
						content: "",
						url: "",
						type: 1
						//"click": clkNode(treeNode.tId)
					});
					//----------------显示编辑区----------------begin
					var v = 99 + newCount;
					var nd = zTree.getNodeByParam("id", v, null);
					showInfo(nd);
					//----------------显示编辑区----------------end
					return;
				} else {
					alert("最多只能添加3个一级菜单!");
					return;
				}
			}
			//添加二级节点
			if (treeNode.pid == 0 && treeNode.id != 0) {
				if (sNodes.length < 5) {
					var cur = $('#cur').val();
					$('#cur').val(parseInt(cur) + 1);
					zTree.addNodes(treeNode, {
						id: 100 + newCount,
						pid: treeNode.id,
						name: "node" + (newCount++),
						content: "",
						url: "",
						type: 1
						//"click": clkNode(treeNode.tId)
					});
					//----------------显示编辑区----------------begin
					var v = 99 + newCount;
					var nd = zTree.getNodeByParam("id", v, null);
					showInfo(nd);
					//----------------显示编辑区----------------end
					return;
				} else {
					alert("最多只能添加5个二级菜单!");
					return;
				}
			}
		} else {
			alert('请保存当前节点');
			return false;
		}
		return false;
	});
}

function beforeDrag(treeId, treeNodes) {
	return false;
}

//删除节点事件
function beforeRemove(treeId, treeNode) {
	//删除一级节点只在其没有二级节点时
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	if (treeNode.pid == 0) {
		var sNodes = zTree.getNodesByParam("pid", treeNode.id, null);
		if (sNodes.length > 0) {
			alert("请确认该节点没有二级节点!");
			return false;
		}
	}
	className = (className === "dark" ? "": "dark");
	showLog("[ " + getTime() + " beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);

	zTree.selectNode(treeNode);
	return confirm("确认删除 节点 -- " + treeNode.name + " 吗？");
}

//删除节点
function onRemove(e, treeId, treeNode) {
	$('#rndiv').hide();
	var treedata = $('#treedata').val();
	var cur = $('#cur').val();
	$('#cur').val(parseInt(cur) - 1);
	//treedata有保存节点才删除
	if (treedata.indexOf(treeNode.id) > -1) {
		var dbc = $('#dbc').val();
		$('#dbc').val(parseInt(dbc) - 1);
	}

	if (array.length == 2) {
		//删除全部节点，只剩下根节点
		$.post(path + '/menu/deleteAllNodes', null, null);
	}

	//重新组装treedata字串
	var nodeJson = "";
	for (var i = 0; i < array.length; i++) {
		if (array[i] != null) {
			if (array[i].id == treeNode.id) {
				if (array[i + 1] != null) {
					nodeJson += '{' + 'id:' + array[i + 1].id + ',pid:' + array[i + 1].pid + ',name:"' + array[i + 1].name + '",url:"' + array[i + 1].url + '",content:"' + array[i + 1].content + '",type:' + array[i + 1].type + '},';
				}
				array.remove(i);
			} else {
				nodeJson += '{' + 'id:' + array[i].id + ',pid:' + array[i].pid + ',name:"' + array[i].name + '",url:"' + array[i].url + '",content:"' + array[i].content + '",type:' + array[i].type + '},';
			}
		}
	}
	$('#treedata').val(nodeJson);
	//alert($('#treedata').val());
	showLog("[ " + getTime() + " onRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
}

//点击编辑图标弹出编辑区
function beforeEditName(treeId, treeNode) {
	showInfo(treeNode);
}

function beforeRename(treeId, treeNode, newName, isCancel) {
	className = (className === "dark" ? "": "dark");
	//菜单上编辑名称，同步右边菜单名称显示
	$('#mname').val(newName);

	showLog((isCancel ? "<span style='color:red'>": "") + "[ " + getTime() + " beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>": ""));
	if (newName.length == 0) {
		alert("菜单名称不能为空!");
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		setTimeout(function() {
			zTree.editName(treeNode);
		}, 10);
		return false;
	}
	return true;
}

//暂不使用
function onRename(e, treeId, treeNode, isCancel) {
	//菜单上编辑名称，同步右边菜单名称显示
	//$('#mname').val(treeNode.name);
	//showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" onRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
}

//显示删除图标
function showRemoveBtn(treeId, treeNode) {
	if (treeNode.id == 0) {
		return ! treeNode.isFirstNode; //第一个节点不显示删除图标
	}
	if (treeNode.id > 0) {
		return treeNode;
	}
}

//显示编辑图标
function showRenameBtn(treeId, treeNode) {
	if (treeNode.id == 0) {
		return ! treeNode.isFirstNode;
	}
	if (treeNode.id > 0) {
		return treeNode;
	}
}

//显示图标
function showUrlBtn(treeId, treeNode) {
	var addStr1 = "<span class='button ico_docu' style='background:url(../../css/zTreeStyle/img/diy/9.png) 0 0 no-repeat;' id='1' title='删除' onfocus='this.blur();'></span>";
	sObj.after(addStr1);
	return treeNode;
}

function showLog(str) {
	if (!log) log = $("#log");
	log.append("<li class='" + className + "'>" + str + "</li>");
	if (log.children("li").length > 8) {
		log.get(0).removeChild(log.children("li")[0]);
	}
}

function getTime() {
	var now = new Date(),
	h = now.getHours(),
	m = now.getMinutes(),
	s = now.getSeconds(),
	ms = now.getMilliseconds();
	return (h + ":" + m + ":" + s + " " + ms);
}

function removeHoverDom(treeId, treeNode) {
	$("#addBtn_" + treeNode.tId).unbind().remove();
};

function selectAll() {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.setting.edit.editNameSelectAll = $("#selectAll").attr("checked");
}

//保存整个树菜单
function saveMenu() {
	var trdata = $("#treedata").val();
	if (trdata != null && trdata.length > 54) {
		document.menuform.action = path + "/menu/saveMenu";
		document.menuform.submit();
		alert('保存成功!');
	} else {
		alert('请点击【自定义菜单列表】的【+】按钮进行编辑及保存！');
	}
}

function sltImg() {
	var url = path + '/wx/imagetextinfo_doSelectList.action';
	var attr = 'width=1000px,height=500px,status=no,scroll=yes,top=150px,left=200px';
	//弹窗改为window.open()解决谷歌浏览器不支持弹窗的特性
	window.open(url, window, attr);
}

//创建菜单
function createMenu() {
	//创建菜单前组装菜单数据
	getMenuJson(); //此方法赋值jsondata
	var jsondata = $("#jsondata").val();
	if (jsondata != null && jsondata.length > 13) {
		document.menuform.action = path + "/menu/createMenu";
		document.menuform.submit();
	} else {
		alert('请点击【自定义菜单列表】的【+】按钮进行编辑及保存！');
	}
}
//菜单发布提示
function showmsg() {
	var rs = $('#rs').val();
	if (rs != null && rs != "") {
		if (rs == "yes") {
			rs = "菜单发布成功！";
		} else if (rs == "no") {
			rs = "菜单发布失败,可能编辑菜单后没有点击【保存】或最后没有点击【确定】保存整个树菜单！";
		} else {
			rs = "未经授权！";
		}
		alert(rs);
	}
}

//点击节点
function onClick() {
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var sltNode = treeObj.getSelectedNodes();
	if (sltNode != null && sltNode.length > 0) {
		var treeNode = sltNode[0];
		//显示编辑区
		if (treeNode != null && treeNode.id != 0) showInfo(treeNode);
	}
}

//显示编辑区
function showInfo(treeNode) {
	//一级包含二级，则此菜单不显示编辑区
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	if (treeNode.pid == 0) {
		var sNodes = zTree.getNodesByParam("pid", treeNode.id, null);
		if (sNodes.length > 0) {
			return false;
		}
	}
	
	$('#rndiv').show();
	//编辑区赋值
	$('#mname').val(treeNode.name);
	$('#mname').focus();
	$('#tid').val(treeNode.id); //保存id到隐藏域
	$('#trid').val(treeNode.tId); //保存tId到隐藏域
	var nodeType = treeNode.type;
	var slt = $("#type").get(0);
	var val = slt.options[nodeType - 1].value;
	if (val != null) {
		slt[val - 1].selected = true;
	}
	if (nodeType == 1) { //文本
		$('#url').val("");
		if (treeNode.content == "") {
			$('#content').val("");
		} else {
			$('#content').val(treeNode.content);
		}
		$('#cont').show();
		$('#pic').hide();
		$('#lnk').hide();
	} else if (nodeType == 2) { //图文
		$('#pic').show();
		showImgTxtHtml(treeNode.id);
		$('#preViewDiv').show();
		$('#preViewDiv').addClass("divimg");
		$('#cont').hide();
		$('#lnk').hide();
	} else if (nodeType == 3) { //链接
		if (treeNode.url == "") {
			$('#url').val("");
		} else {
			$('#url').val(treeNode.url);
		}
		$('#lnk').show();
		$('#cont').hide();
		$('#pic').hide();
	}
}

//显示菜单类型
function showdiv(obj) {
	var tid = $('#trid').val();
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var treeNode = treeObj.getNodeByTId(tid);
	var val = obj.value;
	if (val == 1) { //文本
		if (treeNode.type == 1) {
			$('#content').val(treeNode.content);
		} else {
			$('#content').val('');
		}
		$('#cont').show();
		$('#pic').hide();
		$('#lnk').hide();
	} else if (val == 2) { //图文
		var treedata = $('#treedata').val();
		$('#pic').show();
		if (treedata.indexOf(treeNode.id) > -1 && treeNode.type == 2) {
			showImgTxtHtml(treeNode.id); //查询图片html
		} else {
			$('#url').val("");
			$('#preViewDiv').html('');
			$('#content').val("");
		}
		$('#preViewDiv').show();
		$('#preViewDiv').addClass("divimg");
		$('#cont').hide();
		$('#lnk').hide();
	} else if (val == 3) { //链接
		if (treeNode.type == 3) {
			$('#url').val(treeNode.url);
		} else {
			$('#content').val("");
		}
		$('#lnk').show();
		$('#cont').hide();
		$('#pic').hide();
	}
}

//保存节点
function saveNode() {
	var rid = $('#rid').val();
	var imghtml = $('#preViewDiv').html();
	var tid = $('#tid').val();
	var url = $('#url').val();
	var type = $('#type').val();
	var name = $('#mname').val();
	var cont = $('#content').val();
	//节点对应图文显示html
	if (imghtml != null && imghtml != "") {
		$("#duiying").val($("#duiying").val() + tid + "$" + imghtml + "$");
	}
	$('#imghtml').val(imghtml);
	if (name == "") {
		alert('菜单名称不能为空!');
		return false;
	} else {
		if (name.indexOf("\"") > -1 || name.indexOf("\“") > -1 || name.indexOf("\”") > -1) {
			alert('不能包含双引号');
			return false;
		}
		if (tid == 0 && name.length > 5) {
			alert('一级菜单名称不能超过5个汉字!');
			return false;
		} else if (tid != 0 && name.length > 8) {
			alert('二级菜单名称不能超过8个汉字!');
			return false;
		}
	}
	if (url != null && url != "" && url.indexOf('http') < 0) {
		alert('链接必须以http开头!');
		return false;
	}
	if (url.indexOf("\"") > -1 || url.indexOf("\“") > -1 || url.indexOf("\”") > -1) {
		alert('不能包含双引号');
		return false;
	}
	if (cont.indexOf("\"") > -1 || cont.indexOf("\“") > -1 || cont.indexOf("\”") > -1) {
		alert('不能包含双引号');
		return false;
	}

	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var nd = treeObj.getNodeByParam("id", tid, null); //根据节点id查找
	//节点赋值
	nd.name = name;
	nd.type = type;
	if (type == 1) { //文本
		nd.content = cont;
	} else if (type == 2) { //图文
		nd.content = rid;
	} else if (type == 3) { //链接
		nd.url = url;
	}
	treeObj.updateNode(nd); //刷新节点
	//alert('name = '+nd.name);
	var newBean = new Bean();
	newBean.id = nd.id;
	newBean.pid = nd.pid;
	newBean.url = nd.url;
	newBean.name = nd.name;
	newBean.type = nd.type;
	newBean.content = nd.content;

	//所有节点的treedata
	var data = $('#treedata').val();
	var nodeJson = "";
	if (data.indexOf(tid) == -1) {
		//alert('是新节点');
		array.push(newBean);
		var cur = $('#dbc').val();
		$('#dbc').val(parseInt(cur) + 1);
	}
	for (var i = 0; i < array.length; i++) {
		if (array[i] != null) {
			if (array[i].id == newBean.id) { //已存在的节点则修改
				array[i] = newBean;
				nodeJson += '{' + 'id:' + newBean.id + ',pid:' + newBean.pid + ',name:"' + newBean.name + '",url:"' + newBean.url + '",content:"' + newBean.content + '",type:' + newBean.type + '},';
				//'",url:"' + newBean.url + '",content:"' + newBean.content + '",type:' + newBean.type + ',imghtml:"' + newBean.imghtml + '"},';
				//alert('修改当前节点信息--------'+ nodeJson);
			} else {
				nodeJson += '{' + 'id:' + array[i].id + ',pid:' + array[i].pid + ',name:"' + array[i].name +
				//'",url:"' + array[i].url + '",content:"' + array[i].content + '",type:' + array[i].type + ',imghtml:"' + array[i].imghtml +'"},';
				'",url:"' + array[i].url + '",content:"' + array[i].content + '",type:' + array[i].type + '},';
				//alert('拼接原来的节点信息------- '+ nodeJson);
			}
		}
	}
	nodeJson = nodeJson.substring(0, nodeJson.length - 1);
	$('#treedata').val(nodeJson); //保存用来显示菜单
	alert('保存菜单【' + newBean.name + '】成功!');
	//alert('treedata = '+$('#treedata').val());
}

//组装整个树菜单JSON数据
function getMenuJson() {
	//基本菜单格式，也就是一个节点的信息   node =  {"name":"短信笑话","type":"click","url":"","key":"13"}
	//只有一级没二级的菜单格式      {"name":"短信笑话","type":"click","url":"","key":"13"}
	//一级下有二级的菜单格式           {"name":"游戏娱乐","sub_button":[node1, node2,]}
	//最外层的格式      	{"button":[          ]}
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	//var alltreeObj = zTree.transformToArray(zTree.getNodes());
	var data = "",
	str = "";

	//查找一级节点
	var nodes = zTree.getNodesByParam("pid", "0", null);
	for (var j = 1; j < nodes.length; j++) {
		var name = nodes[j].name;
		var type = nodes[j].type;
		var url = nodes[j].url;
		var key = nodes[j].id;

		var id = nodes[j].id;
		//alert('id='+id);
		//查找二级节点集合
		var nds = zTree.getNodesByParam("pid", id, null);
		var nd2 = '';
		for (var k = 0; k < nds.length; k++) {
			if (nds[k] != null) {
				type = nds[k].type;
				if (type == 1 || type == 2) { //文本、图文
					type = 'click';
				} else if (type == 3) { //链接
					type = 'view';
				}
				nd2 += '{"name":"' + nds[k].name + '","type":"' + type + '","url":"' + nds[k].url + '","key":"' + nds[k].id + '"' + '},';
			}
		}
		//alert('nd2 ='+nd2);
		//一级下有二级
		if (nd2 != '') {
			str = '{"name":"' + name + '","sub_button":[' + nd2 + ']},';
		} else { //只有一级没有二级
			if (type == 1 || type == 2) { //文本、图文
				type = 'click';
			} else if (type == 3) { //链接
				type = 'view';
			}
			str = '{"name":"' + name + '","type":"' + type + '","url":"' + url + '","key":"' + key + '"' + '},';
		}
		data += str;
	}
	var json = '{"button":[' + data + ']}';
	//alert('json ='+json);
	$("#jsondata").val(json); //传到后台创建菜单
}

//删除数组
Array.prototype.remove = function(dx) {
	if (isNaN(dx) || dx > this.length) {
		return false;
	}
	for (var i = 0,
	n = 0; i < this.length; i++) {
		if (this[i] != this[dx]) {
			this[n++] = this[i];
		}
	}
	this.length -= 1;
};

function clkNode(tid) {
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var treeNode = treeObj.getNodeByTId(tid);
	alert('treeNode.name = ' + treeNode.name);
	//showdiv(treeNode);
}

//节点Bean数组
function Bean() {
	var id = {};
	var pid = {};
	var url = {};
	var name = {};
	var content = {};
}

//---------------菜单预览-------------------
/*	<ul id="preview_screen1" class="pre_nav_list">
			 <li id="preview_0" class="previewlevel1 pre_nav ">
			 <a href="javascript:;" class="pre_nav_btn">一见钟情</a>
			 <ul class="pre_sub_nav_list" style=""><span class="pre_sub_nav_arrow"></span>
			 <li class="previewlevel2 pre_sub_nav"><a href="javascript:;" class="pre_sub_nav_btn">一见如故</a></li>
			 <li class="previewlevel2 pre_sub_nav"><a href="javascript:;" class="pre_sub_nav_btn">一吻定情</a></li>
			 </ul>
			 </li>
			 <li id="preview_1" class="previewlevel1 pre_nav ">
			 <a href="javascript:;" class="pre_nav_btn">a</a>
			 </li>
			 <li id="preview_2" class="previewlevel1 pre_nav ">
			 <a href="javascript:;" class="pre_nav_btn">c</a>
			 </li>
			 </ul>*/
function previewMenu() {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	//查找一级节点
	var nodes = zTree.getNodesByParam("pid", "0", null);
	var html = "",
	ndstr1 = "";
	for (var j = 1; j < nodes.length; j++) {
		if (nodes[j] != null) {
			var id = nodes[j].id;
			var name = nodes[j].name; //一级菜单名称
			ndstr1 = '<li id="preview_' + j + '" class="previewlevel1 pre_nav ">' + '<a href="javascript:;" class="pre_nav_btn" onclick="show(' + j + ');">' + name + '</a></li>';
			//查找二级节点集合
			var nds = zTree.getNodesByParam("pid", id, null);
			var ndstr2 = "";
			var str2 = "";
			if (j == 2) {
				str2 = '<ul class="pre_sub_nav_list" id="show' + j + '" style="display:none;margin-left:90px;"><span class="pre_sub_nav_arrow"></span>';
			} else if (j == 3) {
				str2 = '<ul class="pre_sub_nav_list" id="show' + j + '" style="display:none;margin-left:150px;"><span class="pre_sub_nav_arrow"></span>';
			} else {
				str2 = '<ul class="pre_sub_nav_list" id="show' + j + '" style="display:none;"><span class="pre_sub_nav_arrow"></span>';
			}

			for (var k = 0; k < nds.length; k++) {
				if (nds[k] != null) {
					ndstr2 += '<li class="previewlevel2 pre_sub_nav" style="letter-spacing:0px;"><a href="javascript:;" class="pre_sub_nav_btn">' + nds[k].name + '</a></li>';
				}
			}
		}
		html += ndstr1 + str2 + ndstr2 + '</ul>';
	}
	html = '<ul id="preview_screen1" class="pre_nav_list">' + html + '</ul>';
	//alert(html);
	$('#pre_nav_wrapper').html(html);
	$('#preview_box').show();
}

//显示二级菜单
function show(id) {
	for (var i = 1; i < 4; i++) {
		if (i == id) {
			$('#show' + id).show();
		} else {
			$('#show' + i).hide();
		}
	}
}

//关闭预览
function clo() {
	$('#preview_box').hide();
}

//显示图文html
function showImgTxtHtml(id) {
	$.post(path + "/menu/findImgTxtHtml", {
		"menuId": id
	},
	function(data) {
		if (data != null && data != "") {
			$('#preViewDiv').html(data);
		}
	});
}