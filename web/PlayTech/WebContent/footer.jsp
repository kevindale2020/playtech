
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		
		$('#username').focus();
		/* login */
		$('.login-form').submit(function(e){
			e.preventDefault();
			var username = $('#username').val();
			var password = $('#password').val();

			if(username=="" || password=="") {

				if(username=="") {
					$('#errorUsername').addClass('alert alert-danger');
					$('#errorUsername').html('Username is required.');
				}
				if(password=="") {
					$('#errorPassword').addClass('alert alert-danger');
					$('#errorPassword').html('Password is required.');
				}
				return false;
			}

			 $.ajax({
				url: "UserServlet",
				method: "GET",
				data: $('.login-form').serialize(),
				success: function(data) {
					var data = JSON.parse(data);
					if(data.response=="1") {
						window.location.href="admin/index.jsp";
					} else {
						$('#errorLogin').addClass('alert alert-danger');
						$('#errorLogin').html(data.message);
						$('.login-form')[0].reset();
					}
			    },
			    error: function() {
		            console.log("Error");
		        }
			});
		});
		/* end */

		/* register */
       $('#register_form').submit(function(e){
        e.preventDefault();

        var fname = $('#fname').val();
        var lname = $('#lname').val();
        var address = $('#address').val();
        var email = $('#email').val();
        var contact = $('#contact').val();
        var username = $('#username').val();
        var password = $('#pass1').val();
        var password2 = $('#pass2').val();

        if(fname==""||lname==""||address==""||email==""||contact==""||username==""||password=="") {
          alert("Please fill-up all fields")
          return false;
        }

        if(password!=password2) {
        	alert("Password does not match with the confirm password");
        	return false;
        }

        if(password.length<8) {
        	alert("Password should at least be 8 characters");
        	return false;
        }

        $.ajax({
          url: "UserServlet",
          method: "POST",
          data: $('#register_form').serialize(),
          success: function(data) {
        	var data = JSON.parse(data);
        	if(data.response=="1") {
        		$('#success').addClass('alert alert-success');
        		$('#success').html(data.message);
        		$('#register_form')[0].reset();
        	} else {
        		$('#success').addClass('alert alert-danger');
        		$('#success').html(data.message);
        		$('#register_form')[0].reset();
        	}
          },
          error: function() {
        	console.log("Error");
          }
        });
      });
       /* end */
	});
</script>
</body>
</html>