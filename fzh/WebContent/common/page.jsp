<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script type="text/javascript">
function toPage(page){
	if(page <= 0){
		alert("当前已是第 1 页！");
		return;
	}
	var form = document.forms[0];
	form.page.value = page;
	form.submit();
}
</script>
<input type="hidden" name="page" value="${page}" />
<div class="group page-nav">
	<div class="pagination">
		<ul>
			<%-- <%
				String start = "0";//(String) request.getAttribute("start");
				Integer totalCount = 10;//(Integer) request.getAttribute("totalCount");
				System.out.println("start: " + start + " == total: " + totalCount);
				Integer currPage = Integer.parseInt(start) / 10;
				Integer totalPage = totalCount / 10;
				if (currPage == 0) {
			%>
			<li class="disabled"><span>上一页</span></li>
			<%
				} else {
			%>
			<li><a
				href="javascript:toPage('1');">上一页</a></li>
			<%
				}
				for (int i = 0; i < totalPage + 1; i++) {
					if (i == currPage) {
			%>
			<li class="active"><span><%=i + 1%></span></li>
			<%
				} else {
			%>
			<li><a
				href="javascript:toPage('1');"><%=i + 1%></a></li>
			<%
				}
				}

				if (currPage < totalPage) {
			%>
			<li><a
				href="javascript:toPage('1');">下一页</a></li>
			<%
				}
			%> --%>
			<li><a href="javascript:toPage('${page-1}');">上一页</a></li>
			<li><a href="javascript:toPage('${page+1}');">下一页</a></li>
		</ul>
	</div>
	<div class="clr"></div>
</div>