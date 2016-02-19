'use strict';

angular
		.module('FreelancerApp')
		.factory(
				'AuthenticationService',
				[
						'Base64',
						'$http',
						'$rootScope',
						'$cookieStore',
						'$timeout',
						'Notification',
						'$state',
						function(Base64, $http, $rootScope, $cookieStore,
								$timeout, Notification, $state) {
							var service = {};

							service.Login = function(username, password,
									remember) {

								var data = 'email=' + username + '&password='
										+ password + '&remember=' + remember;
								$http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

								return $http.post('/unreg/signin', data);
							};

							service.SetCredentials = function(user) {
								$rootScope.globals = {
									currentUser : {
										id : user.id,
										fname : user.fname,
										lname : user.lname,
										role : user.role
									}
								};
							};

							service.ClearCredentials = function() {
								$http({
									url : '/unreg/logout',
									method : "POST"
								});
								$rootScope.globals = {};
								$rootScope.logged = false;
							};

							service.initSocial = function($scope) {
								var linkedinVerifier = getUrlVars();
								if (linkedinVerifier !== undefined
										&& linkedinVerifier.oauth_verifier !== undefined) {
									$http
											.get(
													"/unreg/signin/linkedin",
													{
														params : {
															verifier : linkedinVerifier.oauth_verifier,
															email : $scope.user.email,
															password : $scope.user.password,
															remember : $scope.user.remember
														}
													})
											.success(
													function(response, status,
															headers, config) {
														Notification
																.success({
																	title : 'Welcome!',
																	message : 'Welcome to our system <b>'
																			+ response.fname
																			+ ' '
																			+ response.lname
																			+ '</b>'
																});

														service
																.proceedSuccessAuthentication(response);
													})
											.error(
													function(data, status,
															headers, config) {
														if (status == 400) {
															$scope.showError = true;
															$scope.errorTitle = 'Error!';
															$scope.errorDescription = 'Invalid credentials';
														} else {
															Notification
																	.error({
																		title : 'Error!',
																		message : 'An error occurred while registering. Please try again.'
																	});
															location
																	.replace('/#/auth');
														}
													});
									return;
								}

								$http
										.get(
												"/unreg/social",
												{
													params : {
														callbackUrlLinkedIn : 'http://'
																+ location.host
																+ '/#/auth'
													}
												})
										.success(
												function(data, status, headers,
														config) {
													$scope.social.linkedin.url = data.linkedinUrl;
													$scope.social.linkedin.available = true;
												})
										.error(
												function(data, status, headers,
														config) {
													if (status == 400) {
														$scope.showError = true;
														$scope.errorTitle = 'Error!';
														$scope.errorDescription = 'Invalid credentials';
													} else {
														Notification
																.error({
																	title : 'Error!',
																	message : 'An error occurred while registering. Please try again.'
																});
													}
												});
							};

							service.autoAuthenticate = function() {
								return $http.get("/unreg/authentication/auto");
							};

							service.proceedSuccessAuthentication = function(
									response) {
								service.SetCredentials(response);
								$rootScope.logged = true;
								if (response.role == 'admin') {
									$state.go('statistics');
									return;
								}
								if (response.isFirst) {
									$state.go('personal');
								} else {
									if (response.role == 'developer')
										$state.go('orders');
									if (response.role == 'customer')
										$state.go('developers');
								}
							}

							return service;
						} ])

		.factory(
				'Base64',
				function() {
					/* jshint ignore:start */

					var keyStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';

					return {
						encode : function(input) {
							var output = "";
							var chr1, chr2, chr3 = "";
							var enc1, enc2, enc3, enc4 = "";
							var i = 0;

							do {
								chr1 = input.charCodeAt(i++);
								chr2 = input.charCodeAt(i++);
								chr3 = input.charCodeAt(i++);

								enc1 = chr1 >> 2;
								enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
								enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
								enc4 = chr3 & 63;

								if (isNaN(chr2)) {
									enc3 = enc4 = 64;
								} else if (isNaN(chr3)) {
									enc4 = 64;
								}

								output = output + keyStr.charAt(enc1)
										+ keyStr.charAt(enc2)
										+ keyStr.charAt(enc3)
										+ keyStr.charAt(enc4);
								chr1 = chr2 = chr3 = "";
								enc1 = enc2 = enc3 = enc4 = "";
							} while (i < input.length);

							return output;
						},

						decode : function(input) {
							var output = "";
							var chr1, chr2, chr3 = "";
							var enc1, enc2, enc3, enc4 = "";
							var i = 0;

							// remove all characters that are not A-Z, a-z, 0-9,
							// +, /, or =
							var base64test = /[^A-Za-z0-9\+\/\=]/g;
							if (base64test.exec(input)) {
								window
										.alert("There were invalid base64 characters in the input text.\n"
												+ "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n"
												+ "Expect errors in decoding.");
							}
							input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

							do {
								enc1 = keyStr.indexOf(input.charAt(i++));
								enc2 = keyStr.indexOf(input.charAt(i++));
								enc3 = keyStr.indexOf(input.charAt(i++));
								enc4 = keyStr.indexOf(input.charAt(i++));

								chr1 = (enc1 << 2) | (enc2 >> 4);
								chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
								chr3 = ((enc3 & 3) << 6) | enc4;

								output = output + String.fromCharCode(chr1);

								if (enc3 != 64) {
									output = output + String.fromCharCode(chr2);
								}
								if (enc4 != 64) {
									output = output + String.fromCharCode(chr3);
								}

								chr1 = chr2 = chr3 = "";
								enc1 = enc2 = enc3 = enc4 = "";

							} while (i < input.length);

							return output;
						}
					};

					/* jshint ignore:end */
				});
