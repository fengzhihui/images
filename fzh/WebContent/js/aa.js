/*
Copyright 2005  Vitaliy Shevchuk (shevit@users.sourceforge.net)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

AjaxAnywhere.defaultInstanceName = "default";
// constructor;
function AjaxAnywhere() {

    this.id = AjaxAnywhere.defaultInstanceName;
    this.formName = null;
    this.notSupported = false;
    this.delayBeforeContentUpdate = true;
    this.delayInMillis = 100;
    this.isInSubmiting = false; //judge a previous ajax request is not completed
    this.delayToDropPreviousRequest = 2000;  //used with isInSubmiting ,when it's true
    
    if (window.XMLHttpRequest) {
        this.req = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        try {
            this.req = new ActiveXObject("Msxml2.XMLHTTP");
        } catch(e) {
            try {
                this.req = new ActiveXObject("Microsoft.XMLHTTP");
            } catch(e1) {
                this.notSupported = true;
                /* XMLHTTPRequest not supported */
            }
        }
    }

    if (this.req == null || typeof this.req == "undefined")
        this.notSupported = true;
}

/**
* Stores substitutes SubmitButton names in to redo sustitution if a button was eventually inside a refresh zone.
*/
AjaxAnywhere.prototype.substitutedSubmitButtons = new Array();
AjaxAnywhere.prototype.substitutedSubmitButtonsInfo = new Object();

/**
* Returns a Form object that corresponds to formName property of this AjaxAnywhere class instance.
*/
AjaxAnywhere.prototype.findForm = function () {
    var form;
    if (this.formName != null)
        form = document.forms[this.formName];
    else if (document.forms.length > 0)
        form = document.forms[0];

    if (typeof form != "object")
        alert("AjaxAnywhere error: Form with name [" + this.formName + "] not found");
    return form;
}


/**
* Binds this instance to window object using "AjaxAnywhere."+this.id as a key.
*/
AjaxAnywhere.prototype.bindById = function () {
    var key = "AjaxAnywhere." + this.id;
    window[key] = this;
}

/**
* Finds an instance by id.
*/
AjaxAnywhere.findInstance = function(id) {
    var key = "AjaxAnywhere." + id;
    return window[key];
}

/**
* This function is used to submit all form fields by AJAX request to the server.
* If the form is submited with &lt;input type=submit|image&gt;, submitButton should be a reference to the DHTML object. Otherwise - undefined.
*/
AjaxAnywhere.prototype.submitAJAX = function(additionalPostData, submitButton) {

    if (this.notSupported)
        return this.onSubmitAjaxNotSupported(additionalPostData);

    if (additionalPostData == null || typeof additionalPostData == "undefined")
        additionalPostData = "";

    this.bindById();

    var form = this.findForm();

    var actionAttrNode = form.attributes.getNamedItem("action");
    var url = actionAttrNode == null?null:actionAttrNode.nodeValue;
    if ((url == null) || (url == ""))
        url = location.href;

    var zones = this.getZonesToReload(url, submitButton);

    if (zones == null) {
        if (typeof form.submit_old == "undefined")
            form.submit();
        else
            form.submit_old();
        return;
    }

    if( this.isInSubmiting ) {
    	delay( this.delayToDropPreviousRequest );
    	this.dropPreviousRequest(); //He Kun
    }	
	
    this.req.open("POST", url, true);
    this.req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    this.req.setRequestHeader("Accept", "text/xml");

    var postData = this.preparePostData(submitButton);

    if (zones != "")
        postData = '&aazones=' + encodeURIComponent(zones) + "&" + postData + "&" + additionalPostData;
    else
        postData += "&" + additionalPostData;

    this.sendPreparedRequest(postData);

}
/**
* sends a GET request to the server.
*/
AjaxAnywhere.prototype.getAJAX = function(url, zonesToRefresh) {
    if (this.notSupported)
        return this.onGetAjaxNotSupported(url);

    this.bindById();

    if (zonesToRefresh == null || typeof zonesToRefresh == "undefined")
        zonesToRefresh = "";
    var urlDependentZones = this.getZonesToReload(url);
    if (urlDependentZones == null) {
        location.href = url;
        return;
    }

    if (urlDependentZones.length != 0)
        zonesToRefresh += "," + urlDependentZones;

    this.dropPreviousRequest();

    url += (url.indexOf("?") != -1) ? "&" : "?";

    url += "aa_rand=" + Math.random();
    // avoid caching

    if (zonesToRefresh != null && zonesToRefresh != "")
        url += '&aazones=' + encodeURIComponent(zonesToRefresh);

    this.req.open("GET", url, true);
    this.req.setRequestHeader("Accept", "text/xml");

    this.sendPreparedRequest("");
}

