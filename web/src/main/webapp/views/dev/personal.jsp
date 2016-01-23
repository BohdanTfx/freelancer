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

</head>
<body>
	<jsp:include page="/template/header.jsp" />
	<jsp:include page="/template/navigation.jsp" />

	<div class="container" style="padding-top: 10px;">
		<div class="row">
			<div class="col-md-12">
				<div class="well panel panel-default">
					<div class="panel-body">
						<div class="row">
							<div class="col-xs-12 col-sm-4 text-center">
								<img src="http://api.randomuser.me/portraits/women/21.jpg"
									alt=""
									class="center-block img-circle img-thumbnail img-responsive">
								<ul class="list-inline ratings text-center" title="Ratings">
									<li><a href="#"><span class="fa fa-desktop fa-lg"></span></a></li>
									<li><a href="#"><span class="fa fa-desktop fa-lg"></span></a></li>
									<li><a href="#"><span class="fa fa-desktop fa-lg"></span></a></li>
									<li><a href="#"><span class="fa fa-desktop fa-lg"></span></a></li>
									<li><a href="#"><span class="fa fa-desktop fa-lg"></span></a></li>
								</ul>
							</div>
							<!--/col-->
							<div class="col-xs-12 col-sm-8 text-right">
								<strong>Pay: </strong> hourly
							</div>
							<div class="col-xs-12 col-sm-8">
								<h2>Jane Doe</h2>
								<p>
									<strong>About: </strong> Web Designer / UI Expert.
								</p>
								<p>
									<strong>e-mail: </strong> test@gmail.com
								</p>
								<p>
									<strong>Time zone: </strong> +2
								</p>
								<p>
									<strong>Language: </strong> Ukrainian
								</p>

							</div>
							<!--/col-->
							<div class="clearfix"></div>
							<div class="col-xs-12 col-sm-12" style="padding-top: 10px;">
								<p>
									<strong>Skills: </strong> <span class="label label-info tags">html5</span>
									<span class="label label-info tags">css3</span> <span
										class="label label-info tags">jquery</span> <span
										class="label label-info tags">bootstrap3</span>
								</p>
							</div>
							<div class="col-xs-12 col-sm-4">
								<h2>
									<strong> 100 </strong>
								</h2>
								<p>
									<small>Project</small>
								</p>
								<button class="btn btn-success btn-block">
									<span class="fa fa-plus-circle"></span> Follow
								</button>
							</div>
							<!--/col-->
							<div class="col-xs-12 col-sm-4">
								<h2>
									<strong>90</strong>
								</h2>
								<p>
									<small>Done</small>
								</p>
								<button class="btn btn-info btn-block">
									<span class="fa fa-user"></span> View Profile
								</button>
							</div>
							<!--/col-->
							<div class="col-xs-12 col-sm-4">
								<h2>
									<strong>10</strong>
								</h2>
								<p>
									<small>Doing</small>
								</p>
								<button type="button" class="btn btn-primary btn-block">
									<span class="fa fa-gear"></span> Options
								</button>
							</div>
							<!--/col-->
						</div>
						<!--/row-->
					</div>
					<!--/panel-body-->
				</div>
				<!--/panel-->
			</div>
			<!--/col-->
		</div>
		<!--/row-->
	</div>

	<jsp:include page="/template/footImport.jsp" />
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/navigation.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/lib/jquery.fullpage.min.js"></script>
</body>
</html>