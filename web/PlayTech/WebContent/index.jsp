<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:import url="header.jsp"/>
<br/><br/>
<section class="container-fluid">
            <div class="row justify-content-center  ">

                <div class="col-4 rounded border shadow p-3 mb-5 bg-white " id="col-Login" >
                        <div class="login-logo">
                            <img src="images/logo_playtech2.png">
                        </div>
                            <!-- <p class="text-center"><strong>Login Admin</strong></p> -->
                        
                            <form class="login-form" method="GET">
                                <input type="hidden" id="action" name="action" value="login">
                                <div class="form-group" id="errorLogin"></div>
                                <div class="form-group">
                                    <label >Username</label>
                                    <div id="errorUsername"></div>
                                    <input type="text" id="username" name="username" class="form-control" placeholder="Username">    
                                </div>
                                <div class="form-group">
                                    <label >Password</label>
                                    <div id="errorPassword"></div>
                                    <input type="password" class="form-control"  id="password" name="password"  placeholder="Password">
                                </div>
                                <a href="register.jsp">Create Account</a>
                                <div class="form-group">
                                    <button id="btnLogin" class="btn btn-primary float-right">Login</button>                              
                                </div>
                            </form>
                        </div>
                </div>
            </section>
<c:import url="footer.jsp"/>