/**
* @private
*/
AjaxAnywhere.prototype.sendPreparedRequest = function (postData) {
    var callbackKey = this.id + "_callbackFunction";
    if (typeof window[callbackKey] == "undefined")
        window[callbackKey] = new Function("AjaxAnywhere.findInstance(\"" + this.id + "\").callback(); ");
    this.req.onreadystatechange = window[callbackKey];

    this.showLoadingMessage();
    this.req.setRequestHeader("aaxmlrequest", "true");
    this.req.send(postData);
}
/**
* Used internally by AjaxAnywhere. Aborts previous request if not completed.
*/
AjaxAnywhere.prototype.dropPreviousRequest = function() {
    if (this.req.readyState != 0 && this.req.readyState != 4) {
        // abort previous request if not completed
        this.req.abort();
        this.handlePrevousRequestAborted();
    }
}

/**
* Internally used to prepare Post data.
* If the form is submited with &lt;input type=submit|image&gt;, submitButton is a reference to the DHTML object. Otherwise - undefined.
*/
AjaxAnywhere.prototype.preparePostData = function(submitButton) {
    var form = this.findForm();
    var result = "";
    for (var i = 0; i < form.elements.length; i++) {
        var el = form.elements[i];
        if (el.tagName.toLowerCase() == "select") {
            for (var j = 0; j < el.options.length; j++) {
                var op = el.options[j];
                if (op.selected)
                    result += "&" + encodeURIComponent(el.name) + "=" + encodeURIComponent(op.value);
            }
        } else if (el.tagName.toLowerCase() == "textarea") {
            result += "&" + encodeURIComponent(el.name) + "=" + encodeURIComponent(el.value);
        } else if (el.tagName.toLowerCase() == "input") {
            if (el.type.toLowerCase() == "checkbox" || el.type.toLowerCase() == "radio") {
                if (el.checked)
                    result += "&" + encodeURIComponent(el.name) + "=" + encodeURIComponent(el.value);
            } else if (el.type.toLowerCase() == "submit") {
                if (el == submitButton) // is "el" the submit button that fired the form submit?
                    result += "&" + encodeURIComponent(el.name) + "=" + encodeURIComponent(el.value);
            } else if (el.type.toLowerCase() != "button") {
                result += "&" + encodeURIComponent(el.name) + "=" + encodeURIComponent(el.value);
            }
        }
    }
    if (typeof submitButton != 'undefined' && submitButton != null && submitButton.type.toLowerCase() == "image") {
        if (submitButton.name == null || submitButton.name == "" || typeof submitButton.name == "undefined")
            result += "&x=1&y=1"; // .x and .y coordinates calculation is not supported.
        else
            result += "&" + encodeURIComponent(submitButton.name) + ".x=1&" +
                      encodeURIComponent(submitButton.name) + ".y=1";
    }
    return result;
}

/**
* Pauses the thread of execution for the specified number of milliseconds
*/
function delay(millis) {
    date = new Date();
    var curDate = null;
    do {
        var curDate = new Date();
    }
    while (curDate - date < millis);
}

