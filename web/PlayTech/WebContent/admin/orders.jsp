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
<h1>Orders</h1>
	<br/>
	<div class="row">
		<div class="col">
			<div id="success"></div>
				<div id="orders"></div>
				<div id="cancelOrders"></div>
				<div id="confirmedOrders"></div>
				<div id="closedOrders"></div>
			</div>
		</div>
	</div>
	
		<!-- Order Details Modal -->
  <div class="modal fade" id="order_details_modal">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title" id="title_order_details"></h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
         <div id="order_details"></div>
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
        
      </div>
    </div>
  </div>

  
<c:import url="footer.jsp"/>