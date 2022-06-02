<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	
	if(session.getAttribute("user_id")==null){
		response.sendRedirect("../index.jsp");
	}
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:import url="header.jsp"/>
<div class="container-fluid">
	<h1>Users</h1>
	<div id="users"></div>
</div>
<c:import url="footer.jsp"/>