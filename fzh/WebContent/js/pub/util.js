
function doPrint() {
	window.print();
}
function exit() {
	parent.close();
}

// Trim whitespace from left and right sides of s.
function trim(s) {
	return s.replace(/^\s*/, "").replace(/\s*$/, "");
}
function isDate(str) {
	var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
	var r = str.match(reg);
	if (r == null) {
		return false;
	} else {
		var d = new Date(r[1], r[3] - 1, r[4]);
		if (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4]) {
			return true;
		} else {
			return false;
		}
	}
}
function isTime(str) {
	var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
	var r = str.match(reg);
	if (r == null) {
		return false;
	} else {
		var d = new Date(r[1], r[3] - 1, r[4], r[5], r[6], r[7]);
		if (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4] && d.getHours() == r[5] && d.getMinutes() == r[6] && d.getSeconds() == r[7]) {
			return true;
		} else {
			return false;
		}
	}
}
function selectDate() {
	var arg = new Object();
	strTime = event.srcElement.value;
	var splits = strTime.split(".");
	strTime = splits[0];
	if (isDate(strTime) == false) {
		strTime = "";
	}
	arg.str_datetime = strTime;
	arg.time_comp = false;
	var rtn = window.showModalDialog(contextPath + "/js/pub/calendar.html", arg, "dialogWidth=210px;dialogHeight=240px;status:no;scroll=no;");
	return (rtn == null ? strTime : rtn);
}
function selectDatetime() {
	var arg = new Object();
	strTime = event.srcElement.value;
	var splits = strTime.split(".");
	strTime = splits[0];
	if (isTime(strTime) == false) {
		strTime = "";
	}
	arg.str_datetime = strTime;
	arg.time_comp = true;
	var rtn = window.showModalDialog(contextPath + "/js/pub/calendar.html", arg, "dialogWidth=210px;dialogHeight=240px;status:no;scroll=no;");
	return (rtn == null ? strTime : rtn);
}
function selectDateYYYYMMDD() {
	var arg = new Object();
	strTime = "";
	valTime = event.srcElement.value;
	if (isDateYYYYMMDD(valTime) == false) {
		strTime = "";
	} else {
		strTime = valTime.substring(0, 4) + "-" + valTime.substring(4, 6) + "-" + valTime.substring(6, 8);
	}
	arg.str_datetime = strTime;
	arg.time_comp = false;
	var rtn = window.showModalDialog("calendar.html", arg, "dialogWidth=210px;dialogHeight=240px;status:no;scroll=no;");
	if (rtn != null) {
		rtn = rtn.split("-")[0] + rtn.split("-")[1] + rtn.split("-")[2];
	}
	return (rtn == null ? valTime : rtn);
}
function isDateYYYYMMDD(str) {
	var reg = /^(\d{1,4})(\d{1,2})(\d{1,2})$/;
	var r = str.match(reg);
	if (r == null) {
		return false;
	} else {
		var d = new Date(r[1], r[2] - 1, r[3]);
		if (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[2] && d.getDate() == r[3]) {
			return true;
		} else {
			return false;
		}
	}
}
function selectTime(t) {
	var rtn = window.showModalDialog(contextPath + "/js/time.htm", t, "dialogWidth=260px;dialogHeight=50px;status:no;scroll=no;");
	return (rtn == null ? "" : rtn);
}






//set list default value
function setSelectValue(obj, val) {
	if (obj == null || obj.type != "select-one") {
		return;
	}
	for (var i = 0; i <= obj.options.length; i++) {
		if (obj.options[i].value == val) {
			obj.options[i].selected = true;
			break;
		} else {
			obj.options[i].selected = false;
		}
	}
}