/**
* A callback. internally used
*/
AjaxAnywhere.prototype.callback = function() {

    if (this.req.readyState == 4) {

        this.onBeforeResponseProcessing();
        this.hideLoadingMessage();

        if (this.req.status == 200) {	
            if (this.req.getResponseHeader('content-type').toLowerCase().substring(0, 8) != 'text/xml')
                alert("AjaxAnywhere error : content-type in not text/xml : [" + this.req.getResponseHeader('content-type') + "]");

            var docs = this.req.responseXML.getElementsByTagName("document");
            var redirects = this.req.responseXML.getElementsByTagName("redirect");
            var zones = this.req.responseXML.getElementsByTagName("zone");
            var exceptions = this.req.responseXML.getElementsByTagName("exception");
            var scripts = this.req.responseXML.getElementsByTagName("script");
            var images = this.req.responseXML.getElementsByTagName("image");

            if (redirects.length != 0) {
                var newURL = redirects[0].firstChild.data;
                location.href = newURL;
            }
            if (docs.length != 0) {
                var newContent = docs[0].firstChild.data;

                //cleanup ressources
                delete this.req;

                document.close();
                document.write(newContent);
                document.close();
            }

            if (images.length != 0) {
                var preLoad = new Array(images.length);
                for (var i = 0; i < images.length; i++) {
                    var img = images[i].firstChild;
                    if (img != null) {
                        preLoad[i] = new Image();
                        preLoad[i].src = img.data;
                    }
                }
                if (this.delayBeforeContentUpdate) {
                    delay(this.delayInMillis);
                }
            }

            if (zones.length != 0) {
                for (var i = 0; i < zones.length; i++) {
                    var zoneNode = zones[i];
                    var name = zoneNode.getAttribute("name");
                    var fc = zoneNode.firstChild;

                    var html = (fc == null)?"":fc.data;

                    var zoneHolder = document.getElementById("aazone." + name);
                    if (zoneHolder != null && typeof(zoneHolder) != "undefined") {
                        zoneHolder.innerHTML = html;
                    }

                }
            }
            if (exceptions.length != 0) {
                var e = exceptions[0];
                var type = e.getAttribute("type");
                var stackTrace = e.firstChild.data;
                this.handleException(type, stackTrace);
            }

            if (scripts.length != 0) {
                for (var $$$$i = 0; $$$$i < scripts.length; $$$$i++) {
                    // use $$$$i variable to avoid collision with "i" inside user script
                    var script = scripts[$$$$i].firstChild;
                    if (script != null) {
                        script = script.data;
                        if (script.indexOf("document.write") != -1) {
                            this.handleException("document.write", "This script contains document.write(), which is not compatible with AjaxAnywhere : \n\n" + script);
                        } else {
                            eval(script);
                        }
                    }
                }

                var globals = this.getGlobalScriptsDeclarationsList(script);
                if (globals != null)
                    for (var i in globals) {
                        var objName = globals[i];
                        try {
                            window[objName] = eval(objName);
                        } catch(e) {
                        }
                    }
            }

        } else {
            if (this.req.status != 0)
                this.handleHttpErrorCode(this.req.status);
        }
        this.restoreSubstitutedSubmitButtons();
        this.onAfterResponseProcessing();
        
        
       //统一处理出错信息
         $(".error_text").hide();
	    var s=$(".error_text:first").text();
	    if(s){
	    	var d=$("#error_text_div");
	    	if(d.size()==0){ d=$("<span id='error_text_div' style='width:200px;height:20px;border:solid 1px blue;line-height:20px;text-align:center;z-index:10000'>"+s+"</span>");
	    		d.css("position","absolute").css("background-color","white").css("color","blue").css("top","1px").css("left","40%");
		    	$(".titlebar_left:first").after(d);
	    	}else{
	    		d.show();
	    		d.text(s);
	    	}
	    	d.fadeOut(5000);
	    }
    }
}

