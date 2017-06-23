

var fields = new Array();
var errorstr = new Array();
var errorobj = null;
errorstr[11] = msgIllegalFormat;
errorstr[12] = msgMonthOutRange;
errorstr[13] = msgStringOutRange;
errorstr[14] = msgInvalidNumberFormat;
errorstr[15] = msgNumberTooBig;
errorstr[16] = msgNumberTooSmall;
errorstr[17] = msgInputCorrectEmail;


errorstr[18] = msgInvalidDate;
errorstr[19] = msgInvalidTime;
errorstr[20] = msgInvalidTimeHHMM;
errorstr[22] = msgIntegerTooLong;
errorstr[23] = msgDecimalTooLong;
errorstr[24] = msgIntTooLong;
errorstr[25] = msIntMustBe;
errorstr[26] = msgInvalidDateYyyymmdd;

function isDateMaskYyyymmdd(str){
	var reg =  /^(\d{1,4})(\d{1,2})(\d{1,2})$/;
	var r = str.match(reg);
	if(r==null){
		alert(msgInvalidDateYyyymmdd);
		return false;
	}
	else{
		var d = new Date(r[1],r[2]-1,r[3]);
		if(d.getFullYear()==r[1]&&(d.getMonth()+1)==r[2]&&d.getDate()==r[3]){
			return true;
		}
		else{
			alert(msgInvalidDateYyyymmdd);
			return false;
		}   
	}
}

function num_format(element,errorMessage){
	var strNumber = eval("document.all."+element+".value");
	var strTemp = (parseInt(strNumber));
	if(strTemp != strNumber){
		if(errorMessage == null)errorMessage = "????????????";
		alert(errorMessage);
		fields = new Array();
		return true;
	}
	else{
		return false;
	}
}
function len_limit(element,len,errorMessage){
	var strInput = Trim(eval("document.all."+element+".value"));
	if (strInput.length != len){
		if(errorMessage == null)errorMessage = "????????????";
		fields = new Array();
		alert(errorMessage);
		eval("document.all."+element+".value") = strInput;
		return true;
	}
	else
	{
		return false;
	}
}
function date_compare(element1,element2,errorMessage) {
	var beginTime = eval("document.all."+element1+".value");
	var endTime = eval("document.all."+element2+".value");
	if(beginTime != "" && endTime != ""){
		var dt_begin = (beginTime.split("."))[0];
		var dt_end = (endTime.split("."))[0];
		if(dt_begin > dt_end) {
			if(errorMessage == null)errorMessage = "????????????";
			fields = new Array();
			alert(errorMessage);
			return true;
		}
		else{
			return false;
		}
	}
}

