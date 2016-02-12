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
								AuthenticationService, $http) {
							$scope.user = {};
							$scope.social = {};
							$scope.social.linkedin = {};
							$scope.social.linkedin.available = false;

							$scope.doClose = function() {
								$scope.showError = false;
							};

							$scope.login = function() {
								AuthenticationService
										.Login($scope.user.email,
												$scope.user.password,
												$scope.user.remember)
										.success(
												function(response) {
													AuthenticationService
															.SetCredentials(
																	response.fname,
																	response.lname,
																	response.role);
													if (response.role == 'admin') {
														$location
																.path('/personal');
														return;
													}
													if (response.isFirst) {
														$location
																.path('/personal')
													} else {
														if (response.role == 'developer')
															$location
																	.path('/orders');
														if (response.role == 'customer')
															$location
																	.path('/developers');
													}
												})
										.error(
												function(response) {
													$scope.showError = true;
													$scope.errorTitle = 'Error!';
													$scope.errorDescription = 'Invalid credentials';
													$scope.user.email = "";
													$scope.user.password = "";
												})
							};

							AuthenticationService.initSocial($scope);
						} ]);