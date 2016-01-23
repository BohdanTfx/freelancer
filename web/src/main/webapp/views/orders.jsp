<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Orders</title>
<jsp:include page="/template/headImport.jsp" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/orders.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/pagination.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/lib/hover.min.css">
</head>
<body>
	<jsp:include page="/template/header.jsp" />
	<div class="container">
		<div class="row">
			<div class="header-offset"></div>
			<div class="panel panel-primary sticky-element">
				<div class="panel-heading">
					<div class="panel-title">
						<a data-toggle="collapse" href="#order_filter"> Filter </a>
						<div class="pull-right">
							<a class="btn btn-warning order-open-filter-btn animated slideInUp"
								data-tooltip="yes" data-toggle="collapse" title="Open filter"
								data-placement="left" href="#order_filter">
								<i class="fa fa-angle-double-down fa-2x"></i>
							</a>
						</div>
					</div>
				</div>
				<div id="order_filter" class="panel-collapse collapse">
					<div class="panel-body form-horizontal">
						<div class="form-group">
							<label class="control-label col-sm-2" for="order_title">
								Search title </label>
							<div class="col-sm-7">
								<input class="form-control" id="order_title" name="order_title"
									placeholder="Enter title">
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-2" for="order_technologies">
								Techologies </label>
							<div class="col-sm-7">
								<input class="form-control" id="order_technologies"
									name="order_technologies" placeholder="Select technologies">
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-2" for="order_title">
								Search title </label>
							<div class="col-sm-7">
								<input class="form-control" id="order_title" name="order_title"
									placeholder="Enter title">
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="panel-group">
				<div class="pagination dark animated fadeIn">
					<a href="?firstPage=yes" class="page dark gradient">
						<i class="fa fa-chevron-left"></i> <i class="fa fa-chevron-left"></i>
						<span>First </span>
					</a>
					<c:forEach items="${pagedItems}" var="page" varStatus="loop">
						<c:choose>
							<c:when test="${page.first eq 'current'}">
								<span class="page dark active">${page.second + 1}</span>
							</c:when>
							<c:otherwise>
								<a href="${page.first}" class="page dark gradient">${page.second + 1}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<a href="?lastPage=yes" class="page dark gradient">
						<span>Last </span>
						<i class="fa fa-chevron-right"></i> <i class="fa fa-chevron-right"></i>
					</a>
				</div>
				<c:forEach items="${orders}" var="order">
					<div class="panel panel-info">
						<div class="panel-heading">
							<div class="panel-title">
								<c:out value="${order.title}" />
								<div class="badge pull-right">
									<c:out value="${order.payType}" />
								</div>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-10">
									<c:out value="${order.descr}" />
								</div>
								<div class="col-xs-2"></div>
							</div>
						</div>
						<div class="panel-footer">
							<div class="row">
								<ul class="tags animated zoomIn">
									<li><a href="#" class="hvr-grow">Java</a></li>
									<li><a href="#" class="hvr-grow">C#</a></li>
								</ul>
							</div>
						</div>
					</div>
				</c:forEach>
				<div class="pagination dark">
					<a href="?firstPage=yes" class="page dark gradient">
						<i class="fa fa-chevron-left"></i> <i class="fa fa-chevron-left"></i>
						<span>First </span>
					</a>
					<c:forEach items="${pagedItems}" var="page" varStatus="loop">
						<c:choose>
							<c:when test="${page.first eq 'current'}">
								<span class="page dark active">${page.second + 1}</span>
							</c:when>
							<c:otherwise>
								<a href="${page.first}" class="page dark gradient">${page.second + 1}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<a href="?lastPage=yes" class="page dark gradient">
						<span>Last </span>
						<i class="fa fa-chevron-right"></i> <i class="fa fa-chevron-right"></i>
					</a>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="/template/footImport.jsp" />
	<script src="${pageContext.request.contextPath}/resources/js/orders.js"></script>
</body>
</html>