//----------------------------------------------------------------------------------------------------------------------
//  ckdoc??????????window????
//----------------------------------------------------------------------------------------------------------------------
function checkval(ckdoc,expectIds) //ckdoc must is window object
{
  // check fields var
  var i;
  var iscorrect = true;
  var isitemcorrect = 1;
  var elvalue = null;
  var alertstr = "";
  var expectIdString;
  if(!expectIds||expectIds.length==0) expectIdString="";
  else expectIdString="@"+expectIds.join("@")+"@";

  for(i=0;i<fields.length;i++)
  {
	if(expectIdString!=""&&expectIdString.indexOf("@"+i+"@")==-1) continue;//跳过不想检查的变量
    isitemcorrect = true;
    fields[i].val = "";

    if(fields[i] != null && fields[i].name != null)
    {
      if(ckdoc.document.all(fields[i].name) == null)
        continue;
      elvalue = Trim(ckdoc.document.all(fields[i].name).value);
    }
    else
      continue;

    if(elvalue == null || elvalue == "")
    {
      if(fields[i].defaultval != null && fields[i].defaultval != "")
        elvalue = ckdoc.document.all(fields[i].name).value = fields[i].defaultval;
      if(fields[i].isnull)
        continue;
      else
      {
        alertstr +=  '<span class=\'errorkey\'><span style=\'color:#F00; font-weight:900;\'>[' + fields[i].cnname + ']</span>:' + msgInputNotNull + '</span>';
        iscorrect = false;
        continue;
      }
    }
	ckdoc.document.all(fields[i].name).value = Trim(ckdoc.document.all(fields[i].name).value);
	//ckdoc.document.all(fields[i].name).value = Trim(ckdoc.document.all(fields[i].name).value);
    
    switch(fields[i].type)
    {
      case 'c'  :
        isitemcorrect = ischar(elvalue,fields[i].len1);
        break;
      case 'i'  :
        isitemcorrect = isint(elvalue,fields[i].len1);
        if(isitemcorrect == 1)
          isitemcorrect = compval(elvalue,fields[i]);
        break;
      case 'l'  :
        isitemcorrect = islong(elvalue,fields[i].len1);
        if(isitemcorrect == 1)
          isitemcorrect = compval(elvalue,fields[i]);
        break;
      case 'd'  :
        isitemcorrect = isdouble(elvalue,fields[i].len1,fields[i].len2);
        if(isitemcorrect == 1)
          isitemcorrect = compval(elvalue,fields[i]);
        break;
      case 'f'  :
        isitemcorrect = isfloat(elvalue,fields[i].len1,fields[i].len2);
        if(isitemcorrect == 1)
          isitemcorrect = compval(elvalue,fields[i]);
        break;
      case 't'  :
        isitemcorrect = validatedate(elvalue);
        break;
      case 'dt'  :
        isitemcorrect = validatedatetime(elvalue);
				break;
			case 'tt'  :
				isitemcorrect = validatetime(elvalue);
				break;
			case 'm'  :
				isitemcorrect = ismail(elvalue,fields[i].len1);
				break;
			case 'i&e':
				isitemcorrect = isie(elvalue,fields[i].len1);
				break;
			default	  :
				isitemcorrect = 0;
    }
    if(isitemcorrect != 1)
    {
		if(isitemcorrect == 13){
			alertstr += '<span class=\'errorkey\'><span style=\'color:#F00; font-weight:900;\'>[' + fields[i].cnname + ']</span>' + errorstr[isitemcorrect] + fields[i].len1 +msgPotinInfo + msgStringOutRangeInfo +'</span>';
		}
		else if(isitemcorrect == 15){
			alertstr += '<span class=\'errorkey\'><span style=\'color:#F00; font-weight:900;\'>[' + fields[i].cnname + ']</span>' + errorstr[isitemcorrect] + fields[i].maxval + '</span>';
		}
		else if(isitemcorrect == 16){
			alertstr += '<span class=\'errorkey\'><span style=\'color:#F00; font-weight:900;\'>[' + fields[i].cnname + ']</span>' + errorstr[isitemcorrect] + fields[i].minval + '</span>';
		}
		else if(isitemcorrect == 22){
			alertstr += '<span class=\'errorkey\'><span style=\'color:#F00; font-weight:900;\'>[' + fields[i].cnname + ']</span>' + errorstr[isitemcorrect] + fields[i].len1 +msgPotinInfo +'</span>';
		}
		else if(isitemcorrect == 23){
			alertstr += '<span class=\'errorkey\'><span style=\'color:#F00; font-weight:900;\'>[' + fields[i].cnname + ']</span>' + errorstr[isitemcorrect] + fields[i].len2 +msgPotinInfo +'</span>';
		}
		else if(isitemcorrect == 24){
			alertstr += '<span class=\'errorkey\'><span style=\'color:#F00; font-weight:900;\'>[' + fields[i].cnname + ']</span>' + errorstr[isitemcorrect] + fields[i].len1 +msgPotinInfo +'</span>';
		}
		else{
			alertstr += '<span class=\'errorkey\'><span style=\'color:#F00; font-weight:900;\'>[' + fields[i].cnname + ']</span>' + errorstr[isitemcorrect] + '</span>';
		}
      
      
      
      iscorrect = false;
    }
    else
      fields[i].val = elvalue;
  }
  if(!iscorrect)
    errorMessageShow(alertstr);

  fields = new Array();
  return iscorrect;
}
function errorMessageShow (str) {
	//alert(str);
	errorobj.innerHTML = "<div style=\"width:98%; Height:22px; padding-top:6px; text-align:left;\">"+str+"</div>";
	}