// add param to url
function addParam(url, name, val) {
	if (url != null && name != null) {
		if (url.indexOf("?") == -1) {
      // exist param
			url = url + "?" + name + "=" + val;
		} else {
      // no param before
			url = url + "&" + name + "=" + val;
		}
	}
	return url;
}
function changedNum(origin, target) {
	var originObj = document.getElementsByName(origin)[0];
	var targetObj = document.getElementsByName(target)[0];
	var value = originObj.value;
	var name = originObj.name;
	if (value == "") {
		return;
	}
	var url = contextPath + "/commons/subscriber.do?" + name + "=" + value;
	var rv = window.showModalDialog(url, target, "dialogWidth=450px;dialogHeight=220px;status:no;scroll=no;");
	if (rv == undefined) {
		return;
	} else {
		if (rv == "-1") {
			return;
		} else {
			targetObj.value = rv;
		}
	}
}
function loadforiframe() {
	if (parent.document.all("loadframe") != null) {
		parent.document.all("loadframe").style.posHeight = document.body.scrollHeight;
	}
	if (parent.document.all("IFRM_MAIN") != null) {
		parent.document.all("IFRM_MAIN").style.posHeight = document.body.scrollHeight;
	}
}
function loadforiframe1() {
	if (parent.document.all("loadframe1") != null) {
		parent.document.all("loadframe1").style.posHeight = document.body.scrollHeight;
	}
	if (parent.document.all("IFRM_MAIN") != null) {
		parent.document.all("IFRM_MAIN").style.posHeight = document.body.scrollHeight;
	}
}
function importLN(obj) {
	createMSGBox();
	positionMSGBox(obj);
	giveMSGBox(obj);
}
function unimportLN() {
	deleteMSGBox();
}
function createMSGBox() {
	with (document) {
		if (getElementById("MSGBox") == null) {
			addl = document.createElement("<div style=\"padding-top:3px; padding-left:5px; padding-right:5px; background:#5088BE; position:absolute; color:#FFFFFF; left:0px; top:0px; z-index:100; filter:progid:DXImageTransform.Microsoft.Shadow(Strength=0, Direction=135, color=A1A1A1);\" id=\"MSGBox\"></div>");
			document.body.appendChild(addl);
		}
	}
}
function deleteMSGBox() {
	with (document) {
		if (getElementById("MSGBox") != null) {
			MSGBox.removeNode(true);
		}
	}
}
function giveMSGBox(obj) {
	with (document) {
		if (getElementById("MSGBox") != null) {
			MSGBox.innerHTML = obj.value.length;
		}
	}
}
function positionMSGBox(obj) {
	vSrc = obj;
	if (document.getElementById("MSGBox") != null) {
		h = vSrc.offsetHeight;
		w = vSrc.offsetWidth;
		l = vSrc.offsetLeft;
		t = vSrc.offsetTop + h + 5;
		vParent = vSrc.offsetParent;
		while (vParent.tagName.toUpperCase() != "BODY") {
			l += vParent.offsetLeft;
			t += vParent.offsetTop;
			vParent = vParent.offsetParent;
		}
	}
	document.getElementById("MSGBox").style.posLeft = l + w;
	document.getElementById("MSGBox").style.posTop = t - h - 4;
	document.getElementById("MSGBox").style.posHeight = h;
}
function insertionPoint() {
	var insertionM = event.srcElement.createTextRange();
	insertionM.collapse(false);
	insertionM.select();
}
function disabledButton() {
	event.srcElement.disabled = true;
}
function getAcctID(obj, num) {
	var isTrue = false;
	var strUrl;
	var arg = new Array();
	strUrl = contextPath + "/fee/woff/acct.do?CMD=ACCT";
	var hWnd = window.showModalDialog(strUrl, arg, "dialogWidth:600px; dialogHeight:390px; status:no;resizable:yes;");
	if (hWnd != null && hWnd != "") {
		var item = new Array();
		var kk = "", m = 0;
		for (var i = 0; i < hWnd.length; i++) {
			if (hWnd.charAt(i) != "|") {
				kk += hWnd.charAt(i);
			} else {
				item[m] = kk;
				kk = "";
				m++;
			}
		}
		eval("document.all." + obj + ".value = item[" + num + "]");
	}
}

function doSubscriberByNo(servnumber) {
	var strUrl;
	var arg = new Array();
	arg.mobile_no = trim(servnumber);
	if(arg.mobile_no != ""){
		strUrl =contextPath+"/commons/subscriber.do?CMD=SELECT&mobileno="+arg.mobile_no;
		var hWnd = window.showModalDialog(strUrl,arg,"dialogWidth:600px; dialogHeight:390px; status:no;resizable:yes;");
		if(hWnd != null && hWnd != ""){
			return hWnd;  
		}
		return "";	
	}
	return "-1";   
}

