
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

function IsLetter(str) {
var len = str.length;
var i = 0;
while (i<len) {
	someletter = str.charAt(i).charCodeAt()
	if (!((someletter>64 && someletter<91)||(someletter>96 && someletter<123))){
		return false;
		}
	i++
	}
	return true;
}


function IsInt(objStr,sign,zero)
{
var reg;    
var bolzero;    

if(Trim(objStr)=="")

    {

        return false;

    }

    else

    {

        objStr=objStr.toString();

    }    

    

    if((sign==null)||(Trim(sign)==""))

    {

        sign="+-";

    }

    

    if((zero==null)||(Trim(zero)==""))

    {

        bolzero=false;

    }

    else

    {

        zero=zero.toString();

        if(zero=="0")

        {

            bolzero=true;

        }

        else

        {

            alert("����Ƿ��0����ֻ��Ϊ(�ա�0)");

        }

    }

    

    switch(sign)

    {

        case "+-":

          

            reg=/(^-?|^\+?)\d+$/;            

            break;

        case "+": 

            if(!bolzero)           

            {

           

                reg=/^\+?[0-9]*[1-9][0-9]*$/;

            }

            else

            {


                reg=/^\+?[0-9]*[0-9][0-9]*$/;

            }

            break;

        case "-":

            if(!bolzero)

            {

            

                reg=/^-[0-9]*[1-9][0-9]*$/;

            }

            else

            {

         


                reg=/^-[0-9]*[0-9][0-9]*$/;

            }            

            break;

        default:

            alert("����Ų���ֻ��Ϊ(�ա�+��-)");

            return false;

            break;

    }

    

    var r=objStr.match(reg);

    if(r==null)

    {

        return false;

    }

    else

    {        

        return true;     

    }

}

function IsFloat(objStr,sign,zero)

{

    var reg;    

    var bolzero;    

    

    if(Trim(objStr)=="")

    {

        return false;

    }

    else

    {

        objStr=objStr.toString();

    }    

    

    if((sign==null)||(Trim(sign)==""))

    {

        sign="+-";

    }

    

    if((zero==null)||(Trim(zero)==""))

    {

        bolzero=false;

    }

    else

    {

        zero=zero.toString();

        if(zero=="0")

        {

            bolzero=true;

        }

        else

        {

            alert("����Ƿ��0����ֻ��Ϊ(�ա�0)");

        }

    }

    

    switch(sign)

    {

        case "+-":

          

            reg=/^((-?|\+?)\d+)(\.\d+)?$/;

            break;

        case "+": 

            if(!bolzero)           

            {

           

                reg=/^\+?(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/;

            }

            else

            {

            

                reg=/^\+?\d+(\.\d+)?$/;

            }

            break;

        case "-":

            if(!bolzero)

            {

            

                reg=/^-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/;

            }

            else

            {

         

                reg=/^((-\d+(\.\d+)?)|(0+(\.0+)?))$/;

            }            

            break;

        default:

            alert("����Ų���ֻ��Ϊ(�ա�+��-)");

            return false;

            break;

    }

    

    var r=objStr.match(reg);

    if(r==null)

    {

        return false;

    }

    else

    {        

        return true;     

    }

}


function IsUpLoadUrl(str) {
	Reg = /([a-z]|[A-Z]){1}:\\([^\n\r \f\\\/:\*\?\"<>\|]+\\)*[^\n\r \f\\\/:\*\?\"<>\|]+\.(\w+)/
	if (Reg.exec(str)) {
		return true;
		}else{
		return false;
		}
	}

function IsEmpty(str){
	if (Trim(str).length == 0) {
		return true;
		}
		return false;
	}

function IsNum (str){
	Reg = /[^0-9]/
	if (!IsEmpty(str)) {
		if (str.toString().match(Reg)) {
			return false;
			}
			return true;
		}
	
		return false;
	}


function IsNumLetter (str) {
	Reg = /[^\w]/;
	Reg1 = /[a-z]|[A-Z]/;
	Reg2 = /[0-9]/;
	if (!IsEmpty(str)) {
		if (!str.match(Reg)) {
			if (str.match(Reg1) && str.match(Reg2)){
				return true;
				}
			
			}
		}
		return false;
	}


function IsChinese (str) {
	Reg = /[^\u4e00-\u9fa5]/
	if (!IsEmpty(str)) {
		if (str.match(Reg)) {
			return false;
			}
			return true;
		}
		return false;
	}

function IsLength (str,len) {
	Reg = /[^\x00-\xff]/g;
	slen = str.replace(Reg,"11").length;
	if (slen > len) {
		return false;
		}
		return true;
	}

function IsMail (str) {
	Reg = /\w+@\w+(\.\w{2,})+/g;
	if (str.match(Reg)) {
		return true;
		}
		return false;
	}

function IsIp (str) {
	Reg = /^([1-9]{1}|[1-9]{1}\d{1}|[1]{1}\d{2}|[2]{1}[0-4]{1}\d{1}|[2]{1}5[0-5]{1})(\.([0-9]{1}|[1-9]{1}\d{1}|[1]{1}\d{2}|[2]{1}[0-4]{1}\d{1}|[2]{1}5[0-5]{1})){3}$/g
	if (str.match(Reg)){
		ip=str.split(".");
		var ipnum = 0;
		for (i=0; i<ip.length;i++) {
			ipnum += ip[i]*Math.pow(255,(ip.length-1-i));
			}
		if (IsBach(ipnum,0,4244897280)){
			return true;
			}
			
		}
		return false;
	}
	
function IsBach (num,minnum,maxnum) {
	if (IsNum(num) && IsNum(minnum) && IsNum(maxnum)) {
		if ((num>=minnum) && (num<=maxnum)) {
			return true;
			}
			
		}
		return false;
	}

function IsUrl (str) {
	Reg = /^\w+:\/\/(\w+\.)+\w+(|:\d+)\/(\w+\/)*[\w\.\?]*$/g;
	if (str.match(Reg)) {
		return true;
		}
		return false;
	}

function buttonover (thobj) {
	thobj.style.color="red";
}
function buttonout (thobj) {
	thobj.style.color="black";
}
function buttonoverpage (thobj) {
	thobj.style.color="red";
}
function buttonoutpage (thobj) {
	thobj.style.color="red";
}
function buttonoverselect (thobj) {
	//thobj.style.backgroundImage="url(../../images/btnUnify/button_B_2.gif)";
	thobj.style.color="red";
}
function buttonoutselect (thobj) {
	//thobj.style.backgroundImage="url(../../images/btnUnify/button_B_2.gif)";
	//thobj.style.color="#ffffff"
}