/**
*  Default sample loading message show function. Overrride it if you like.
*/
AjaxAnywhere.prototype.showLoadingMessage = function() {

        var doc ;
        /*
        if (parent.topFrame){
            doc = parent.topFrame.document ;
        } else if (parent.parent.topFrame) {
            doc = parent.parent.topFrame.document ;
        } else if (parent.parent.parent.topFrame) {
            doc = parent.parent.parent.topFrame.document ;
        } else {
            doc = document ;
        }*/
        
        var pare=window.parent;
    	while(pare){
    		if(pare.self==pare.parent) break;
    		pare=pare.parent;
    	}
    	
    	doc=pare.document;
        
    var div = doc.getElementById("AA_" + this.id + "_loading_div");
    if (div == null) {

        div = doc.createElement("DIV");
        doc.body.appendChild(div);
        div.id = "AA_" + this.id + "_loading_div";
        
        div.innerHTML = "<MARQUEE scrollAmount='5' behavior='alternate'>&nbsp;Loading...</MARQUEE>";
        div.style.position = "absolute";
        div.style.border = "1 solid green";
        div.style.color = "white";
        div.style.backgroundColor = "green";
        div.style.width = "100px";
        div.style.heigth = "50px";
        div.style.fontFamily = "Arial, Helvetica, sans-serif";
        div.style.fontWeight = "bold";
        div.style.fontSize = "11px";
    }
    div.style.top = doc.body.scrollTop + "px";
    div.style.left = (doc.body.offsetWidth - 120) + "px";

    div.style.display = "block";
}

/**
*  Default sample loading message hide function. Overrride it if you like.
*/
AjaxAnywhere.prototype.hideLoadingMessage = function() {
    var div ;
    var doc ;
    /*
    if (parent.topFrame){
        doc = parent.topFrame.document ;
    } else if (parent.parent.topFrame) {
        doc = parent.parent.topFrame.document ;
    } else if (parent.parent.parent.topFrame) {
        doc = parent.parent.parent.topFrame.document ;
    } else {
        doc = document ;
    }*/
    var pare=window.parent;
	while(pare){
		if(pare.self==pare.parent) break;
		pare=pare.parent;
	}
	
	doc=pare.document;
    div = doc.getElementById("AA_" + this.id + "_loading_div");
    if (div){
        div.style.display = "none";
    }

}

/**
* This function is used to facilitatte AjaxAnywhere integration with existing projects/frameworks.
* It substitutes default Form.sumbit().
* The new implementation calls AjaxAnywhere.isFormSubmitByAjax() function to find out if the form
* should be submitted in traditional way or by AjaxAnywhere.
*/
AjaxAnywhere.prototype.substituteFormSubmitFunction = function() {

    this.bindById();

    var form = this.findForm();

    form.submit_old = form.submit;  
    var code = " var ajax = AjaxAnywhere.findInstance(\"" + this.id + "\"); " +    			
    			" if ( typeof ajax !='object' || ! ajax.isFormSubmitByAjax() ) " +
               " {	ajax.findForm().submit_old(); }" +
               " else " +
               "{ if(event && event.srcElement && ajax.isSubmitableButton( event.srcElement ) &&  !ajax.isAjaxButton(event.srcElement.name) ) " +
               " {   ajax.findForm().submit_old();  } " +
               "  else " +
               "   ajax.submitAJAX(); " +
               "}";
               //by He Kun 2008-2-1, judge by button to decide use ajax submit or not.
    form.submit = new Function(code);

}
/**
  * calll substituteSubmitButtonsBehavior, but use string to pass button id array
  *
  */
