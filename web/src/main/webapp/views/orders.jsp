<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/lib/bootstrap-slider.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/lib/bootstrap-switch.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/lib/awesome-bootstrap-checkbox.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/lib/select2.min.css">
</head>
<body>
	<jsp:include page="/template/header.jsp" />
	<div class="container">
		<div class="row">
			<div class="header-offset"></div>
			<div class="panel panel-primary sticky-element">
				<div class="panel-heading" id="filterHeader">
					<div class="panel-title">
						<label>Filter</label>
						<div class="pull-right">
							<button
								class="btn btn-warning order-open-filter-btn animated slideInUp"
								data-tooltip="yes" title="Open filter" data-placement="left">
								<i class="fa fa-angle-double-down fa-2x"></i>
							</button>
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
							<label class="control-label col-sm-2" for="order_pay_type">
								Order pay type </label>
							<div class="col-sm-10">
								<div class="row">
									<div class="col-sm-5">
										<div class="checkbox checkbox-info">
											<input type="checkbox" id="hourlyPayType"
												name="hourly_pay_type">
											<label for="hourlyPayType"> Hourly </label>
										</div>
										<input id="hourlyPayTypeSlider" type="text">
									</div>
									<div class="col-sm-5">
										<div class="checkbox checkbox-info">
											<input type="checkbox" id="fixedPayType"
												name="fixed_pay_type">
											<label for="fixedPayType"> Fixed </label>
										</div>
										<input id="fixedPayTypeSlider" type="text">
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-2" for="timeZoneSelect">
								Time zones </label>
							<div class="col-sm-7">
								<select id="timeZoneSelect" multiple="multiple">
									<option value="-12">-12 Baker island, Howland island</option>
									<option value="-11">-11 American Samoa, Niue</option>
									<option value="-10">-10 Hawaii</option>
									<option value="-9">-9 Marquesas Islands, Gamblier
										Islands</option>
									<option value="-8">-8 British Columbia, Mexico,
										California</option>
									<option value="-7">-7 British Columbia, US Arizona</option>
									<option value="-6">-6 Canada Saskatchewan, Costa Rica,
										Guatemala, Honduras</option>
									<option value="-5">-5 Colombia, Cuba, Ecuador, Peru</option>
									<option value="-4">-4 Venezuela, Bolivia, Brazil,
										Barbados</option>
									<option value="-3">-3 Newfoundland, Argentina, Chile</option>
									<option value="-2">-2 South Georgia</option>
									<option value="-1">-1 Capa Verde</option>
									<option value="0">0 Ghana, Iceland, Senegal</option>
									<option value="+1">+1 Algeria, Nigeria, Tunisia</option>
									<option value="+2">+2 Ukraine, Zambia, Egypt</option>
									<option value="+3">+3 Belarus, Iraq, Iran</option>
									<option value="+4">+4 Armenia, Georgia, Oman</option>
									<option value="+5">+5 Kazakhstan, Pakistan, India</option>
									<option value="+6">+6 Ural, Bangladesh</option>
									<option value="+7">+7 Western Indonesai, Thailand</option>
									<option value="+8">+8 Hong Kong, China, Taiwan,
										Australia</option>
									<option value="+9">+9 Timor,Japan</option>
									<option value="+10">+10 New Guinea, Australia</option>
									<option value="+11">+11 Solomon Islands, Vanuatu</option>
									<option value="+12">+12 New zealand, Kamchatka,
										Kiribati</option>
								</select>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="panel-group">
				<div class="pagination dark animated fadeIn">
					<div class="col-sm-8">
						<a href="?firstPage=yes" class="page dark gradient">
							<i class="fa fa-chevron-left"></i> <i class="fa fa-chevron-left"></i>
							<span>First</span>
						</a>
						<c:forEach items="${pagedItems}" var="page" varStatus="loop">
							<c:choose>
								<c:when test="${page.first eq 'current'}">
									<span class="page dark active"> ${page.second + 1} </span>
								</c:when>
								<c:otherwise>
									<a href="${page.first}" class="page dark gradient">
										${page.second + 1} </a>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<a href="?lastPage=yes" class="page dark gradient">
							<span>Last</span>
							<i class="fa fa-chevron-right"></i> <i
								class="fa fa-chevron-right"></i>
						</a>
					</div>
					<div class="pull-right col-sm-4">
						<div class="hidden-xs col-sm-6">
							<input id="dateSortInput" type="checkbox" checked
								data-label-text="Date" data-on-color="info"
								data-off-color="warning" data-size="mini" data-on-text="Asc"
								data-off-text="Desc">
						</div>
						<div class="pull-right col-sm-6">
							<input id="hourlySortInput" type="checkbox" checked
								data-label-text="Payment" data-on-color="info"
								data-off-color="warning" data-size="mini" data-on-text="Asc"
								data-off-text="Desc">
						</div>
					</div>
				</div>
				<c:forEach items="${orders}" var="order">
					<div class="panel panel-info">
						<div class="panel-heading">
							<div class="panel-title">
								<c:out value="${order.title}" />
								<div class="pull-right col-xs-3 col-sm-4">
									<div class="hidden-xs label label-warning col-sm-7">Added</div>
									<div class="label label-info col-sm-5 pull-right">
										<c:out value="${order.payType}" />
									</div>
								</div>
							</div>
						</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-10 col-sm-10">
									<c:out value="${order.descr}" />
								</div>
								<div class="pull-right col-xs-3 col-sm-4">
									<div class="hidden-xs label label-warning col-sm-7">
										<fmt:formatDate value="${order.date}" var="formattedDate"
											type="date" pattern="HH-mm MM-dd-yyyy" />
										<c:out value="${formattedDate}" />
									</div>
									<div class="label label-info col-sm-5 pull-right">
										<fmt:formatNumber value="${order.payment}" var="number"
											currencySymbol="$" type="currency" />
										<c:out value="${number}" />
									</div>
								</div>
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
	<script
		src="${pageContext.request.contextPath}/resources/js/lib/bootstrap-slider.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/lib/bootstrap-switch.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/lib/select2.min.js"></script>
</body>
</html>