function zoomByValue(url,obj,value) {
	var strUrl;
	var arg = new Array();
	arg.value = trim(value);
	if(arg.mobile_no != ""){
		strUrl = contextPath + url + "&" + obj + "=" + arg.value;
		var hWnd = window.showModalDialog(strUrl,arg,"dialogWidth:600px; dialogHeight:390px; status:no;resizable:yes;");
		if(hWnd != null && hWnd != ""){
			return hWnd;  
		}
		return "";	
	}
	return "-1";   
}


function checkToList(){
	if(ev_check()){            
		var check = false;
		eval("check = doSearch()");                
		if(check) { 
	 		resetPage();
	        formList.submit();	
	    }		 
	}      
}

function code2name(url, obj1, index1, index2, nameonly) {
	if (nameonly == null || nameonly == "") {
		nameonly = false;
	}
	var obj2 = obj1 + "_2name";
	var strUrl;
	var arg = new Array();
	strUrl = contextPath + url;
	var hWnd = window.showModalDialog(strUrl, arg, "dialogWidth:600px; dialogHeight:390px; status:no;resizable:yes;");
	if (hWnd != null && hWnd != "") {
		var item = new Array();
		var buffstr = "", index = 0;
		for (var i = 0; i < hWnd.length; i++) {
			if (hWnd.charAt(i) != "|") {
				buffstr += hWnd.charAt(i);
			} else {
				item[index] = buffstr;
				buffstr = "";
				index++;
			}
		}
		eval("document.all." + obj1 + ".value = item[" + index1 + "]");
		if (nameonly) {
			eval("document.all." + obj2 + ".value = item[" + index2 + "]");
		} else {
			var codeandname = item[index1] + " " + item[index2];
			eval("document.all." + obj2 + ".value = codeandname");
		}
	}
}

// add by mys	
function getMoreCheck(definition, condition, property, dbFlag) {
	var property2 = property + "_morecheck";
	var code = eval("document.all." + property + ".value");
	var strUrl = contextPath + "/commons/morecheck.do?CMD=MORECHECK&definition=" + definition;
	strUrl = strUrl + "&condition=" + condition + "&code=" + code;
	strUrl = strUrl + "&dbFlag=" + dbFlag;
	var arg = new Array();
	var hWnd = window.showModalDialog(strUrl, arg, "dialogWidth:600px; dialogHeight:390px; status:no;resizable:yes;");
	if (hWnd != null) {
		if (hWnd == "") {
			eval("document.all('" + property + "').value =\"\" ");
			eval("document.all('" + property2 + "').value =\"\" ");
		} else {
			var codeAndName = hWnd.split("|");
			eval("document.all('" + property + "').value = codeAndName[0]");
			eval("document.all('" + property2 + "').value = codeAndName[1]");
		}
	}
}	

