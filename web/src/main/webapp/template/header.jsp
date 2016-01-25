<%@ taglib prefix="sec" uri="http://testsystem.fx/jsp/tlds/security"%>
<header class="main-header">
	<div class="header-lower">
		<div class="auto-container clearfix">

			<div class="outer-box">
				<div class="logo">
					<a href="${pageContext.request.contextPath}/home">
						<img alt="Site logo" class="img-logo img-png animated rotateIn"
							data-tooltip="yes" title="Home" data-placement="right"
							src="${pageContext.request.contextPath}/resources/img/logo.png">
					</a>
				</div>

				<nav class="main-menu">
					<div class="navbar-header">
						<button type="button" class="navbar-toggle" data-toggle="collapse"
							data-target=".navbar-collapse">
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>
					</div>

					<div class="navbar-collapse collapse clearfix">
						<sec:access role="none">
							<ul class="navigation">
								<li><a href="${pageContext.request.contextPath}/signin">
										<i class="fa fa-sign-in"></i>Sign In
									</a></li>
								<li><a href="${pageContext.request.contextPath}/chooserole">
										<i class="fa fa-user-plus"></i>Sign Up
									</a></li>
							</ul>
						</sec:access>
						<sec:access role="user">
							<ul class="navigation">
								<li><a
										href="${pageContext.request.contextPath}/cabinet/main">
										<i class="fa fa-home"></i>Cabinet
									</a></li>
								<li id="logout"><a
										href="${pageContext.request.contextPath}/logout">
										<i class="fa fa-sign-out"></i>Logout
									</a></li>
							</ul>
						</sec:access>
					</div>
				</nav>

			</div>
		</div>
	</div>
</header>