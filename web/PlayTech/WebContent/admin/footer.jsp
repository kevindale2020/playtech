
<!---footer---->
    <div class="navbar navbar-default navbar-fixed-bottom">
    <div class="container">
      <p class="navbar-text pull-right">© 2021 PlayTech | All Rights Reserved</p>
    </div>
    
    
  </div>
</div>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="../js/datatables/jquery.dataTables.min.js"></script>
<script src="../js/datatables/dataTables.bootstrap4.min.js"></script>
<script type="text/javascript">

	function getProducts() {
		
		$.ajax({
			url: "../AdminServlet",
			method: "POST",
			async: false,
			data: {
				"action": "product_list"
			},
			success: function(data) {
				console.log(data);
				$('#products').html(data);
				 $('#dataTable').DataTable({ 
		         	"destroy": true, //use for reinitialize datatable
		         });
			}
		});
	}
	
function getUsers() {
		
		$.ajax({
			url: "../UserServlet",
			method: "POST",
			async: false,
			data: {
				"action": "user_lists"
			},
			success: function(data) {
				console.log(data);
				$('#users').html(data);
				 $('#dataTable2').DataTable({ 
		         	"destroy": true, //use for reinitialize datatable
		         });
			}
		});
	}
	
	function getProfile() {
		$.ajax({
			url: "../UserServlet",
			method: "POST",
			async: false,
			data: {
				"action": "admin_profile"
			},
			success: function(data) {
				var data = JSON.parse(data);
				console.log(data);
				$('#fname').val(data.fname);
				$('#lname').val(data.lname);
				$('#address').val(data.address);
				$('#email').val(data.email);
				$('#contact').val(data.contact);
				$('#username').val(data.username);
				$('#user_old_image').val(data.image);
				$('#profileImage').attr("src", "../images/users/"+data.image);
			}
		});
	}
	
	function getOrders() {
		$.ajax({
			url: "../AdminServlet",
			method: "POST",
			async: false,
			data: {
				"action": "admin_orders"
			},
			success:function(data) {
				console.log(data);
				$('#orders').html(data);
				 $('#dataTable3').DataTable({ 
		         	"destroy": true, //use for reinitialize datatable
		         });
			}
		});
	}
	
	function getCancelOrders() {
		$.ajax({
			url: "../AdminServlet",
			method: "POST",
			async: false,
			data: {
				"action": "admin_cancel_orders"
			},
			success:function(data) {
				console.log(data);
				$('#cancelOrders').html(data);
				 $('#dataTable4').DataTable({ 
		         	"destroy": true, //use for reinitialize datatable
		         });
			}
		});
	}
	
	function getConfirmedOrders() {
		$.ajax({
			url: "../AdminServlet",
			method: "POST",
			async: false,
			data: {
				"action": "admin_confirm_orders"
			},
			success:function(data) {
				console.log(data);
				$('#confirmedOrders').html(data);
				 $('#dataTable5').DataTable({ 
		         	"destroy": true, //use for reinitialize datatable
		         });
			}
		});
	}
	
	function getClosedOrders() {
		$.ajax({
			url: "../AdminServlet",
			method: "POST",
			async: false,
			data: {
				"action": "admin_closed_orders"
			},
			success:function(data) {
				console.log(data);
				$('#closedOrders').html(data);
				 $('#dataTable6').DataTable({ 
		         	"destroy": true, //use for reinitialize datatable
		         });
			}
		});
	}
	
	$(document).ready(function(){
		getProducts();
		getUsers();
		getProfile();
		getOrders();
		getCancelOrders();
		getConfirmedOrders();
		getClosedOrders();
		
		/* edit profile */
		 $('#btnSave').on('click', function(){
	      var fname = $('#fname').val();
	      var lname = $('#lname').val();
	      var address = $('#address').val();
	      var email = $('#email').val();
	      var contact = $('#contact').val();
	      var username = $('#username').val();
	
	      if(fname=="" || lname=="" || address=="" || email=="" || contact=="" || username=="") {
	         alert('Please fill all the fields');
	         return false;
	      }
	
	       $.ajax({
	          url: "../UserServlet",
	          type: "POST",
	          async: false,
	          data: {
	            "action": "edit_profile",
	            "fname": fname,
	            "lname": lname,
	            "address": address,
	            "email": email,
	            "contact": contact,
	            "username": username
	          },
	          success: function(data) {
	        	var data = JSON.parse(data);
	        	console.log(data);
	            $('#success').addClass('alert alert-success');
	            $('#success').html(data.message);
	            getProfile();
	          }
	        });
    	});
		/* end */
		
		/* change password */
		$('#btnChange').on('click', function(){
	      var current = $('#current').val();
	      var newpass = $('#newpass').val();
	      var confirm = $('#confirm').val();
	
	      if(current=="" || newpass=="" || confirm=="") {
	        alert('Please fill all the fields');
	        return false;
	      }
	
	      if(newpass != confirm) {
	        $('#error').addClass('alert alert-danger');
	        $('#error').html('Password does not match');
	        return false;
	      }
	
	      $.ajax({
	         url: "../UserServlet",
	          type: "POST",
	          async: false,
	          data: {
	            "action": "change_password",
	            "current": current,
	            "newpass": newpass
	          },
	          success: function(data) {
	            var data = JSON.parse(data);
	            console.log(data);
	            if(data.response=="1") {
	              $('#current').val('');
	              $('#newpass').val('');
	              $('#confirm').val('');
	              $('#success').addClass('alert alert-success');
	              $('#success').html(data.message);
	            } else {
	              $('#current').val('');
	              $('#newpass').val('');
	              $('#confirm').val('');
	              $('#error').addClass('alert alert-danger');
	              $('#error').html(data.message);
	            }
	          }
	      });
    	});
		/* end */
		
		/* image upload */
		$('#image_form').submit(function(event){
	      event.preventDefault();
	      
	      var image_name = $('#inputGroupFile04').val();
	      if(image_name=="") {
	        alert("Please Select Image");
	        return false;
	      } else {
	        var extension = $('#inputGroupFile04').val().split('.').pop().toLowerCase();
	        if(jQuery.inArray(extension, ['gif', 'png', 'jpg', 'jpeg']) == -1) {
	          alert("Invalid Image File");
	          $('#inputGroupFile04').val('');
	          return false;
	        } else {
	          $.ajax({
	            url: "../UserServlet",
	            method: "POST",
	            data: new FormData(this),
	            contentType: false,
	            processData: false,
	            success: function(data) {
	            var data = JSON.parse(data);
	            console.log(data);
	              if(data.response=="1") {
	            	  alert(data.message);
		              getProfile();
	              }
	            }
	          });
	        }
	      }
    	});
		/* end */
		
		/* trigger product modal */
		$('#btnAdd').click(function(){
			$('#product_modal').modal('show');
			$('#product_form')[0].reset();
		});
		/* end */
		
		/* add product */
		$('#product_form').submit(function(e){
			e.preventDefault();
			var name = $('#name').val();
			var desc = $('#desc').val();
			var qty = $('#qty').val();
			var price = $('#price').val();
			
			if(name==""||desc==""||qty==""||price=="") {
				if(name=="") {
					$('#name').addClass('is-invalid');
					$('#nameErr').addClass('invalid-feedback');
					$('#nameErr').html('Name is required');
				}
				if(desc=="") {
					$('#desc').addClass('is-invalid');
					$('#descriptionErr').addClass('invalid-feedback');
					$('#descriptionErr').html('Description is required');
				}
				if(qty=="") {
					$('#qty').addClass('is-invalid');
					$('#qtyErr').addClass('invalid-feedback');
					$('#qtyErr').html('Quantity is required');
				}
				if(price=="") {
					$('#price').addClass('is-invalid');
					$('#priceErr').addClass('invalid-feedback');
					$('#priceErr').html('Price is required');
				}
				return false;
			}
			
			$.ajax({
		          url: "../AdminServlet",
		          method: "POST",
		          data: new FormData(this),
		          contentType: false,
				  processData: false,
		          success: function(data) {
		        	var data = JSON.parse(data);
		        	if(data.response=="1") {
		        		$('#success').html(data.message);
		        		$('#success').addClass('alert alert-success');
		        		$('#product_modal').modal('hide');
		        		$('#product_form')[0].reset();
		        		getProducts();
		        	} else {
		        		$('#message').html(data.message);
		        		$('#message').addClass('alert alert-danger');
		        		$('#name').focusu();
		        		$('#product_form')[0].reset();
		        	}
		          },
		          error: function() {
		        	console.log("Error");
		          }
		   });
		});
		/* end */
		
		/* edit modal trigger */
		$(document).on('click', '.edit_product', function(){
			var pid = $(this).attr('id');
			
			$.ajax({
				url: "../AdminServlet",
				method: "POST",
				async: false,
				data: {
					"action": "product_details",
					"pid": pid
				},
				success: function(data) {
					var data = JSON.parse(data);
					//console.log(data);
					$('#pid').val(data.pid);
					$('#cname').val(data.name);
					$('#cdesc').val(data.desc);
					$('#cstock').val(data.stockin);
					$('#cprice').val(data.price);
					$('#prodImage').attr("src", "../images/products/"+data.image);
					$('#product_old_image').val(data.image);
					$('#edit_product_modal').modal('show');
				}
			});
		});
		/* end */
		
		/* edit product */
		$('#edit_product_form').submit(function(e){
			e.preventDefault();
			var name = $('#cname').val();
			var desc = $('#cdesc').val();
			var qty = $('#cstock').val();
			var price = $('#cprice').val();

			if(name==""||desc==""||qty==""||price=="") {
				if(name=="") {
					$('#cname').addClass('is-invalid');
					$('#cnameErr').addClass('invalid-feedback');
					$('#cnameErr').html('Name is required');
				}
				if(desc=="") {
					$('#cdesc').addClass('is-invalid');
					$('#cdescriptionErr').addClass('invalid-feedback');
					$('#cdescriptionErr').html('Description is required');
				}
				if(qty=="") {
					$('#cstock').addClass('is-invalid');
					$('#cstockErr').addClass('invalid-feedback');
					$('#cstocckErr').html('Stock is required');
				}
				if(price=="") {
					$('#cprice').addClass('is-invalid');
					$('#cpriceErr').addClass('invalid-feedback');
					$('#cpriceErr').html('Price is required');
				}
				return false;
			}

			$.ajax({
				url: "../AdminServlet",
				method: "POST",
				data: new FormData(this),
				contentType: false,
				processData: false,
				success: function(data) {
					var data = JSON.parse(data);
					console.log(data);
					$('#success').addClass('alert alert-success');
					$('#success').html(data.message);
					$('#edit_product_modal').modal('hide');
					$('#edit_product_form')[0].reset();
					getProducts();
				}
			});
		});
		/* end /*
		
		/* delete product */
		  $(document).on('click', '.delete_product', function(){
        if(confirm('Are you sure you want to delete this product?')) {
           var pid = $(this).attr("id");
          $.ajax({
              url: "../AdminServlet",
              method: "POST",
              async: false,
              data: {
                "action": "delete_product",
                "pid": pid
              },
              success: function(data) {
            	 var data = JSON.parse(data);
            	 if(data.response=="1") {
            		 $('#success').addClass('alert alert-success');
    				 $('#success').html(data.message);
                     getProducts();
            	 }
              }
           });
        } else {
          return false;
        }
      });
		/* end */
		
	/* trigger order details modal */
	$(document).on('click', '.order_details', function(){
		var id = $(this).attr('id');
		// alert(id);
		$('#title_order_details').html('Order#'+id);
		
		$.ajax({
			url: "../AdminServlet",
			method: "POST",
			async: false,
			data: {
				action: "admin_order_details",
				orderno: id
			},
			success: function(data) {
				console.log(data);
				$('#order_details').html(data);
				$('#order_details_modal').modal('show');
			}
		});
	});
	/* end */
	
	/* confirm order */
	$(document).on('click', '.confirm_order', function(){
		
		var id = $(this).attr('id');
		
		$.ajax({
			url: "../AdminServlet",
			method: "POST",
			async: false,
			data: {
				action: "admin_confirm_order",
				orderno: id
			},
			success: function(data) {
				var data = JSON.parse(data);
	           	if(data.response=="1") {
	           		$('#success').addClass('alert alert-success');
	   				$('#success').html(data.message);
	   				getOrders();
	   				getConfirmedOrders();
	            }
			}
		});
	});
	/* end */
	
	/* set for delivery */
	$(document).on('click', '.set_delivery', function(){
		
		 if(confirm('Are you sure you want to set this order out for delivery?')) {
		
			var id = $(this).attr('id');
			
			$.ajax({
				url: "../AdminServlet",
				method: "POST",
				async: false,
				data: {
					action: "admin_set_delivery",
					orderno: id
				},
				success: function(data) {
					var data = JSON.parse(data);
		           	if(data.response=="1") {
		           		$('#success').addClass('alert alert-success');
		   				$('#success').html(data.message);
		   				getConfirmedOrders();
		            }
				}
			});
		 } else {
			 return false;
		 }
	});
	/* end */
	
	/* report form */
	$('#report_form').submit(function(e){
		e.preventDefault();
		
		var start_date = $('#start_date').val();
		var end_date = $('#end_date').val();
		
		if(start_date=="" || end_date=="") {
			alert('Empty dates');
			return false;
		}
		
		$.ajax({
	          url: "../AdminServlet",
	          method: "POST",
	          data: new FormData(this),
	          contentType: false,
			  processData: false,
	          success: function(data) {
	        	$('#report_data').html(data);
	          }
	   });
	});
	/* end */
		
		/* select and display image */
     	$('#inputGroupFile02').on('change',function(){
        	//get the file name
        	var fileName = $(this).val();
        	//replace the "Choose a file" label
        	$(this).next('.custom-file-label').html(fileName);
      	});
		
     	$('#inputGroupFile03').on('change',function(){
        	//get the file name
        	var fileName = $(this).val();
        	//replace the "Choose a file" label
        	$(this).next('.custom-file-label').html(fileName);
      	});	
     	
     	$('#inputGroupFile04').on('change',function(){
        	//get the file name
        	var fileName = $(this).val();
        	//replace the "Choose a file" label
        	$(this).next('.custom-file-label').html(fileName);
      	});	
	});
</script>
</body>
</html>