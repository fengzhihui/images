 /*************************************************
	Validator v1.0
*************************************************/
 Validator = {
	Require : /.+/,
	Email : /^\w+([-+.]\w+)*@\w+([-.]\\w+)*\.\w+([-.]\w+)*$/,
	Phone : /^1[3|4|5|8][0-9]\d{4,8}$/,
	Mobile : /^((\(\d{3}\))|(\d{3}\-))?13\d{9}$/,
	Url : /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/,
	IdCard : /^\d{15}(\d{2}[A-Za-z0-9])?$/,
	Currency : /^\d+(\.\d+)?$/,
	Number : /^\d+$/,
	Num:/^\\d+$/,
	NoTZeroNumber:/^[1-9]\d*$/,
	Zip : /^[1-9]\d{5,8}$/,
	QQ : /^[1-9]\d{4,8}$/,
	Integer : /^[-\+]?\d+$/,
	Double : /^[-\+]?\d+(\.\d+)?$/,
	English : /^[A-Za-z\_]+$/,
	Chinese :  /^[\u0391-\uFFE5]+$/,
	UnSafe : /^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/,
    IP : /^((1?\d?\d|(2([0-4]\d|5[0-5])))\.){3}(1?\d?\d|(2([0-4]\d|5[0-5])))$/, 
    IsPassword:/^[a-zA-Z][a-zA-Z0-9_]{5,15}$/,
    IsLoginName:/^[a-zA-Z][a-zA-Z0-9_]{5,19}$/,
    AccountId:/^[a-zA-Z0-9_]{4,16}$/,
    AccountPassword:/^[a-zA-Z0-9_]{8,16}$/,
	IsSafe : function(str){return !this.UnSafe.test(str);},
	SafeString : "this.IsSafe(value)",
	Limit : "this.limit(this.LenE(value),getAttribute('min'),  getAttribute('max'))",
	LimitB : "this.limit(this.LenB(value), getAttribute('min'), getAttribute('max'))",
	Date : "this.IsDate(value, getAttribute('min'), getAttribute('format'))",
	Repeat : "value == document.getElementsByName(getAttribute('to'))[0].value",
	Range : "getAttribute('min') < value && value < getAttribute('max')",
	Compare : "this.compare(value,getAttribute('operator'),getAttribute('to'))",
	Custom : "this.Exec(value, getAttribute('regexp'))",
	Group : "this.MustChecked(getAttribute('name'), getAttribute('min'), getAttribute('max'))",
	PhoneOrMobile:"this.Phone.test(value)||this.Mobile.test(value)",
	Password:"this.IsPassword.test(value)",
	LoginName:"this.IsLoginName.test(value)",
	ErrorItem : [document.forms[0]],
	ErrorMessage : [":\t\t\t\t"],
	Validate : function(theForm, mode, doc){
		this.ErrorMessage = ["\t\t\t\t"] ;
		var obj = theForm || event.srcElement;
		var count = obj.elements.length;
		this.ErrorMessage.length = 1;
		this.ErrorItem.length = 1;
		this.ErrorItem[0] = obj;
		//去除原来的验证消息
		$("img[id^='errorMsg_']").remove();
		
		
		for(var i=0;i<count;i++){
			with(obj.elements[i]){
				var _dataType = getAttribute("dataType");
				if(typeof(_dataType) == "object" || typeof(this[_dataType]) == "undefined")  continue;
				this.ClearState(obj.elements[i]);
				if(getAttribute("require") == "false" && value == "") continue;
				switch(_dataType){
					case "Date" :
					case "Repeat" :
					case "Range" :
					case "Compare" :
					case "Custom" :
					case "Group" : 
					case "Limit" :
					case "LimitB" :
					case "SafeString" :
					case "PhoneOrMobile":
					case "Password":
					case "LoginName":
					
						if(!eval(this[_dataType]))	{
							this.AddError(i, getAttribute("msg"));
						}
						break;
					default :
						if(!this[_dataType].test(value)){
							this.AddError(i, getAttribute("msg"));
						}
						break;
				}
			}
		}
	/*	if(this.ErrorMessage.length > 1){
			mode = mode || 1;
			var errCount = this.ErrorItem.length;
			switch(mode){
			case 2 :
				for(var i=1;i<errCount;i++)
					this.ErrorItem[i].style.color = "red";
			case 1 :
			    var message = "" ;
			    for (var i=0 ;i< this.ErrorMessage.length ; i++){
			        if (i != 0){
			            message+= this.ErrorMessage[i]+"<br/>" ;
			        }
			    }
				this.showMessage(message,doc,this.ErrorItem[1]);
				break;
			case 3 :
				for(var i=1;i<errCount;i++){
				try{
					var span = document.createElement("SPAN");
					span.id = "__ErrorMessagePanel";
					span.style.color = "red";
					this.ErrorItem[i].parentNode.appendChild(span);
					span.innerHTML = this.ErrorMessage[i].replace(/\d+:/,"*");
					}
					catch(e){alert(e.description);}
				}
				this.ErrorItem[1].focus();
				break;
			default :
				alert(this.ErrorMessage.join("\n"));
				break;
			}
			return false;
		}*/
		if(this.ErrorMessage.length > 1) return false;
		return true;
	},
	limit : function(len,min, max){
		min = min || 0;
		if(max==null){
		    max = Number.MAX_VALUE;
		}
		return min <= len && len <= max;
	},
	LenB : function(str){
		var textValue =str;
		textValue = textValue.replace(/\s/gi,"");
		if(textValue == ""){
			return 0;
		}
		return str.replace(/[^\x00-\xff]/g,"**").length;
	},
	LenE : function(str){
		var textValue =str;
		textValue = textValue.replace(/\s/gi,"");
		if(textValue == ""){
			return 0;
		}
		return str.length;
	},
	ClearState : function(elem){
		with(elem){
			if(style.color == "red")
				style.color = "";
			var lastNode = parentNode.childNodes[parentNode.childNodes.length-1];
			if(lastNode.id == "__ErrorMessagePanel")
				parentNode.removeChild(lastNode);
		}
	},
	AddError : function(index, str){
		var item=this.ErrorItem[0].elements[index];
		this.ErrorItem[this.ErrorItem.length] = item;
		var queryItem=$(item);
		var to=item;
		if(item.type=="hidden"||item.style.display=="none"){
			var ns=$(item).next("input:visible");
			if(ns.size()>0) to=ns.get(0);
		}
		$(to).after("<img id='errorMsg_"+index+"' src='/web/images/image/error_1.png' style='vertical-align: middle; cursor: pointer; margin-left: 3px;width:16px;height:16px;' title='"+str+"'/> ")
		
		this.ErrorMessage.length++;
		//this.ErrorMessage[this.ErrorMessage.length] = this.ErrorMessage.length + ":" + str;
		
	},
	Exec : function(op, reg){
		return new RegExp(reg,"g").test(op);
	},
	compare : function(op1,operator,op2){
		switch (operator) {
			case "NotEqual":
				return (op1 != op2);
			case "GreaterThan":
				return (op1 > op2);
			case "GreaterThanEqual":
				return (op1 >= op2);
			case "LessThan":
				return (op1 < op2);
			case "LessThanEqual":
				return (op1 <= op2);
			default:
				return (op1 == op2);            
		}
	},
	MustChecked : function(name, min, max){
		var groups = document.getElementsByName(name);
		var hasChecked = 0;
		min = min || 1;
		max = max || groups.length;
		for(var i=groups.length-1;i>=0;i--)
			if(groups[i].checked) hasChecked++;
		return min <= hasChecked && hasChecked <= max;
	},
	IsDate : function(op, formatString){
		formatString = formatString || "ymd";
		var m, year, month, day;
		switch(formatString){
			case "ymd" :
				m = op.match(new RegExp("^\\s*((\\d{4})|(\\d{2}))([-./])(\\d{1,2})\\4(\\d{1,2})\\s*$"));
				if(m == null ) return false;
				day = m[6];
				month = m[5]--;
				year =  (m[2].length == 4) ? m[2] : GetFullYear(parseInt(m[3], 10));
				break;
			case "dmy" :
				m = op.match(new RegExp("^\\s*(\\d{1,2})([-./])(\\d{1,2})\\2((\\d{4})|(\\d{2}))\\s*$"));
				if(m == null ) return false;
				day = m[1];
				month = m[3]--;
				year = (m[5].length == 4) ? m[5] : GetFullYear(parseInt(m[6], 10));
				break;
			default :
				break;
		}
		var date = new Date(year, month-1, day);
        return (typeof(date) == "object" && year == date.getFullYear() && month == (date.getMonth()+1) && day == date.getDate());
		function GetFullYear(y){return ((y<30 ? "20" : "19") + y)|0;}
	},

    showMessage: function (str, doc, errorObj) {
         if (parent.dispatch){
            doc = parent.dispatch.parent.document ;
        } else if (parent.parent.dispatch) {
            doc = parent.parent.dispatch.parent.document ;
        } else if (parent.parent.parent.dispatch) {
            doc = parent.parent.parent.dispatch.parent.document ;
        } else {
            doc = document ;
        }
        var sWidth,sHeight; 
        sWidth=doc.body.offsetWidth; 
        sHeight=screen.height; 
        
        var selects = document.getElementsByTagName("select") ;

        for (var i = 0; i < selects.length ; i++ ){
           selects[i].disabled = true ;
        }
        
        
        var bgObj=doc.createElement("div"); 
        bgObj.setAttribute('id','bgDiv'); 
        bgObj.style.background="#777"; 
        bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=1,opacity=25,finishOpacity=75"; 
        bgObj.style.opacity="0.6"; 
        bgObj.style.width=sWidth + "px"; 
        bgObj.style.height=sHeight + "px"; 
        doc.body.appendChild(bgObj); 
    
        var msgObj=doc.createElement("div") 
        msgObj.setAttribute("id","msgDiv"); 
        msgObj.style.background="white"; 
        msgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(startX=20, startY=20, finishX=100, finishY=100,style=1,opacity=100,finishOpacity=100);"; 
        msgObj.style.marginLeft = "-200px" ; 
        msgObj.style.marginTop = -75+document.documentElement.scrollTop+"px"; 
    
        var title=doc.createElement("h4"); 
        title.setAttribute("id","msgTitle"); 
        title.style.background="#D5E5D5"; 
        title.style.filter="progid:DXImageTransform.Microsoft.Alpha(startX=20, startY=20, finishX=100, finishY=100,style=1,opacity=100,finishOpacity=100);"; 
        title.style.opacity="0.15"; 
        title.style.finishOpacity="1.00"; 
        title.innerHTML = "  错误信息  ";
        doc.body.appendChild(msgObj); 
        doc.getElementById("msgDiv").appendChild(title); 
        var txt=doc.createElement("p"); 
        txt.style.margin="0" ; 
        txt.setAttribute("id","msgTxt"); 
        txt.style.marginLeft = "20" ;
        txt.innerHTML=str; 
        doc.getElementById("msgDiv").appendChild(txt); 
        var inButton = doc.createElement("span") ;
        inButton.setAttribute("id","bgbutton") ;
        inButton.style.filter="progid:DXImageTransform.Microsoft.Alpha(startX=20, startY=20, finishX=100, finishY=100,style=1,opacity=100,finishOpacity=100);";
        var inp = doc.createElement("input") ;
        inp.type="button" ;
        inp.value=" 关闭 " ;
        inp.onclick = function(){ 
            doc.body.removeChild(bgObj); 
            doc.getElementById("msgDiv").removeChild(title); 
            doc.body.removeChild(msgObj);
            
            for (var i = 0; i < selects.length ; i++ ){
               selects[i].disabled = false ;
            }
            if (errorObj) {
                errorObj.focus() ;
            }
        } ;
        inButton.appendChild(inp) ;
        inButton.appendChild(doc.createElement("br")) ;
        doc.getElementById("msgDiv").appendChild(inButton); 
    },
	
	appendAtt : function(note, vali){
        for (i in vali){
            note.setAttribute(i,vali[i]);
        }
	},
    removeAtt:function(note,vali){
		 for (i in vali){
	            note.removeAttribute(i);
	      }
		 note.style.color="";
	}
 }