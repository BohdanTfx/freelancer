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
</head>
<body>
	<jsp:include page="/template/header.jsp" />
	<ul class="side-navigation">
		<li data-menuanchor="main" class="active" data-tooltip="yes"
			title="Home" data-placement="right"><a href="#main"></a></li>
		<li data-menuanchor="customer" data-tooltip="yes"
			title="Second section" data-placement="right"><a
				href="#customer"></a></li>
		<li data-menuanchor="developer" data-tooltip="yes"
			title="Third section" data-placement="right"><a
				href="#developer"></a></li>
	</ul>
	<div id="fullpage">
		<div class="section">
			<div class="background-img-cover background-home screen-area"></div>
		</div>
		<div class="section">
			<div class="background-img-cover background-cust-home screen-area"></div>
		</div>
		<div class="section">
			<div class="background-img-cover background-dev-home screen-area">
				<div class="header-offset"></div>
				<div class="col-xs-offset-6 col-xs-5">
					<div class="dev-agit">Lorem ipsum dolor sit amet, consectetur
						adipisicing elit, sed do eiusmod tempor incididunt ut labore et
						dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
						exercitation ullamco laboris nisi ut aliquip ex ea commodo
						consequat. Duis aute irure dolor in reprehenderit in voluptate
						velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint
						occaecat cupidatat non proident, sunt in culpa qui officia
						deserunt mollit anim id est laborum.</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/template/footImport.jsp" />
	<script
			src="${pageContext.request.contextPath}/resources/js/home.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/lib/jquery.fullpage.min.js"></script>
</body>
</html>
