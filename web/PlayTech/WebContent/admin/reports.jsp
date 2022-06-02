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
<!-- content -->
   <div class="container-fluid">
    <div class="row">
      <div class="col">
         <h3>Reports</h3>
        <div class="py-3">
          <div class="col-md-4">
            <form id="report_form" method="post">
            <input type="hidden" name="action" value="admin_generate_report">
              <div class="form-group">
                <input id="start_date" type="text" class="form-control" name="start_date" placeholder="Start Date" onfocus="(this.type='date')">
              </div>
              <div class="form-group">
               <input id="end_date" type="text" class="form-control" name="end_date" placeholder="End Date" onfocus="(this.type='date')">
              </div>
              <button id="btnGo" name="btnGo" class='btn btn-primary btn-sm'>Go</button>
            </form>
          </div>
        </div>
        <div id="report_data"></div>
      </div>
    </div>
  </div>
  
<!-- end -->
<c:import url="footer.jsp"/>