function addfield(name,cnname,type,isnull,len1,len2,defaultval,minval,maxval)
{
  var len = fields.length;
  fields[len] = new Object;
  fields[len].name = name;
  fields[len].cnname = cnname;
  fields[len].isnull = isnull;
  fields[len].type = type;  			//typs is ??'c','f','d','i','l','t','m', m is email  
  fields[len].len1= len1;   			//'c','i','t'??use len1,'f','d'use len1??len2
  fields[len].len2 = len2;
  //----------------------------------------------------------------------------------------------------------------------
  //????????????????????
  //----------------------------------------------------------------------------------------------------------------------
  fields[len].defaultval = defaultval;  //null,reserved
  fields[len].minval	= minval;		//only number
  fields[len].maxval	= maxval;		//only number
  return len;
}

function validatedate( strValue )
{
  //----------------------------------------------------------------------------------------------------------------------
  //strValue must be as:  yyyy/mm/dd or yyyy-mm-dd or yyyy.mm.dd
  //----------------------------------------------------------------------------------------------------------------------
  var objRegExp = /^\d{4}(\-|\/|\.)\d{1,2}(\-|\/|\.)\d{1,2}$/;
  if(!objRegExp.test(strValue))
  {
    return 18;
    //-????????????????????yyyy-mm-dd
  }
  else{
    var strSeparator = strValue.substring(4,5);
    var arrayDate = strValue.split(strSeparator); //split date into month, day, year
    //create a lookup for months not equal to Feb.
    var arrayLookup = { '01' : 31,'03' : 31, '04' : 30,'05' : 31,'06' : 30,'07' : 31,
        '08' : 31,'09' : 30,'10' : 31,'11' : 30,'12' : 31};
    var intDay = eval(arrayDate[2]);
    var intMonth = eval(arrayDate[1]);
    if(intMonth > 12 || intMonth <= 0) return 12;
    //-????????????12??????1
    if(arrayDate[1].length < 2) arrayDate[1] = '0' + arrayDate[1];

    //check if month value and day value agree
    if(arrayLookup[arrayDate[1]] != null ) {
      if(intDay <= arrayLookup[arrayDate[1]] && intDay != 0)
        return 1; //found in lookup table, good date
    }
    //check for February
    if(intMonth == 2)
    {
      var intYear = parseInt(arrayDate[0]);
      if( ((intYear % 4 == 0 && intDay <= 29) || (intYear % 4 != 0 && intDay <=28)) && intDay !=0)
        return 1;
    }

  }
  return 18;
  //-????????????????????yyyy-mm-dd
}

function validatetime( strValue )
{
  //----------------------------------------------------------------------------------------------------------------------
  //strValue must be as:  hh:mm
  //----------------------------------------------------------------------------------------------------------------------
  var arrayTime = strValue.split(":")
  if (arrayTime.length==3)
  {
    strHour=arrayTime[0];
    strMinute=arrayTime[1];
    strSecond=arrayTime[2];
    if ((parseInt(strHour, 10)>=0 && parseInt(strHour, 10)<=23) &&
        (parseInt(strMinute, 10)>=0 && parseInt(strMinute, 10)<=59) &&
        (parseInt(strSecond, 10)>=0 && parseInt(strSecond, 10)<=59))
     {
     	Reg = /^\d{1,2}:\d{1,2}:\d{1,2}$/
     	if(Reg.exec(strValue)){
     		return 1;
     	}
     }
   return 20;
    //-??????????????????????hh:mm
  }
  return 20;
   //-??????????????????????hh:mm
}


