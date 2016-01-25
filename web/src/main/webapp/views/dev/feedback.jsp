<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="/template/headImport.jsp" />
    <title>Info dev page</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/css/lib/jquery.fullpage.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/css/navigation.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/css/dev/feedback.css">


</head>
<body>
<jsp:include page="/template/header.jsp" />
<jsp:include page="/template/navigation.jsp" />

<div class="container">
    <div class="row">
        <div class="col-sm-10 col-sm-offset-1" id="logout">

            <div class="comment-tabs">

                <div class="tab-content">
                    <div class="tab-pane active" id="comments-logout">
                        <ul class="media-list">
                            <li class="media">
                                <a class="pull-left hidden-xs" href="#">
                                    <object class="media-object img-circle" data="https://s3.amazonaws.com/uifaces/faces/twitter/dancounsell/128.jpg" type="image/png">
                                        <img class="media-object img-circle" src="${pageContext.request.contextPath}/resources/img/bg/default_logo.jpg">
                                    </object>
                                </a>
                                <div class="media-body">
                                    <div class="well well-lg">

                                        <h4 class="media-heading text-uppercase reviews">Marco Hugo</h4>

                                        <ul class="media-date text-uppercase reviews list-inline">
                                            <li class="dd">22</li>
                                            <li class="mm">09</li>
                                            <li class="aaaa">2014</li>
                                        </ul>
                                        <p class="media-comment">
                                            Great snippet! Thanks for sharing.
                                        </p>
                                        <a class="btn btn-info btn-circle text-uppercase" href="#" id="reply"><span class="glyphicon glyphicon-share-alt"></span> Complain</a>
                                    </div>
                                </div>

                            </li>
                            <li class="media">
                                <a class="pull-left hidden-xs" href="#">
                                    <object class="media-object img-circle" data="https://s3.amazonaws.com/uifaces/faces/twitter/kurafire/128.jpg" type="image/png">
                                        <img class="media-object img-circle" src="${pageContext.request.contextPath}/resources/img/bg/default_logo.jpg">
                                    </object>
                                </a>
                                <div class="media-body">
                                    <div class="well well-lg">
                                        <h4 class="media-heading text-uppercase reviews">Nico</h4>
                                        <ul class="media-date text-uppercase reviews list-inline">
                                            <li class="dd">22</li>
                                            <li class="mm">09</li>
                                            <li class="aaaa">2014</li>
                                        </ul>
                                        <p class="media-comment">
                                            I'm looking for that. Thanks!
                                        </p>
                                        <a class="btn btn-info btn-circle text-uppercase" href="#" id="reply"><span class="glyphicon glyphicon-share-alt"></span> Complain</a>
                                    </div>
                                </div>
                            </li>
                            <li class="media">
                                <a class="pull-left hidden-xs" href="#">
                                    <object class="media-object img-circle" data="https://s3.amazonaws.com/uifaces/faces/twitter/lady_katherine/128.jpg" type="image/png">
                                        <img class="media-object img-circle" src="${pageContext.request.contextPath}/resources/img/bg/default_logo.jpg" alt="profile">
                                    </object>
                                </a>
                                <div class="media-body">
                                    <div class="well well-lg">
                                        <h4 class="media-heading text-uppercase reviews">Kriztine</h4>
                                        <ul class="media-date text-uppercase reviews list-inline">
                                            <li class="dd">22</li>
                                            <li class="mm">09</li>
                                            <li class="aaaa">2014</li>
                                        </ul>
                                        <p class="media-comment">
                                            Yehhhh... Thanks for sharing.
                                        </p>
                                        <a class="btn btn-info btn-circle text-uppercase" href="#" id="reply"><span class="glyphicon glyphicon-share-alt"></span> Complain</a>
                                    </div>
                                </div>

                            </li>
                        </ul>
                    </div>


                </div>
            </div>
        </div>
    </div>

</div>

<jsp:include page="/template/footImport.jsp" />
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/navigation.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/lib/jquery.fullpage.min.js"></script>
</body>
</html>