AjaxAnywhere.prototype.substituteSubmitButtonsBehaviorByIDs = function (keepExistingOnClickHandler, ids) {

	if(typeof ids == "undefined") {
		return false;
	}
	
	var idArray = ids.split(",");

	var controlArray = new Array();
	 for (var i = 0; i < idArray.length; i++ ) {
	 	var con = document.getElementById(idArray[i]);
	 	if(con) {
	 		controlArray.push(con);
	 	} else {
	 		alert("control not found by id: " + idArray[i]);
	 	}
	 }

	 this.substituteSubmitButtonsBehavior(keepExistingOnClickHandler, controlArray);
}
/**
* Substitutes the default behavior of &lt;input type=submit|image&gt; to submit the form via AjaxAnywhere.
*
* @param {boolean} indicates if existing onClick handlers should be preserved.
* If keepExistingOnClickHandler==true,
* Existing handler will be called first if it returns false, or if event.returnValue==false, AjaxAnywhere will not
* continue form submission.
* If keepExistingOnClickHandler==false or undefines, existing onClick event handlers will be replaced.
*
* @param {Array} list of submitButtons and submitImages names. If the parameter is omitted or undefined,
* all elements will be processed
*/
AjaxAnywhere.prototype.substituteSubmitButtonsBehavior = function (keepExistingOnClickHandler, elements) {
    var form = this.findForm();
    if (elements == null || typeof elements == "undefined") { // process all elements
        var elements = new Array();
        for (var i = 0; i < form.elements.length; i++) {
            elements.push(form.elements[i]);
        }

        var inputs = document.getElementsByTagName("input");
        for (var i = 0; i < inputs.length; i++) {
            var input = inputs[i];
            if (input.type != null && typeof input.type != "undefined" &&
                input.type.toLowerCase() == "image" && input.form == form) {
                elements.push(input);
            }
        }

        for (var i = 0; i < elements.length; i++) {
            var el = elements[i];
            if (el.tagName.toLowerCase() == "input" && (el.type.toLowerCase() == "submit"
                    || el.type.toLowerCase() == "image")) {             
                this.substituteSubmitBehavior(el, keepExistingOnClickHandler);

            }
        }
    } else { //process only specified elements
        for (var i = 0; i < elements.length; i++) {
            var el = elements[i];
            if (el == null)
                continue;

            if (typeof el != "object")
                form.elements[el];

            if (typeof el != "undefined") {
                if (el.tagName.toLowerCase() == "input" && (el.type.toLowerCase() == "submit"
                        || el.type.toLowerCase() == "image"))
                    delay(10);//He Kun , 2008-2-15, delay 10 ms, it's required,or follow line will not be executed, the reason is not clear!
                    this.substituteSubmitBehavior(el, keepExistingOnClickHandler);
            }
        }
    }

}

/**
  * check specified button is submitable button, such as input,image,select,submit
  * @author He Kun
  */
AjaxAnywhere.prototype.isSubmitableButton = function( obj ) {
	 if (obj.type != null && typeof obj.type != "undefined") {
	 	var typeName = obj.type.toLowerCase();
	 
	 	if(typeName=="image" || typeName=="input" || typeName=="select" || typeName=="submit"  || typeName=="button")
	 	   return true;
	 }	 
	 return false;
}

/**
  * check specified button is in array substitutedSubmitButtons
  * @author He Kun
  */
AjaxAnywhere.prototype.isAjaxButton = function( idOrName ) {
	 var inList = false;
	 for (var i = 0; i < this.substitutedSubmitButtons.length; i++) {
        var btnName = this.substitutedSubmitButtons[i];
        if (btnName ==  idOrName ) {
            inList = true;
            break;
        }
    }
    return inList;
}

/**
* Performs a single element behavior substitution
*
* @private
*/
AjaxAnywhere.prototype.substituteSubmitBehavior = function (el, keepExistingOnClickHandler) {

    var inList = false;
    for (var i = 0; i < this.substitutedSubmitButtons.length; i++) {
        var btnName = this.substitutedSubmitButtons[i];
        if (btnName == el.name) {
            inList = true;
            break;
        }
    }
    if (!inList)
        this.substitutedSubmitButtons.push(el.name);

    this.substitutedSubmitButtonsInfo[el.name] = keepExistingOnClickHandler;
	
    if (keepExistingOnClickHandler && (typeof el.onclick != "undefined") && ( el.onclick != null) && ( el.onclick != "")) {
        el.AA_old_onclick = el.onclick;
    }

    el.onclick = handleSubmitButtonClick;
    el.ajaxAnywhereId = this.id;
}

