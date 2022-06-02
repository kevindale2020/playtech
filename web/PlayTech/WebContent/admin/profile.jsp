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
<!-- Begin Page Content -->
        <div class="container-fluid">
<div class="container py-2">
    <div class="row my-2">
        <!-- edit form column -->
        <div class="col-lg-4">
            <h2 class="text-center font-weight-light">Admin Profile</h2>
        </div>
        <div class="col-lg-8">
            <div id="success"></div>
            <div id="error"></div>
        </div>
        <div class="col-lg-8 order-lg-1 personal-info">

                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">First name</label>
                    <div class="col-lg-9">
                        <input id="fname" class="form-control" type="text"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Last name</label>
                    <div class="col-lg-9">
                        <input id="lname" class="form-control" type="text"  />
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Address</label>
                    <div class="col-lg-9">
                        <input id="address" class="form-control" type="text" placeholder="Street" />
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Email</label>
                    <div class="col-lg-9">
                        <input id="email" class="form-control" type="email" />
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Contact</label>
                    <div class="col-lg-9">
                        <input id="contact" class="form-control" type="text" />
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Username</label>
                    <div class="col-lg-9">
                        <input id="username" class="form-control" type="text" />
                    </div>
                </div>
                 <div class="form-group row">
                    <div class="col-lg-9 ml-auto text-right">
                        <input id="btnSave" type="submit" class="btn btn-primary" value="Save Changes" />
                    </div>
                </div>
    
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Current Password</label>
                    <div class="col-lg-9">
                        <input id="current" class="form-control" type="password" />
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">New Password</label>
                    <div class="col-lg-9">
                        <input id="newpass" class="form-control" type="password" />
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Confirm password</label>
                    <div class="col-lg-9">
                        <input id="confirm" class="form-control" type="password" />
                    </div>
                </div>
                 <div class="form-group row">
                    <div class="col-lg-9 ml-auto text-right">
                        <input id="btnChange" type="submit" class="btn btn-primary" value="Change Password" />
                    </div>
                </div>
       
        </div>
        <div class="col-lg-4 order-lg-0 text-center">
           <img id="profileImage" class="mx-auto img-fluid rounded-circle" alt="avatar" />
           
            <h6 class="my-4">Upload a new photo</h6>
            <form id="image_form" method="post" enctype="multipart/form-data">
            <div class="input-group px-xl-4">
                <div class="custom-file">
                    <input type="file" id="inputGroupFile04" name="image">
                    <label class="custom-file-label" for="inputGroupFile04" aria-describedby="inputGroupFileAddon04">Choose file</label>
                </div>
                 <input type="hidden" name="action" id="action" value="image_upload" />
                 <input type="hidden" name="user_old_image" id="user_old_image"   />
                <div class="input-group-append">
                    <button class="btn btn-secondary" type="submit" name="insert" id="insert"><i class="fa fa-upload"></i></button>
                </div>
            </div>
          </form>
        </div>
    </div>
</div>

        </div>
        <!-- /.container-fluid -->
<c:import url="footer.jsp"/>