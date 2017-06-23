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
<link rel="stylesheet" href="<%=basePath %>css/groupmag.css">
<script type="text/javascript"
	src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/groupmag.js"></script>
</head>
<body>

	<div class="bd">
		<div class="group_list">
			<div class="inner_menu_box" id="groupsList">
				<dl class="inner_menu">
					<dt class="inner_menu_item selected">
						<a href="./微信公众平台_files/微信公众平台.htm" class="inner_menu_link"> <strong>全部用户</strong><em
							class="num">(${totalCount})</em>
						</a>
					</dt>

					<s:iterator value="#request.resultList" id="entry" status="st">
						<s:if test="%{#entry.flag==1 || #entry.flag==2}">
							<dd class="inner_menu_item "
								id="group<s:property value='groupId' />">
								<a href="javascript:void(0);" class="inner_menu_link"> <strong><s:property
											value='groupName' /></strong><em class="num">(<s:property
											value='count' />)
								</em>
								</a>
							</dd>
						</s:if>
						<s:else>
							<dd class="inner_menu_item "
								id="group<s:property value='groupId' />">
								<a href="javascript:void(0);" class="inner_menu_link"> <strong><s:property
											value='groupName' /></strong><em class="num">(<s:property
											value='count' />)
								</em>
								</a> <span class="frm_input_box appended"
									id="groupInput<s:property value='groupId' />"
									data-gid="<s:property value='groupId' />"
									data-gname="<s:property value='groupName' />"
									data-gnum="<s:property value='count' />"> <input
									type="text" value="<s:property value='groupName' />"
									class="frm_input js_groupInput"> <a
									href="javascript:void(0);"
									class="frm_input_append icon16_common enter_gray js_groupNameEnter">确定</a>
								</span> <span class="menu_opr"> <a
									data-gid="<s:property value='groupId' />"
									href="javascript:void(0);"
									class="icon16_common edit_gray no_extra js_iconEdit">编辑</a> <a
									data-gid="<s:property value='groupId' />"
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
						<a
							href="https://mp.weixin.qq.com/cgi-bin/contactmanage?t=user/index&pagesize=10&pageidx=0&type=0&groupid=1&token=918449472&lang=zh_CN"
							class="inner_menu_link"> <strong>黑名单</strong><em class="num">(0)</em>
						</a>
					</dt>
				</dl>
			</div>
		</div>
	</div>

</body>
</html>
