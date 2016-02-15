'use strict';

angular
		.module('FreelancerApp')
		.service(
				'signupAPI',
				function(Notification, $translate) {
					var that = this;

					this.checkEmail = function($http, ngModel, email) {
						$http
								.get("/user/email", {
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
					}

					this.initSocial = function($http, $scope) {
						var linkedinVerifier = getUrlVars();
						if (linkedinVerifier !== undefined
								&& linkedinVerifier.oauth_verifier !== undefined) {
							$http
									.get(
											"/user/signup/linkedin",
											{
												params : {
													verifier : linkedinVerifier.oauth_verifier
												}
											})
									.success(
											function(data, status, headers,
													config) {
												$scope.user.email = data.emailAddress;
												$scope.user.first_name = data.firstName;
												$scope.user.last_name = data.lastName;
												$scope.user.img_url = data.pictureUrl;
												$scope.signup = true;
												$scope.role = localStorage
														.getItem("role");

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
											});
							return;
						}

						$http.get("/user/social", {
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
					}

					this.createUser = function($http, user, adminUUID,
							roleAdmin, adminAPI) {
						if (adminUUID != undefined || roleAdmin == true) {
							roleAdmin = false;
							adminAPI.removeUUID(adminUUID);
						}

						console.log(user);
						var config = {
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8;'
							}
						}
						$http
								.post("/user/create", user, config)
								.success(
										function(data, status, headers, config) {
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
					}

					this.triggerEmailValidation = function($http, $scope) {
						var email = $scope.user.email;
						if (email.match(/^.+@.+\..+$/)) {
							that.checkEmail($http, $scope.emailInputNgModel,
									email);
						}
					};
				});