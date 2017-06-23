var ans = "",// 答案
   type = getParam("type"),
 openid = getParam("openid");

//var timer = setInterval(Tshow, 1000);
var seconds = 0, minutes = 0, hour = 0, score = "";
var url = "http://ikanav.duapp.com/fzh/crzguess";
//var url = "http://localhost:8080/fzh/crzguess";
if(type == 7){ document.title = "fzhwx开发者-疯狂成语"; }

/**
 * 初始化
 */
$(function(){
	getAnswer();
});

/**
 * 加载数据
 */
function getAnswer() {
	$.post(url + "/getAnswer", 
	{
		userid : openid,
		type : type
	}, function(data) {
		showdata(data);
	}, "json");
}

/**
 * 保存游戏
 */
function saveRecord(){
	$.post(url + '/saveRecord',
	{
		userid : openid,
		type : type
	}, function(data) {
		if(data == false){
			alert("保存游戏失败，请稍后再试！");
		} else {
			//保存成功，继续下一关
			getAnswer();
		}
	}, "json");
}

/**
 * 展示数据
 * @param data
 */
function showdata(data){
	ans = data.answer;
	$(".level").html(data.ids);
	$('.answerbox').attr("style", "margin-left:20%;");
	$(".catalogue").html("第一个字是：" + ans.substring(0, 1));
	$(".question-pic img").attr("src", data.picUrl);

	var arr = data.randtxt.match(/./g);// 字符串转数组

	// 加载答案
	for(var k = 1; k <= ans.length; k++) {
		$('.answerbox ul').append(
				'<li><a id="ans' + k + '" href="javascript:unslt(' + k
						+ ');"></a></li>');
	}
	// 加载18个选项
	for(var z = 0; z < arr.length; z++) {
		$('.keyboard ul').append(
				'<li><a id="answers' + z + '" href="javascript:slt(' + z
						+ ');">' + arr[z] + '</a></li>');
	}
}

/**
 * 选择答案
 * @param id
 */
function slt(id) {
	var arr = $("#sltans").find("li");
	var size = arr.size();
	var answers = $("#answers" + id).html();
	// 赋值
	for(var i = 1; i <= size; i++) {
		var atxt = $("#ans" + i).html();
		if (atxt == "") {
			$("#ans" + i).html(answers);
			break;
		} else {
			if (i > 1 && i == size) {
				$("#ans" + i).html(answers);
			}
		}
	}
	// 取值
	var str = "";
	for ( var i = 1; i <= size; i++) {
		var atxt = $("#ans" + i).html();
		str += atxt;
	}
	if (str == ans) {
		$("#sltans").find("a").css("background", "green");
		setTimeout(function() {
			//clearInterval(timer);//暂停计时
			$('.keyboard ul').html("");
			$('.answerbox ul').html("");
			$("#sltans").find("a").css("background", "gray");
			saveRecord();
		}, 300);
		//提示关注
		/*if(num == 5 && getParam("from") != null && getParam("from")!=""){
			if(confirm("亲，关注【fzhwx开发者】可以获得更多好玩功能哦!")==true){
				location.href = "http://mp.weixin.qq.com/s?__biz=MjM5MjM3OTY1Mw==&mid=200137399&idx=1&sn=df847d7c79a06d2b27628778d008ab8b#rd";//引导关注页面
			}
		}*/
	}
	if (str != ans && str.length == size) {
		$("#sltans").find("a").css("background", "red");
	}
}

/**
 * 取消答案
 * @param id
 */
function unslt(id) {
	$("#ans" + id).html("");
	$("#sltans").find("a").css("background", "gray");
}

/**
 * 获取URL参数
 * @param name
 * @returns
 */
function getParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

/**
 * 动态时钟
 */
function Tshow() {
	seconds ++;
	if (seconds == 60) {
		minutes ++;
		seconds = 0;
	}
	if (minutes == 60) {
		hour ++;
	}
	score = minutes + seconds;
	document.getElementById('time').innerHTML = minutes + "' " + seconds + '"';
}