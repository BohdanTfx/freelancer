<%--
  Created by IntelliJ IDEA.
  User: Максим
  Date: 21.01.2016
  Time: 23:29
  To change this template use File | Settings | File Templates.
--%>
<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Home page</title>
    <jsp:include page="/template/headImport.jsp"/>
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
<jsp:include page="/template/header.jsp"/>


<div class="cabinet-top">
    <jsp:include page="/template/navigation.jsp"/>
</div>


<div class="container">
    <ul id="nav-tabs" class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#history">History</a></li>
        <li><a data-toggle="tab" href="#tests">Tests</a></li>
    </ul>
    <div id="tab-content" class="panel panel-default">
        <div class="tab-content">
            <div id="history" class="tab-pane fade in active">

                <%--start header--%>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="panel-title col-md-12">
                                <div class="col-md-4 col-sm-5 col-xs-9">
                                    <c:out value="Test"/>
                                </div>
                                <div class="col-md-4 col-sm-5 hidden-xs">
                                    <c:out value="Technology"/>
                                </div>
                                <div class="col-md-2 hidden-sm hidden-xs">
                                    <c:out value="Expire date"/>
                                </div>
                                <div class="badge col-md-2 col-sm-2 col-xs-3">
                                    <c:out value="Your Rate"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%--end header--%>

                <c:forEach items="${devQAs}" var="devQA">

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="col-md-4 col-sm-5 col-xs-9">
                                        <c:out value="${devQA.test.name}"/>
                                    </div>
                                    <div class="col-md-4 col-sm-5 hidden-xs">
                                        <c:out value="${devQA.test.technology.name}"/>
                                    </div>
                                    <c:choose>
                                        <c:when test="${devQA.isExpire==true}">
                                            <div class="col-md-2 hidden-sm hidden-xs">
                                                <div class="red-color">
                                                    <c:out value="${devQA.expire}"/>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="col-md-2 hidden-sm hidden-xs">
                                                <div class="green-color">
                                                    <c:out value="${devQA.expire}"/>
                                                </div>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

                                    <c:choose>
                                        <c:when test="${devQA.rate}<${devQA.test.passScore}">
                                            <div class="badge col-md-2 col-sm-2 col-xs-3">
                                                <div class="red-color">
                                                    <c:out value="${devQA.rate}"/>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="badge col-md-2 col-sm-2 col-xs-2">
                                                <div class="green-color">
                                                    <c:out value="${devQA.rate}"/>
                                                </div>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div id="tests" class="tab-pane fade">
                <%--start test--%>
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <div class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#order_1">
                                <c:out value="Order title"/>
                            </a>

                            <div class="badge pull-right">
                                <c:out value="pay_type"/>
                            </div>
                        </div>
                    </div>
                    <div id="order_2" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-xs-10">
                                    <c:out value="Order description"/>
                                </div>
                                <div class="col-xs-2"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <%--end test--%>
            </div>
        </div>
    </div>
</div>


<jsp:include page="/template/footImport.jsp"/>
<script
        src="${pageContext.request.contextPath}/resources/js/lib/jquery.fullpage.min.js"></script>
<script
        src="${pageContext.request.contextPath}/resources/js/navigation.js"></script>
</body>
</html>