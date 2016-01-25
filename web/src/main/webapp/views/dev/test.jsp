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
                <div class="history-title">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="panel-title col-md-12">
                                    <div class="col-md-4 col-sm-5 col-xs-9">
                                        <a id="history_title_test"><i class="fa fa-tasks"><c:out value=" Test"/></i></a>
                                    </div>
                                    <div class="col-md-4 col-sm-5 hidden-xs">
                                        <a id="history_title_technology"><i class="fa fa-tv"><c:out
                                                value=" Technology"/></i></a>
                                    </div>
                                    <div class="col-md-2 hidden-sm hidden-xs">
                                        <i class="fa fa-calendar-o"><c:out value=" Expire date"/></i>
                                    </div>
                                    <div class="col-md-2 col-sm-2 col-xs-3 text-center">
                                        <i class="fa fa-bar-chart"><c:out value=" Your Rate"/></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%--end header--%>
                <div id="history_list">
                    <c:forEach items="${devQAs}" var="devQA">
                        <div class="panel panel-default">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="panel-heading">
                                        <div class="col-md-4 col-sm-5 col-xs-9">
                                            <c:out value="${devQA.test.name}"/>
                                        </div>
                                        <div class="col-md-4 col-sm-5 hidden-xs">
                                            <c:out value="${devQA.test.technology.name}"/>
                                        </div>
                                        <c:choose>
                                            <c:when test="${devQA.isExpire==true}">
                                                <div class="col-md-2 hidden-sm hidden-xs">
                                                    <i class="fa fa-calendar-times-o">
                                                    <span class="red-color">
                                                        <c:out value="${devQA.expire}"/>
                                                    </span>
                                                    </i>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="col-md-2 hidden-sm hidden-xs">
                                                    <i class="fa fa-calendar-check-o">
                                                        <span class="green-color"><c:out
                                                                value="${devQA.expire}"/></span>
                                                    </i>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>

                                        <c:choose>
                                            <c:when test="${devQA.rate < devQA.test.passScore}">
                                                <div class="col-md-2 col-sm-2 hidden-xs">
                                                    <div class="progress">
                                                        <span class="progress-text"><c:out
                                                                value="${devQA.rate}%"/></span>

                                                        <div class="progress-bar progress-bar-danger" role="progressbar"
                                                             aria-valuenow="40" aria-valuemin="0" aria-valuemax="100"
                                                             style="width:${devQA.rate}%">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-xs-3 hidden-sm hidden-md hidden-lg">
                                                <span class="text-center progress-text red-color"><c:out
                                                        value="${devQA.rate}%"/></span>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="col-md-2 col-sm-2 hidden-xs">
                                                    <div class="progress">
                                                        <span class="progress-text"><c:out
                                                                value="${devQA.rate}%"/></span>

                                                        <div class="progress-bar progress-bar-success"
                                                             role="progressbar"
                                                             aria-valuenow="40" aria-valuemin="0" aria-valuemax="100"
                                                             style="width:${devQA.rate}%">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-xs-3 hidden-sm hidden-md hidden-lg">
                                                <span class="text-center progress-text green-color"><c:out
                                                        value="${devQA.rate}%"/></span>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div id="tests" class="tab-pane fade">
                <div class="row filter">
                    <div class="form-group col-xs-12">
                        <label for="all">All:</label>
                        <input type="text" ng-model="search.$" class="form-control" id="all">
                    </div>
                    <div class="form-group col-md-6 col-xs-12">
                        <label for="tech">Technology:</label>
                        <input type="text" ng-model="search.$" class="form-control" id="tech">
                    </div>
                </div>

                <%--start header--%>
                <div class="history-title">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="panel-title col-md-12">
                                    <div class="col-md-4 col-sm-5 col-xs-5">
                                        <i class="fa fa-tasks"><c:out value=" Test"/></i>
                                    </div>
                                    <div class="col-md-4 col-sm-5 col-xs-4">
                                        <i class="fa fa-tv"><c:out value=" Technology"/></i>
                                    </div>
                                    <div class="col-md-2 hidden-sm hidden-xs">
                                        <i class="fa fa-bar-chart"><c:out value=" Pass Score"/></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%--end header--%>

                <c:forEach items="${tests}" var="test">
                    <div class="panel panel-default">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="panel-heading">
                                    <div class="col-md-4 col-sm-5 col-xs-5">
                                        <c:out value="${test.name}"/>
                                    </div>
                                    <div class="col-md-4 col-sm-5 col-xs-4">
                                        <c:out value="${test.technology.name}"/>
                                    </div>
                                    <div class="col-md-2 hidden-sm hidden-xs">
                                        <c:out value="${test.passScore}%"/>
                                    </div>
                                    <div class="col-md-2 col-sm-2 col-xs-3">
                                        <a class="btn btn-success btn-pass-test" href="test_${test.id}">Pass test</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
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