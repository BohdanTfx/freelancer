angular
		.module('FreelancerApp')
		.controller(
				'authCtrl',
				[
						'$scope',
						'$rootScope',
						'$location',
						'$cookieStore',
						'AuthenticationService',
						function($scope, $rootScope, $location, $cookieStore,
								AuthenticationService) {
							$scope.doClose = function() {
								$scope.showError = false;
							}

							if (typeof $cookieStore
									.get('freelancerRememberMeCookieAng') !== 'undefined') {
								$location.path('/');
								return;
							}

							$scope.login = function() {
								AuthenticationService
										.Login($scope.username,
												$scope.password,
												$scope.remember)
										.success(
												function(response) {
													AuthenticationService
															.SetCredentials(
																	response.fname,
																	response.lname,
																	response.role);
													$location.path('/');

												})
										.error(
												function(response) {
													$scope.showError = true;
													$scope.errorTitle = 'Error!';
													$scope.errorDescription = 'Invalid credentials';
												})
							};
						} ]);