function validatedatetime( strValue )
{
  arrayValue = strValue.split(" ");
  if(arrayValue.length==2)
  {
    var rv = validatedate(arrayValue[0]);
    if(rv == 1)
      rv = validatetime(arrayValue[1]);
    return rv;
  }
  return 19;
  //-??????????????????????????yyyy-mm-dd hh:mm:ss
}

function isSpace(val) {
  return isEmpty(removeSpace(val));
}

function isEmpty(val) {
	if (val == "") {
    return true;
  } else {
    return false;
  }
}

function ischar(val,len)
{
  if(getUniCodeLength(val)<=len) return 1;
  return 13;
  //-??????????????
}



function isint(val,len)
{
  if(isNaN(val)) return 14;
  //-??????????
  if(val.indexOf(".")>0) return 25;
  //-??????????
  if(val.length<=len) return 1;
  return 24;
  //????????????????????????x??
}

function islong(val,len)
{
//----------------------------------------------------------------------------------------------------------------------
//  ??isint????
//----------------------------------------------------------------------------------------------------------------------
  return isint(val,len);
}

function isdouble(val,len1,len2)
{
	if(isNaN(val)){return 14;}
	//-??????????
	if(val.indexOf('.')==-1){
		if(val.length > len1){
		return 22;
		}
	}
	if(val.substring(0,val.indexOf(".")).length > len1)
	{
		return 22;
		//????????????????????????????x??
	}
	
	if(val.indexOf('.') >= 0 && (val.length) > (val.indexOf('.'))){
		if(val.substring(val.indexOf(".")).length-1 > len2){
		return 23;}
	}
	
	return 1;
  //????????????????????????????x??
}


function isfloat(val,len1,len2)
{
//----------------------------------------------------------------------------------------------------------------------
//  ??isfloat????
//----------------------------------------------------------------------------------------------------------------------
  return isdouble(val,len1,len2);
}

function ismail(val,len)
{
  var i=0 ;
  var slength=val.length;
  if (val.length>len)
    return 17;
    //-Email????????
  if(val.charAt(0)=="@")
    return 17;
    //-Email????????
  while((i<slength)&&(val.charAt(i)!="@"))
    i++;
  if (i>=slength)
    return 17;
    //-Email????????
  else i+=2;
  while((i<slength)&&(val.charAt(i)!="."))
    i++;
  if(i>=slength-1)
    return 17;
    //-Email????????
  return 1;
}

function compval(val1,val2)
{
  if(val2 == null || val2 == '')
    return 1;
  var val11 = parseFloat(val1);
  if(val2.maxval != null && val2.maxval != ''){
    if(val1.valueOf("number") - val2.maxval.valueOf("number") > 0){
    	alert(val1.valueOf("number") - val2.maxval.valueOf("number"));
		return 15;
		}
	}
      
  if(val2.minval != null && val2.minval != ''){
    if(val1.valueOf("number") - val2.minval.valueOf("number") < 0){
		return 16;
		}
	}
  return 1;
}

var regexpcn = /(%u[A-F0-9]{4})/gi;
var regexp = /(%[A-F0-9]{2})/gi;
var regval = null;
function getUniCodeLength(unicodeval)
{
  if(unicodeval == null) return 0;
  regval = escape(Trim(unicodeval));
  regval = regval.replace(regexpcn,"aa");
  regval = regval.replace(regexp,"a");
  return regval.length;
}

function LTrim(str)
{
	var whitespace = new String(" \t\n\r");
	var s = new String(str);
	if (whitespace.indexOf(s.charAt(0)) != -1){
	var j=0, i = s.length;
	while (j < i && whitespace.indexOf(s.charAt(j)) != -1)
	{
	j++;
	}
	s = s.substring(j, i);
	}
	return s;
}

function RTrim(str)
{
var whitespace = new String(" \t\n\r");
var s = new String(str);
if (whitespace.indexOf(s.charAt(s.length-1)) != -1)
{
var i = s.length - 1;
while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1)
{
i--;
}
s = s.substring(0, i+1);
}
return s;
}

