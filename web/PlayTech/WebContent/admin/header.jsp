<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>PlayTech</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"/>
   <link rel="stylesheet" type="text/css" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
  <style>
  	body{background:#f9f9f9;}
#wrapper{padding:90px 15px;}
.navbar-expand-lg .navbar-nav.side-nav{flex-direction: column;}
.card{margin-bottom: 15px; border-radius:0; box-shadow: 0 3px 5px rgba(0,0,0,.1); }
.header-top{box-shadow: 0 3px 5px rgba(0,0,0,.1)}
@media(min-width:992px) {
#wrapper{margin-left: 200px;padding: 90px 15px 15px;}
.navbar-nav.side-nav{background: #080808;box-shadow: 2px 1px 2px rgba(0,0,0,.1);position:fixed;top:56px;flex-direction: column!important;left:0;width:200px;overflow-y:auto;bottom:0;overflow-x:hidden;padding-bottom:40px}
}
/* navbar */
.navbar-brand img {
	width: 100%;
	height: auto;
}

.side-nav {
	margin: 0 !important;
	padding: 20px 10px !important;
	top: 75px !important;
}

.navbar-light .navbar-nav .nav-link {
    color: #9d9d9d !important;
}

/* change the color of active or hovered links */
.navbar-light .nav-item.active .nav-link,
.navbar-light .nav-item:focus .nav-link,
.navbar-light .nav-item:hover .nav-link {
    color: #003791 !important;
}

.bg-light {
    background-color: #ffffff!important;
}

/* end */

/* modal */
.modal-lg {
    max-width: 80% !important;
}
/* end */

.btn-primary {
	color: #fff;
	background-color: #003791 !important;;
	border-color: #003791 !important;;
}
	
.btn-primary:hover {
	color: #fff;
	background-color: #002d78 !important;;
	border-color: #002d78 !important;;
}
  	
  </style>
</head>
<body>
<div id="wrapper">
    <nav class="navbar header-top fixed-top navbar-expand-lg  navbar-light bg-light">
      <a class="navbar-brand" href="#"><img src="../images/logo_playtech2.png"></a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText" aria-controls="navbarText"
        aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav side-nav">
          <li class="nav-item">
            <a class="nav-link" href="users.jsp">Users
              <span class="sr-only">(current)</span>
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="products.jsp">Products</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="orders.jsp">Orders</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="reports.jsp">Reports</a>
          </li>
        </ul>
        <ul class="navbar-nav ml-md-auto d-md-flex">
          <li class="nav-item">
            <a class="nav-link" href="index.jsp">Home
              <span class="sr-only">(current)</span>
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" style="text-decoration: underline; color:  #017db3"></a>
          </li>
          <!-- Dropdown -->
		    <li class="nav-item dropdown">
		      <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
		        Welcome <%=session.getAttribute("username") %>
		      </a>
		      <div class="dropdown-menu">
		        <a class="dropdown-item" href="profile.jsp">My Profile</a>
		        <a class="dropdown-item" href="http://localhost:8081/PlayTech/AdminServlet?action=logout">Logout</a>
		      </div>
		    </li>
		     <li class="nav-item">
            <a class="nav-link" style="text-decoration: underline; color:  #017db3"></a>
          </li>
        </ul>
      </div>
    </nav>
