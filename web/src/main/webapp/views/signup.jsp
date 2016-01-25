<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Sign up</title>

    <jsp:include page="/template/headImport.jsp"/>
    <!-- x-editable (bootstrap version) -->
    <link href="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.4.6/bootstrap-editable/css/bootstrap-editable.css"
          rel="stylesheet"/>

    <link href="../resources/css/lib/select2.css" rel="stylesheet" type="text/css">
    <link href="../resources/css/lib/awesome-bootstrap-checkbox.css" rel="stylesheet" type="text/css">
    <link href="../resources/css/signup/signup.css" rel="stylesheet" type="text/css">

</head>
<body>
<jsp:include page="/template/header.jsp"/>
<div class="container">
    <div class="row">
        <div class="header-offset"></div>
        <div
                class="col-xs-12 col-sm-8 col-md-4 col-sm-offset-2 col-md-offset-4">
            <div class="panel panel-primary animated fadeIn cabinet-top">
                <div class="panel-heading">
                    <div class="panel-title">
                        <i class="fa fa-edit"></i> Sign up as ${role}
                        <div class="pull-right animated bounceIn">
                            <div class="col-xs-6 col-sm-6 col-md-6">
                                <div class="form-group">
                                    <div class="social-btn linkedin">
                                        <img alt="LInkedin" class="social-img hvr-grow"
                                             src="${pageContext.request.contextPath}/resources/img/social_icon/linkedin_icon.png"/>
                                    </div>
                                </div>
                            </div>
                            <div class  ="col-xs-6 col-sm-6 col-md-6">
                                <div class="form-group">
                                    <div class="social-btn google-plus">
                                        <img alt="Google_plus" class="social-img hvr-grow"
                                             src="${pageContext.request.contextPath}/resources/img/social_icon/google_plus_icon.png"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <c:if test="${notAvailableEmail != null}">
                        <div
                                class="alert alert-message alert-message-danger animated fadeInUp">
                            <a href="#" class="close"> &times; </a>
                            <h4>Error</h4>

                            <p>This email has been already used</p>
                        </div>
                    </c:if>
                    <c:if test="${error_message != null}">
                        <div
                                class="alert alert-message alert-message-danger animated fadeInUp">
                            <a href="#" class="close"> &times; </a>
                            <h4>Error</h4>

                            <p>${error_message}</p>
                        </div>
                    </c:if>
                    <c:if test="${notEqualsPasswords != null}">
                        <div
                                class="alert alert-message alert-message-danger animated fadeInUp">
                            <a href="#" class="close"> &times; </a>
                            <h4>Error</h4>

                            <p>Passwords are not equals</p>
                        </div>
                    </c:if>
                    <c:if test="${notCorrectData != null}">
                        <div
                                class="alert alert-message alert-message-danger animated fadeInUp">
                            <a href="#" class="close"> &times; </a>
                            <h4>Error</h4>

                            <p>You fail while signup</p>
                        </div>
                    </c:if>
                    <form role="form" method="post" id="registerForm"
                          action="${pageContext.request.contextPath}/user/create?role=${role}"
                          class="animated fadeInUp">

                        <input type="hidden" name="role" value="${role}">

                        <div class="row">
                            <div class="col-xs-6 col-sm-6 col-md-6">
                                <div class="form-group">
                                    <input type="text" name="first_name" id="first_name" required
                                           class="form-control input-sm" placeholder="First name"
                                           autofocus>
                                </div>
                            </div>
                            <div class="col-xs-6 col-sm-6 col-md-6">
                                <div class="form-group">
                                    <input type="text" name="last_name" id="last_name"
                                           class="form-control input-sm" placeholder="Last name">
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <input name="email" id="email" class="form-control input-sm"
                                   placeholder="Email" required>
                        </div>

                        <div class="row">
                            <div class="col-xs-6 col-sm-6 col-md-6">
                                <div class="form-group">
                                    <input type="password" name="password" id="password"
                                           class="form-control input-sm" placeholder="Enter password" required>
                                </div>
                            </div>
                            <div class="col-xs-6 col-sm-6 col-md-6">
                                <div class="form-group">
                                    <input type="password" name="password_confirmation"
                                           id="password_confirmation" class="form-control input-sm"
                                           placeholder="Confirm password" required>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-xs-1" data-tooltip="yes" title="Autodetect" data-placement="left">
                                <div class="checkbox checkbox-primary checkbox-circle">
                                    <input id="autoDetect" class="styled" type="checkbox">
                                    <label for="autoDetect">
                                        <%--Autodetect--%>
                                    </label>
                                </div>
                            </div>
                            <div class="col-xs-10">
                                <select class="form-control" name="country"
                                        id="country"></select>
                            </div>
                        </div>

                        <input type="hidden" name="timeZone" id="timeZone">

                        <button type="submit" id="addMeBtn" class="btn btn-primary btn-block">Add
                            me!
                        </button>

                    </form>
                </div>
            </div>
        </div>
    </div>
</div>


<jsp:include page="/template/footImport.jsp"/>

<script src="http://code.jquery.com/jquery-2.0.3.min.js"></script>
<script src="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.4.6/bootstrap-editable/js/bootstrap-editable.min.js"></script>
<script src="../resources/js/lib/select2.js"></script>

<!-- main.js -->
<script src="../resources/js/signup/signup.js"></script>
<script charset="UTF-8" src="/resources/js/home.js"></script>

</body>
</html>