// add by mys 
function setExcelOutPage(url) {
	var arg = new Array();
	var actionUrl = formList.action;
	var ExcelUrl = contextPath + url;
	var setPageUrl = contextPath + "/commons/selectpage.jsp";
	var hWnd = window.showModalDialog(setPageUrl, arg, "dialogWidth:600px; dialogHeight:390px; status:no;resizable:yes;");
	if (hWnd != null && hWnd != "") {
		var infoPage = hWnd.split("|");
		eval("document.all('startindex').value = infoPage[0]");
		eval("document.all('endindex').value = infoPage[1]");
		formList.action = ExcelUrl;
		formList.submit();
		formList.action = actionUrl;
	}
}
function checkTheAll(FO, BO) {
	FO = "document." + FO;
	CO = FO + ".item" + BO;
	var sis = eval(FO + ".item_" + BO);
	var val = eval(FO + "." + BO);
	if (sis != null) {
		if (sis.length != null) {
			for (var i = 0; i < sis.length; i++) {
				var e = sis[i];
				if (e.type == "checkbox") {
					e.checked = eval(CO).checked;
				}
			}
			if (eval(CO).checked) {
				val.value = "*,";
			} else {
				val.value = "";
			}
		} else {
			var e = sis;
			if (e.type == "checkbox") {
				e.checked = eval(CO).checked;
			}
			if (eval(CO).checked) {
				val.value = "*,";
			} else {
				val.value = "";
			}
		}
	}
}
function checkTheOne(FO, BO) {
	FO = "document." + FO;
	CO = FO + ".item" + BO;
	var sis = eval(FO + ".item_" + BO);
	var val = eval(FO + "." + BO);
	var TB = TO = 0;
  //  alert(sis.length);
	val.value = "";
  //   alert(val.value);
	if (sis != null) {
		if (sis.length != null) {
			for (var i = 0; i < sis.length; i++) {
				var e = sis[i];
				if (e.type == "checkbox") {
					TB++;
					if (e.checked) {
						TO++;
						val.value = e.value + "," + val.value;
					}
				}
			}
          // alert(TO);
			if (TO == TB) {
				eval(CO).checked = true;
				val.value = "*,";
			} else {
				eval(CO).checked = false;
			}
		} else {
			var e = sis;
			if (e.type == "checkbox") {
				eval(CO).checked = e.checked;
				if (eval(CO).checked) {
					val.value = "*,";
				} else {
					val.value = "";
				}
			}
		}
	}
}
function excelout(url) {
	var msg = "\u662f\u5426\u8981\u5bfc\u51faExcel\u6587\u4ef6?";
	if (confirm(msg)) {
		formList.action = contextPath + url + "?CMD=EXCELOUT";
		formList.submit();
		formList.action = contextPath + url + "?CMD=LIST";
	}
}

function getitemvalue (str) {
	return document.getElementById(str).value;
}
function delitem (obj) {
	while (obj.tagName.toUpperCase() != "DIV") {
		obj = obj.parentNode;
	}
	obj.removeNode(true);
}
function readonlychange (obj) {
	if (obj.readOnly) {
		obj.readOnly = false;
		obj.style.border = "#999999 solid 1px";
	}else{
		obj.readOnly = true;
		obj.style.border = "#999999 solid 0px";
	}
}
//--------
function showtitle (obj,str) {
	sumstrwidth(str);
	megshowlocation(obj);
	brindmegshow(obj,str);
	printinmegshow(obj,str);
}
function brindmegshow (obj,str) {
if (document.all("megshow") == null){
	var addl;
	addl = document.createElement("<iframe id=\"megshow\" style=\"position:absolute; top:0px; left:0px; z-index:10; overflow:hidden; height:24px; width:"+s60width+"px; filter:progid:DXImageTransform.Microsoft.Shadow(Strength=0,Direction=135);\" frameborder=\"0\" scrolling=\"no\"></iframe>");
	add1 = document.body.appendChild(addl);
	add1.style.posLeft = mx+2;
	add1.style.posTop = my+obj.offsetHeight+4;
	if ((mx+2+add1.offsetWidth)>document.body.offsetWidth){
		mx2 = document.body.offsetWidth - add1.offsetWidth;
		add1.style.posLeft = mx2;
		}
	}
}
function sumstrwidth (str) {
	s60width = 0;
	for (i=0;i<str.length;i++) {
		var byteOfStr = str.charAt(i); 
		if(IsChinese(byteOfStr)){
			s60width += 12;
			}else{
			s60width += 10;
			}
		}
	s60width += 20;
	}
function closemegshow () {
document.body.removeChild(document.all("megshow"));
}
function printinmegshow (obj,str) {
window.frames["megshow"].document.write("<body style=\"cursor:default; background:#5289C1; margin:0px; padding:0px; padding-left:10px; padding-top:6px; border-style:none; overflow:auto; font-size:12px; width:100%; color:#fff;\" oncopy=\"return false;\" oncut=\"return false;\" onselectstart=\"return false;\" oncontextmenu=\"return false;\">"+str+"</body>");
}
var mx,my,s60width=0;
function megshowlocation (obj) {
vSrc = obj;
	h = vSrc.offsetHeight;
	w = vSrc.offsetWidth;
	l = vSrc.offsetLeft;
	t = vSrc.offsetTop;
	vParent = vSrc.offsetParent;
	while (vParent.tagName.toUpperCase() != "BODY")
		{
			l += vParent.offsetLeft;
			t += vParent.offsetTop;
			vParent = vParent.offsetParent;
		}
	mx = l;
	my = t;
}