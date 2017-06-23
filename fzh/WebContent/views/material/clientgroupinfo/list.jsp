<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户组</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/groupmag.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/groupmag.js"></script>
</head>
<body>

	<div class="bd">
		<div class="group_list">
			<div class="inner_menu_box" id="groupsList">
				<dl class="inner_menu">
					<dt class="inner_menu_item selected">
						<a href="javascript:void(0);" class="inner_menu_link"> <strong>全部用户</strong><em
							class="num">(${request.allcount})</em> <input type="hidden"
							value="${request.groupno}" id="groupno">
						</a>
					</dt>

					<s:iterator value="#request.clist" id="entry" status="st">
						<s:if test="%{#entry.flag==1 || #entry.flag==2}">
							<dd class="inner_menu_item "
								id="group<s:property value='wclientgroupInfoNo' />">
								<a
									href="clientinfo_list.do?param._se_wclientgroupInfoNo=${wclientgroupInfoNo }"
									target="mainContent" class="inner_menu_link"> <strong><s:property
											value='groupName' /></strong> <em class="num">(<s:property
											value='wenxinPublicNo' />)
								</em>
								</a>
							</dd>
						</s:if>
						<s:else>
							<dd class="inner_menu_item "
								id="group<s:property value='wclientgroupInfoNo' />">
								<a
									href="clientinfo_list.do?param._se_wclientgroupInfoNo=${wclientgroupInfoNo }"
									target="mainContent" class="inner_menu_link"> <strong><s:property
											value='groupName' /></strong> <em class="num">(<s:property
											value='wenxinPublicNo' />)
								</em>
								</a> <span class="frm_input_box appended"
									id="groupInput<s:property value='wclientgroupInfoNo' />"
									data-gid="<s:property value='wclientgroupInfoNo' />"
									data-gname="<s:property value='groupName' />"
									data-gnum="<s:property value='wenxinPublicNo' />"> <input
									type="text" value="<s:property value='groupName' />"
									class="frm_input js_groupInput" /> <a
									href="javascript:void(0);"
									class="frm_input_append icon16_common enter_gray js_groupNameEnter">确定</a>
								</span> <span class="menu_opr"> <a
									data-gid="<s:property value='wclientgroupInfoNo' />"
									href="javascript:void(0);"
									class="icon16_common edit_gray no_extra js_iconEdit">编辑</a> <a
									data-gid="<s:property value='wclientgroupInfoNo' />"
									href="javascript:void(0);"
									class="icon16_common del_gray js_iconDel">删除</a>
								</span>
							</dd>
						</s:else>
					</s:iterator>
				</dl>

				<div class="inner_menu_item extra" id="js_groupAdd">
					<a href="javascript:void(0);" class="inner_menu_link"> <i
						class="icon14_common add_gray"></i> <strong>新建分组</strong>
					</a>
				</div>

				<dl class="inner_menu">
					<dt class="inner_menu_item selected">
						<a href="javascript:void(0);" class="inner_menu_link"> <strong>黑名单</strong><em
							class="num">(0)</em></a>
					</dt>
				</dl>
			</div>
		</div>
	</div>

</body>
</html>
