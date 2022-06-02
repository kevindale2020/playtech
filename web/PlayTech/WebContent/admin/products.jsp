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
<h1>Products</h1>
	<br/>
	<div class="row">
		<div class="col">
			<div class="py-3">
				<button id="btnAdd" class="btn btn-success"><i class="fa fa-plus"></i> Add New</button>
			</div>
      <div id="success"></div>
			<div id="products"></div>
		</div>
	</div>

	<!-- Product Modal -->
  <div class="modal fade" id="product_modal">
    <div class="modal-dialog">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title">Add New Product</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
          <div id="message"></div>
          <form id="product_form" class="form-horizontal" method="post" enctype="multipart/form-data">
          <!-- upload image -->
          <div class="input-group mb-3">
            <div class="custom-file">
              <input type="file" id="inputGroupFile02" name="image" size="60">
              <label class="custom-file-label" for="inputGroupFile02" aria-describedby="inputGroupFileAddon02">Choose file</label>
            </div>
            <input type="hidden" name="action" id="action" value="new_product" />
          </div>
          <div class="input-group mb-3">
            <input type="text" class="form-control" id="name" name="name" placeholder="Name">
            <div id="nameErr"></div>
          </div>
          <div class="input-group mb-3">
            <textarea class="form-control" rows="5" id="desc" name="desc" placeholder="Description"></textarea>
            <div id="descriptionErr"></div>
          </div>
          <div class="input-group mb-3">
            <input type="number" class="form-control" id="qty" name="qty" placeholder="Stock">
            <div id="qtyErr"></div>
          </div>
           <div class="input-group mb-3">
            <input type="number" class="form-control" id="price" name="price" placeholder="Price">
            <div id="priceErr"></div>
          </div>    
          <div class="form-group">
            <button id="btnSubmit" name="btnSubmit" class="btn btn-primary">Submit</button>
          </div>
        </form>

        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
        
      </div>
    </div>
  </div>

  <!-- Edit Product Modal -->
  <div class="modal fade" id="edit_product_modal">
    <div class="modal-dialog">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title">Edit Product</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
          <form id="edit_product_form" class="form-horizontal" method="post" enctype="multipart/form-data">
          <!-- upload image -->
          <div class="input-group mb-3">
          	<img id="prodImage" class="img-thumbnail">
          </div>
          <div class="input-group mb-3">
            <div class="custom-file">
              <input type="file" id="inputGroupFile03" name="image">
              <label class="custom-file-label" for="inputGroupFile03" aria-describedby="inputGroupFileAddon03">Choose file</label>
            </div>
             <input type="hidden" name="action" id="action" value="save_product" />
            <input type="hidden" name="pid" id="pid" />
            <input type="hidden" name="product_old_image" id="product_old_image" />
          </div>
          <div class="input-group mb-3">
            <input type="text" class="form-control" id="cname" name="cname" placeholder="Name">
            <div id="cnameErr"></div>
          </div>
          <div class="input-group mb-3">
            <textarea class="form-control" rows="5" id="cdesc" name="cdesc" placeholder="Description"></textarea>
            <div id="cdescriptionErr"></div>
          </div>
          <div class="input-group mb-3">
            <input type="number" class="form-control" id="cstock" name="cstock" placeholder="Stock">
            <div id="cstockErr"></div>
          </div>
           <div class="input-group mb-3">
            <input type="number" class="form-control" id="cprice" name="cprice" placeholder="Price">
            <div id="cpriceErr"></div>
          </div>    
          <div class="form-group">
            <button id="btnSave" name="btnSave" class="btn btn-primary">Save</button>
          </div>
        </form>

        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
        
      </div>
    </div>
  </div>
</div>
<c:import url="footer.jsp"/>