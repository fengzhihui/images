<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="navbar">
	<div class="navbar-inner">
		<ul class="nav pull-right">
			<li><a href="#"
				class="hidden-phone visible-tablet visible-desktop" role="button">设置</a></li>
			<li id="fat-menu" class="dropdown"><a href="#" role="button"
				class="dropdown-toggle" data-toggle="dropdown"> <i
					class="icon-user"></i> ${user.username} <i class="icon-caret-down"></i></a>
				<ul class="dropdown-menu">
					<li><a tabindex="-1" href="${path}/user/updateUI">修改密码</a></li>
					<li class="divider"></li>
					<li><a tabindex="-1" class="visible-phone" href="#">设置</a></li>
					<li class="divider visible-phone"></li>
					<li><a tabindex="-1"
						href="${path}/logout">退出</a></li>
				</ul></li>
		</ul>
		<a class="brand" target="_blank"
			href="${path}/wx.jsp"> <span class="first">fzhwx开发者</span>
			<span class="second">公众号管理平台</span>
		</a>
	</div>
</div>
