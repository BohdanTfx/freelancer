<%--
  Created by IntelliJ IDEA.
  User: Максим
  Date: 21.01.2016
  Time: 23:29
  To change this template use File | Settings | File Templates.
--%>
<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>Home page</title>
  <jsp:include page="/template/headImport.jsp" />
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/resources/css/lib/jquery.fullpage.min.css">
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/resources/css/navigation.css">
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/resources/css/main.css">
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/resources/css/test.css">

</head>
<body>
<jsp:include page="/template/header.jsp" />


<div class="cabinet-top">
  <jsp:include page="/template/navigation.jsp" />
</div>

<div class="container">
  <div class="tabbable">
    <div class="row">
      <ul class="nav nav-tabs">
        <li class="active"><a class="" href="#tab1_history" data-toggle="tab">History</a>
        </li>
        <li><a class="" href="#tab2_test" data-toggle="tab">Tests</a>
        </li>
      </ul>
      <div class="tab-content">
        <div class="tab-pane active" id="tab1_history">
          <div class="list-group">
            <a href="#" class="list-group-item active">
              <h4 class="list-group-item-heading">List group item heading</h4>
              <p class="list-group-item-text">...</p>
            </a>
            <a href="#" class="list-group-item">
              <h4 class="list-group-item-heading">List group item heading</h4>
              <p class="list-group-item-text">...</p>
            </a>
            <c:forEach items="${passed_tests}" var="p_test">
              <a href="#" class="list-group-item">
                <div class="list-group-item-heading">
                  <div class="panel-title">
                    <a data-toggle="collapse" data-parent="#accordion"
                       href="#order_${p_test.id}">
                      <c:out value="${p_test.title}" />
                    </a>
                    <div class="badge pull-right">
                      <c:out value="${p_test.payType}" />
                    </div>
                  </div>
                </div>
                <div id="order_${p_test.id}" class="list-group-item-text">
                  <div class="row">
                    <div class="col-xs-10">
                      <c:out value="${p_test.descr}" />
                    </div>
                    <div class="col-xs-2"></div>
                  </div>
                </div>
              </a>
            </c:forEach>
          </div>
        </div>
        <div class="tab-pane active" id="tab2_test">
          <div class="col-md-6">
            <h2 class="">This is what this is all about</h2>

            <p class="">Lebowski ipsum hERE'S WHAT HAPPENS, LARRY! Your "revolution" is over,
              Mr. Lebowski! Condolences! The bums lost! We've got a man down, Dude. Finishing
              my coffee. Nice marmot. Eight-year-olds, Dude. They won't hurt us, Donny.
              These men are cowards. You don't go out and make a living dressed like
              that in the middle of a weekday. They're nihilists. Look, Larry… Have you
              ever heard of Vietnam? Well sir, it's this rug I have, really tied the
              room together.</p>
            <br class="">
            <br class="">
          </div>

          <div class="col-md-6">
            <h2 class="">For More Information</h2>

            <p class="">Lebowski ipsum look, Larry… Have you ever heard of Vietnam? They won't
              hurt us, Donny. These men are cowards. I don't see any connection to Vietnam,
              Walter. When will you find these guys? I mean, do you have any promising
              leads? Every time a rug is micturated upon in this fair city, I have to
              compensate.</p>
            <p class="">Would it be possible for me to get my twenty grand in cash? I gotta check
              this with my accountant of course. Whose toe was it, Walter? Forget it,
              Donny. You're out of your element.</p>
          </div>
        </div>
      </div>
    </div>
    <!-- end row -->
  </div>
  <!-- end tabbable -->
</div>
<!-- end main container -->




<jsp:include page="/template/footImport.jsp" />
<script
        src="${pageContext.request.contextPath}/resources/js/lib/jquery.fullpage.min.js"></script>
<script
        src="${pageContext.request.contextPath}/resources/js/navigation.js"></script>
</body>
</html>