/**
*
* @private
*/
AjaxAnywhere.prototype.restoreSubstitutedSubmitButtons = function() {
    if (this.substitutedSubmitButtons.length == 0)
        return;

    var form = this.findForm();

    for (var i = 0; i < this.substitutedSubmitButtons.length; i++) {
        var name = this.substitutedSubmitButtons[i];
        var el = form.elements[name];
        if (el != null && typeof el != "undefined") {
            if (el.onclick != handleSubmitButtonClick) {
                var keepExistingOnClickHandler = this.substitutedSubmitButtonsInfo[el.name];
                this.substituteSubmitBehavior(el, keepExistingOnClickHandler);
            }
        } else {
            //input type=image
            if (name != null && typeof name != "undefined" && name.length != 0) {
                var elements = document.getElementsByName(name);
                if (elements != null)
                    for (var j = 0; j < elements.length; j++) {
                        el = elements[j];
                        if (el != null && typeof el != "undefined"
                                && el.tagName.toLowerCase() == "input"
                                && typeof el.type != "undefined" && el.type.toLowerCase() == "image") {
                            if (el.onclick != handleSubmitButtonClick) {
                                var keepExistingOnClickHandler = this.substitutedSubmitButtonsInfo[el.name];
                                this.substituteSubmitBehavior(el, keepExistingOnClickHandler);
                            }
                        }
                    }
            }
        }
    }
}

/**
* @private
*/
function handleSubmitButtonClick(_event) {	
    if (typeof this.AA_old_onclick != "undefined") {
       
        var ret = this.AA_old_onclick(_event);       
        if (false == ret)
            return false;
        if (typeof window.event != "undefined")
            if (window.event.returnValue == false)
                return false;
    }
    var onsubmit = this.form.onsubmit;
    if (typeof onsubmit == "function") {
        if (false == onsubmit(_event))
            return false;
        if (typeof window.event != "undefined")
            if (window.event.returnValue == false)
                return false;
    }
    AjaxAnywhere.findInstance(this.ajaxAnywhereId).submitAJAX('', this);

    return false;
}
/**
* Override this function if you use AjaxAnywhere.substituteFormSubmitFunction() to
* dynamically inform AjaxAnywhere of the method you want to use for the form submission.
*/
AjaxAnywhere.prototype.isFormSubmitByAjax = function () {
    return true;
}

/**
* Some browsers (notably IE) do not load images from thier cache when content is updated using
* innerHTML. As a result, each image is re-requested from the server even though the image exists
* in the cache. To work around this issue, AjaxAnywhere preloads images present in the new content
* and intrduces a brief dely (default of 100 milleseconds) before calling innerHTML.
* See http://support.microsoft.com/default.aspx?scid=kb;en-us;319546 for further details.
* This function can be used to change this behaviour.
* @param (boolean) isDelay
*/
AjaxAnywhere.prototype.setDelayBeforeLoad = function (isDelay) {
    this.delayBeforeContentUpdate = isDelay;
}

/**
* Returns the current delay behavior.
*/
AjaxAnywhere.prototype.isDelayBeforeLoad = function () {
    return this.delayBeforeContentUpdate;
}

/**
* Sets the delay period in milliseconds. The default delay is 100 milliseconds.
* @param (int) delayMillis
*/
AjaxAnywhere.prototype.setDelayTime = function (delayMillis) {
    this.delayInMillis = delayMillis;
}

/**
* Returns the delay period in milliseconds.
*/
AjaxAnywhere.prototype.getDelayTime = function () {
    return this.delayInMillis;
}