function Trim(str)
{
return RTrim(LTrim(str));
}

//----------------------------------------------------------------------------------------------------------------------
//????EMAIL
//----------------------------------------------------------------------------------------------------------------------

function  checkmail(mail)
{
  var  strr;
  re=/(\w+@\w+\.\w+)(\.{0,1}\w*)(\.{0,1}\w*)/i;
  re.exec(mail);
  if  (RegExp.$3!=""&&RegExp.$3!="."&&RegExp.$2!=".")  strr=RegExp.$1+RegExp.$2+RegExp.$3
    else
      if  (RegExp.$2!=""&&RegExp.$2!=".")  strr=RegExp.$1+RegExp.$2
        else    strr=RegExp.$1
          if  (strr!=mail)  {alert(msgInputCorrectEmail);return false}
  return  true;
}

//----------------------------------------------------------------------------------------------------------------------
//????????????????equalsDateTime(strStart,strEnd,format)

//????dt:??????????????
//????fm:????????????
//????type:????????,????:????,1:min
//??????????????????????????????????????????????????
//----------------------------------------------------------------------------------------------------------------------
function checkDateByMask(dt,fm,type){

//----------------------------------------------------------------------------------------------------------------------
//1.??????????????
//----------------------------------------------------------------------------------------------------------------------
    var N=11;
    var format=new Array(N);
    format[0]="yyyy/MM/dd";
    format[1]="yyyy-MM-dd";
    format[2]="yyyyMMdd";
    format[3]="yy-MM-dd";

    format[4]="yyyy/MM/dd hh:mm:ss";
    format[5]="yyyy-MM-dd hh:mm:ss";
    format[6]="yy/MM/dd hh:mm:ss";
    format[7]="yy-MM-dd hh:mm:ss";

    format[8]="hh:mm:ss";
    format[9]="hh:mm";
    format[10]="yyyyMM";

//----------------------------------------------------------------------------------------------------------------------
//2.????????????????
//----------------------------------------------------------------------------------------------------------------------
    var b=false;
    for(var i=0;i<N;i++){
         if(format[i].toLowerCase()==fm.toLowerCase()){
                b=true;break;
         }
    }
    if(!b){
         return false;
    }

//----------------------------------------------------------------------------------------------------------------------
//3.????????
//----------------------------------------------------------------------------------------------------------------------

 //dt=processformat(dt,fm);

//----------------------------------------------------------------------------------------------------------------------
//4.????????????????????????????????
//----------------------------------------------------------------------------------------------------------------------
    if(dt.length!=fm.length){
         return false;
    }
    else{
         var dt1=dt.replace(/[0-9]/g,"%d");
         var dt2=fm.replace(/[ymdhs]/gi,"%d");
         if(dt1!=dt2){
               return false;
         }
    }

//----------------------------------------------------------------------------------------------------------------------
//5.????????????????????
//----------------------------------------------------------------------------------------------------------------------
  try{
     fm=fm.replace(/Y/g,"y").replace(/D/g,"d");
     var iyyyy=fm.indexOf("yyyy");
     var iyy=fm.indexOf("yy");
     var imm=fm.indexOf("MM");
     var idd=fm.indexOf("dd");
     var ihh=fm.indexOf("hh");
     var imi=fm.indexOf("mm");
     var iss=fm.indexOf("ss");

     var newdt=new Date();
     newdt.setYear("2000");
     newdt.setDate("01");
     newdt.setMonth("01");

     var year="";
     //Year
     try{
         var isyear=false;
         if(iyyyy>-1){
            year=dt.substr(iyyyy,4);
            isyear=true;
         }
         else if(iyy>-1){
            year=dt.substr(iyy,2);
            isyear=true;
         }
         if(isyear){
            if(type=="1"){//
               year=parseInt(year)+1911;
            }
            newdt.setFullYear(year);
         }
     }
     catch(e1){
         return false;
     }
	 //Day
	 if(newdt.getMonth() != 1){
     try{
         if(idd>-1){
             if(dt.substr(idd,2)>"31"||dt.substr(idd,2)<"01"){
                 return false;
             }
             newdt.setDate(dt.substr(idd,2));
         }
     }
     catch(e1){
         return false;
     }

     //Month
     try{
         if(imm>-1){
             if(dt.substr(imm,2)>"12"||dt.substr(imm,2)<"01"){
                 return false;
             }
             newdt.setMonth(dt.substr(imm,2)-1);
         }
     }
     catch(e1){
         return false;
     }
    }
	else{
	 //Month
     try{
         if(imm>-1){
             if(dt.substr(imm,2)>"12"||dt.substr(imm,2)<"01"){
                 return false;
             }
             newdt.setMonth(dt.substr(imm,2)-1);
         }
     }
     catch(e1){
         return false;
     }
	 //DAY
	 try{
         if(idd>-1){
             if(dt.substr(idd,2)>"31"||dt.substr(idd,2)<"01"){
                 return false;
             }
             newdt.setDate(dt.substr(idd,2));
         }
     }
     catch(e1){
         return false;
     }
	}

     //Hour
     try{
         if(ihh>-1){
             if(dt.substr(ihh,2)>"23"){
                 return false;
             }
             newdt.setHours(dt.substr(ihh,2));
         }
     }
     catch(e1){
         return false;
     }

     //Minute
     try{
         if(imi>-1){
             if(dt.substr(imi,2)>"59"){
                 return false;
             }
             newdt.setMinutes(dt.substr(imi,2));
         }
     }
     catch(e1){
         return false;
     }

     //Second
     try{
         if(iss>-1){
             if(dt.substr(iss,2)>"59"){
                 return false;
             }
             newdt.setSeconds(dt.substr(iss,2));
         }
     }
     catch(e1){
         return false;
     }

     //Year
     if(iyyyy>-1){
          if(newdt.getFullYear()!=year){
                 return false;
          }
     }
     else if(iyy>-1){
          if(newdt.getFullYear()!=year){
                 return false;
          }
     }

      //Day
     if(idd>-1){
          if(newdt.getDate()!=dt.substr(idd,2)){
                 return false;
          }
     }

     //Month
     if(imm>-1){
          if(newdt.getMonth()!=(dt.substr(imm,2)-1)){
                return false;
          }
     }

     //Hour
     if(ihh>-1){
          if(newdt.getHours()!=dt.substr(ihh,2)){
                 return false;
          }
     }

     //Minute
     if(imi>-1){
          if(newdt.getMinutes()!=dt.substr(imi,2)){
                 return false;
          }
     }

     //Second
     if(iss>-1){
          if(newdt.getSeconds()!=dt.substr(iss,2)){
                return false;
          }
     }
     return true;
}
catch(e){
     return false;
}
}
function checkforchoose(obj,nam) {
	var myobj = eval("document.all." + obj);
	if (myobj.length != null) {
			for (i=0;i<myobj.length;i++) {
				if (myobj[i].checked == true) {
					return true;
					}
				}
		}else{
			if (myobj.checked == true) {
				return true;
				}
		}
	alert(nam);
	return false;
	}
function isie (val,len) {
	if (val.length > len) return 24;
	Reg = /^[\d\*\?]*$/g
	if (!IsEmpty(val)) {
		if (val.match(Reg)) {
			return 1;
			}
			return 11;
		}
		return 11;
	}
function validDateStr(str){
	var reg =  /^(\d{1,4})(\d{1,2})(\d{1,2})$/;
	var r = str.match(reg);
	if(r==null){
		return false;
	}
	else{
		var d = new Date(r[1],r[2]-1,r[3]);
		if(d.getFullYear()==r[1]&&(d.getMonth()+1)==r[2]&&d.getDate()==r[3]){
			return true;
		}
		else{
			return false;
		}   
	}
}