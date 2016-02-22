'use strict';

angular
		.module('FreelancerApp')
		.service(
				'signupAPI',
				function(Notification, $translate) {
					var that = this;

					this.checkEmail = function($http, ngModel, email) {
						$http
								.get("/unreg/email", {
									params : {
										email : email
									}
								})
								.success(
										function(data, status, headers, config) {
											ngModel.$setValidity(
													'emailAvailable', data);
										})
								.error(
										function(data, status, headers, config) {
											Notification
													.error({
														title : $translate
																.instant('message.error'),
														message : $translate
																.instant('signup.email.check.error')
													});
										});
					};

					this.signupGoogle = function (auth, $http, $scope) {
						hello(auth).login();

						hello.on('auth.login', function (auth) {
							hello(auth.network).api('/me').then(function (r) {
								$scope.user.email = r.email;
								$scope.user.first_name = r.first_name;
								$scope.user.last_name = r.last_name;
								//$scope.user.img_url = r.thumbnail;
								$scope.signup = true;
								$scope.role = $scope.roles[localStorage
									.getItem("openTaskSignUpRole")].value;
							});
						});

						hello.init({
							google: '344510194886-fcto0du17jj39h2oil732hu2cmuq7p67.apps.googleusercontent.com'
						}, {
							redirect_uri: 'http://localhost:8081/index.html',
							scope: 'email'
						});
					};

					this.initSocial = function($http, $scope) {
						var linkedinVerifier = getUrlVars();
						if (linkedinVerifier !== undefined
								&& linkedinVerifier.oauth_verifier !== undefined) {
							$http
									.get(
											"/unreg/signup/linkedin",
											{
												params : {
													verifier : linkedinVerifier.oauth_verifier
												}
											})
									.success(
											function(data, status, headers,
													config) {
												if (data != "") {
													$scope.user.email = data.emailAddress;
													$scope.user.first_name = data.firstName;
													$scope.user.last_name = data.lastName;
													$scope.user.img_url = data.pictureUrl;
													$scope.signup = true;
												} else {
													location
															.replace('/#/signup');
												}

												$scope.role = $scope.roles[localStorage
														.getItem("openTaskSignUpRole")].value;
											})
									.error(
											function(data, status, headers,
													config) {
												Notification
														.error({
															title : $translate
																	.instant('message.error'),
															message : $translate
																	.instant('signup.social.linkedin.error')
														});
												location.replace('/#/signup');
											});
							return;
						}

						$http.get("/unreg/social", {
							params : {
								callbackUrlLinkedIn : document.URL
							}
						}).success(function(data, status, headers, config) {
							$scope.social.linkedin.url = data.linkedinUrl;
							$scope.social.linkedin.available = true;
						}).error(
								function(data, status, headers, config) {
									Notification.error({
										title : $translate
												.instant('message.error'),
										message : $translate
												.instant('signup.social.error')
									});
								});
					};

					this.createUser = function($http, user, adminUUID,
							roleAdmin, adminAPI) {
						if (adminUUID != undefined || roleAdmin == true) {
							roleAdmin = false;
							adminAPI.removeUUID(adminUUID);
						}

						var config = {
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8;'
							}
						};
						$http
								.post("/unreg/create", user, config)
								.success(
										function(data, status, headers, config) {

											Notification
												.success({
													title : $translate
														.instant('signup.notification-success-title'),
													message : $translate
														.instant('signup.notification-success-body')
												});
											location.replace("/#/auth");

										})
								.error(
										function(data, status, headers, config) {
											Notification
													.error({
														title : $translate
																.instant('message.error'),
														message : $translate
																.instant('signup.user.create.error')
													});
										});
					};

					this.triggerEmailValidation = function($http, $scope) {
						var email = $scope.user.email;
						if (email.match(/^.+@.+\..+$/)) {
							that.checkEmail($http, $scope.emailInputNgModel,
									email);
						}
					};
				});
