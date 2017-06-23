<%@ page language="java" contentType="text/html;charset=utf-8"%>
<html>
<head>
<title>消息记录列表</title>
<link rel="stylesheet" type="text/css"
	href="<%= request.getContextPath() %>/index1/css/style.css" />
<script type="text/javascript"
	src="<%= request.getContextPath() %>/index1/js/jquery.min.js"></script>

<script type="text/javascript">
	    function ev_check() {
	        return checkval(window);
	    }

	    //AJAX
	    function ajax(users,group){
	    	if(group !="" && users !=""){
				var url = "<%=request.getContextPath()%>/wx/clientinfo_doupdateVO.do";
				$.post(url, {"users":users,"group":group}, function(data){
					if(data !=null && data =="ok"){
						alert('操作成功!');
						//刷新左边frame
						parent.left.location.href = "wx/clientinfo_groupAndCount.do";
					}
			    });
			}
		}
		
	    //添加到分组
	    function addGroup(){
	    	var users = $('#users').val();
	    	var group = $('#addgroup').val();
	    	if(group !=null && group !=0)ajax(users, group);
		}
		
	    //修改分组
	    function updateGroup(user,obj){
			var group = obj.value;
			if(group !=null && user !=null)ajax(user, group);
		}

	    //复选框事件
		function chk(){
			var slt = $('#addgroup');
		    var flag = false;
		    var users = "";
			$('input[name="param._selectitem"]:checked').each(function(){
		    	flag = true;
		    	users += $(this).val() + ',';
		    });
		    //alert(users);
		    $('#users').val(users);
		    if(flag){
		    	if(slt.attr("disabled") == 'disabled'){
					slt.attr("disabled",false);
			    }
			}else{
				slt.attr("disabled",true);
				$('#allbox').attr('checked', false);
			}
		}
	</script>

</head>

<body>
	<s:form key="formList" cssStyle="formList" name="formList"
		theme="simple" onsubmit="return ev_check();">
		<s:hidden name="param._orderby" />
		<s:hidden name="param._desc" />
		<s:hidden name="param._pageno" />
		<s:hidden name="param._pagesize" />
		<s:hidden name="param.queryAll" />
		<input type="hidden" name="_rowcount"
			value="<s:property value="dp.rowCount" />" />

		<div class="wrapper">
			<div class="rinavbg">
				<span class="righ_top">消息列表</span>
			</div>

			<!-- <div class="righ_cont">
	<ul>
		<li><span class="ju">关键字</span><s:textfield
			name="param._ski_keyWord" /></li>

		<li>
		<button type="button" class="btn1 bg"
			onclick="doQuery('/wx/keyservice_doList.do')"><i
			class=" icon-search"></i>查询</button>
		</li>
	</ul>
	</div>
 -->

			<div class="righ_cont1" style="margin-top: 4px">
				<ul>
					<li><a href="javascript:void(0)" title="删除"
						onclick="doClick('btnDelete');"><i class="icon-remove-circle"></i>删除</a></li>
				</ul>
			</div>

		</div>

		<input type="button" id="btnDelete" name="btnDelete"
			style="display: none;" class="delete" onmouseover="buttonover(this);"
			onmouseout="buttonout(this);" onfocus="buttonover(this)"
			onblur="buttonout(this)" value="<s:text name="button_delete"/>"
			onClick="doDelete('/wx/keyservice_doDelete.do')" />

		<div class="divmain">
			<div class="biaoge" style="margin-top: 20px">
				<table class="table table-bordered table-hover table-condensed"
					contenteditable="false">
					<thead>
						<tr class="thead-text">
							<th width="10%" title="<s:text name="全选"/>"><input
								id="allbox" type="checkbox" name="allbox"
								onclick="checkAll();chk();" class="table_checkbox" /></th>
							<th width="20%"><a
								href="javascript:doOrderby('weixinOpenid')">WEIXINOPENID</a></th>
							<th width="20%"><a href="javascript:doOrderby('content')">消息内容</a></th>
							<th width="20%"><a href="javascript:doOrderby('time')">时间</a></th>
							<th width="20%"><a href="javascript:doOrderby('operate')">操作</a></th>
						</tr>
					</thead>
					<s:iterator value="dp.datas" id="id" status="c">
						<tr class="list1">
							<td><input type="checkbox" name="param._selectitem"
								value="<s:property value='weixinOpenid' />" onclick="chk();"
								class="table_checkbox" /></td>
							<td><s:property value="weixinOpenid" /></td>
							<td><s:property value="name" /></td>
							<td></td>
							<td><input type="hidden" id="users"> <s:i18n
									name="public">
								</s:i18n></td>

							<!-- <td><s:i18n name="public">
					<a
						href="wx/keyservice_doCreateOrUpdateInput.do?keyServiceNo=<s:property value='keyServiceNo' />"
						style="color: #0088cc;">修改</a>|<a
						href="wx/keyservice_doAnswerInput.do?keyServiceNo=<s:property value='keyServiceNo' />"
						style="color: #0088cc;">设置回复</a>
				</s:i18n></td> -->
						</tr>
					</s:iterator>
					<s:if test="dp.datas.size==0">
						<tr align="center">
							<td colspan="8">查无记录</td>
						</tr>
					</s:if>
				</table>

			</div>
		</div>


	</s:form>

</body>
</html>