/**
*   If an exception is throws on the server-side during AJAX request, it will be processed
* by this function. The default implementation is alert(stackTrace);
* Override it if you need.
*/
AjaxAnywhere.prototype.handleException = function(type, details) {
    alert(details);
}
/**
*   If an HTTP Error code returned during AJAX request, it will be processed
* by this function. The default implementation is alert(code);
* Override it if you need.
*/
AjaxAnywhere.prototype.handleHttpErrorCode = function(code) {
    var details = confirm("AjaxAnywhere default error handler. XMLHttpRequest HTTP Error code:" + code + " \n\n Would you like to view the response content in a new window?");
    if (details) {
        var win = window.open("", this.id + "_debug_window");
        if (win != null) {
            win.document.write("<html><body><xmp>" + this.req.responseText);
            win.document.close();
            win.focus();
        } else {
            alert("Please, disable your pop-up blocker for this site first.");
        }
    }
}

/**
* Override it if you need.
*/
AjaxAnywhere.prototype.handlePrevousRequestAborted = function() {
    alert("AjaxAnywhere default error handler. INFO: previous AJAX request dropped")
}


/**
*   If the HTML received in responce to AJAX request contains JavaScript that defines new
* functions/variables, they must be propagated to the proper context. Override this method
* to return the Array of function/variable names.
*/
AjaxAnywhere.prototype.getGlobalScriptsDeclarationsList = function(script) {
    return null;
}

/**
* This function should be overridden by AjaxAnywhere user to implement client-side
* determination of zones to reload.
*
* If the form is submited with &lt;input type=submit|image&gt;, submitButton is a reference to the DHTML object. Otherwise - undefined.
*
* @Returns a comma separated list of zones to reload, or "document.all" to reload
* the whole page. Returns null if the request must be sent in traditional way
*
*/
AjaxAnywhere.prototype.getZonesToReload = function(url, submitButton) {
    return this.getZonesToReaload();
    // backward compatibility only
}
/**
* depreceted : wrond spelling : Reaload will be removed in later versions
*/
AjaxAnywhere.prototype.getZonesToReaload = function(url, submitButton) {
    return "";
}

/**
* Override this method to implement a custom action
*/
AjaxAnywhere.prototype.onRequestSent = function () {
};
/**
* Override this method to implement a custom action
*/
AjaxAnywhere.prototype.onBeforeResponseProcessing = function () {
};
/**
* Override this method to implement a custom action
*/
AjaxAnywhere.prototype.onAfterResponseProcessing = function () {
};

/**
* Provides a default implementation from graceful degradation for getAJAX()
* calls location.href=url if XMLHttpRequest is unavailable, reloading the entire page .
*/
AjaxAnywhere.prototype.onGetAjaxNotSupported = function (url) {
    location.href = url;
    return false;
};

/**
* Provides a default implementation from graceful degradation for submitAJAX()
* calls form.submit() if XMLHttpRequest is unavailable, reloading the entire page
*/
AjaxAnywhere.prototype.onSubmitAjaxNotSupported = function (additionalPostData) {
    var form = this.findForm();

    var actionAttrNode = form.attributes.getNamedItem("action");
    var url = actionAttrNode == null?null:actionAttrNode.nodeValue;
    var url_backup = url;
    if (typeof additionalPostData != 'undefined' && additionalPostData != null) {
        url += (url.indexOf("?") != -1) ? "&" : "?";
        url += additionalPostData;
        form.setAttribute("action", url);
        // only POST method allows sending additional
        // date by altering form action URL.
        form.setAttribute("method", "post");
    }

    if (typeof form.submit_old == "undefined")
        form.submit();
    else
        form.submit_old();

    form.setAttribute("action", url_backup);
    return false;
};

AjaxAnywhere.prototype.submitByURL = function (url,contextPth) {
	 var form = this.findForm(); 
	 if(contextPth!=null) url = contextPth + url;
	 else if(contextPath!=null) url = contextPath + url;
	 form.action = url;
	 this.submitAJAX();
}
// default instance.
ajaxAnywhere = new AjaxAnywhere();
ajaxAnywhere.bindById();