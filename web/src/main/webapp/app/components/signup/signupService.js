'use strict';

angular
		.module('FreelancerApp')
		.service(
				'signupAPI',
				function(Notification) {
					var that = this;

					this.checkEmail = function($http, ngModel, email) {
						$http.get("/user/email", {
							params : {
								email : email
							}
						}).success(function(data, status, headers, config) {
							ngModel.$setValidity('emailAvailable', data);
						}).error(function(data, status, headers, config) {
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
															title : 'Error!',
															message : 'An error occurred while registering via Linkedin. Please try again.'
														});
											});
							return;
						}

						$http
								.get("/user/social", {
									params : {
										callbackUrlLinkedIn : document.URL
									}
								})
								.success(
										function(data, status, headers, config) {
											$scope.linkedinUrl = data.linkedinUrl;
										})
								.error(
										function(data, status, headers, config) {
											Notification
													.error({
														title : 'Error!',
														message : 'An error occurred while registering. Please try again.'
													});
										});
					}

					this.createUser = function($http, user) {
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
														title : 'Error!',
														message : 'An error occurred while registering. Please try again.'
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

function getUrlVars() {
	var vars = [], hash;
	var hashes = window.location.href.slice(
			window.location.href.indexOf('?') + 1).split('&');
	for (var i = 0; i < hashes.length; i++) {
		hash = hashes[i].split('=');
		vars.push(hash[0]);
		vars[hash[0]] = hash[1];
	}
	return vars;
}