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
    <jsp:include page="${pageContext.request.contextPath}/template/navigationImport.jsp"/>
   <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/css/lib/jquery.fullpage.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css"/>
</head>
<body>
<jsp:include page="/template/header.jsp" />
<jsp:include page="${pageContext.request.contextPath}/template/navigation.jsp"/>

<div class="menu-offset cabinet-top">
    <%--PORTFOLIO CONTENT--%>
</div>



<jsp:include page="/template/footImport.jsp" />
<script
        src="${pageContext.request.contextPath}/resources/js/lib/jquery.fullpage.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/navigation.js"></script>
<script>
    ddiconmenu.docinit({ // initialize an Icon Menu
        menuid: 'myiconmenu', //main menu ID
        easing: "easeInOutCirc",
        dur: 500 //<--no comma after last setting
    })
</script>
</body>
</html>



























