<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="sidebar-nav">
		<a href="#dashboard-menu" class="nav-header" data-toggle="collapse">
		<i class="icon-dashboard"></i>管理 <i class="icon-chevron-up"></i></a>
		<ul id="dashboard-menu" class="nav nav-list collapse in">
			<li><a href="${path}/wxpubno/addWxnoUI">配置接口</a></li>
			<li><a href="${path}/firstKey/firstKeyUI">首次关注回复</a></li>
			<li><a href="${path}/keyset/list">关键字回复</a></li>
			<li><a href="${path}/imagetextinfo/list">图文素材</a></li>
			<li><a href="${path}/menu/wxtreeUI">自定义菜单</a></li>
			<li><a href="${path}/user/addUser">添加登录用户</a></li>
			<li><a href="${path}/wxpubno/JSSDKUI">添加分享配置</a></li>
			<li><a href="${path}/wxpubno/oauthUI">网页授权配置</a></li>
		</ul>

		<a href="#calc-menu" class="nav-header" data-toggle="collapse">
		<i class="icon-bar-chart"></i>统计 <i class="icon-chevron-up"></i></a>
		<ul id="calc-menu" class="nav nav-list collapse in">
			<li><a href="${path}/messageinfo/calcUserMsg">消息统计</a></li>
			<li><a href="${path}/messageinfo/calcKeyword">关键字统计</a></li>
			<li><a href="${path}/messageinfo/calcMenuMsg">自定义菜单统计</a></li>
		</ul>

		<a href="#accounts-menu" class="nav-header" data-toggle="collapse"><i
			class="icon-briefcase"></i>账号<span class="label label-info">+3</span></a>
		<ul id="accounts-menu" class="nav nav-list collapse">
			<li><a href="sign-in.html">Sign In</a></li>
			<li><a href="sign-up.html">Sign Up</a></li>
			<li><a href="reset-password.html">Reset Password</a></li>
		</ul>

		<a href="#error-menu" class="nav-header collapsed"
			data-toggle="collapse"><i class="icon-exclamation-sign"></i>Error
			Pages <i class="icon-chevron-up"></i></a>
		<ul id="error-menu" class="nav nav-list collapse">
			<li><a href="403.html">403 page</a></li>
			<li><a href="404.html">404 page</a></li>
			<li><a href="500.html">500 page</a></li>
			<li><a href="503.html">503 page</a></li>
		</ul>

		<a href="#legal-menu" class="nav-header" data-toggle="collapse"><i
			class="icon-legal"></i>微官网模板</a>
		<ul id="legal-menu" class="nav nav-list collapse">
			<li><a href="${path}/wxweb.jsp">微官网展示</a></li>
			<li><a href="terms-and-conditions.html">Terms and Conditions</a></li>
		</ul>

		<a href="help.html" class="nav-header"><i
			class="icon-question-sign"></i>Help</a> <a href="faq.html"
			class="nav-header"><i class="icon-comment"></i>Faq</a>
	</div>
