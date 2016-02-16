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
							$scope.user = {};
							$scope.social = {};
							$scope.social.linkedin = {};
							$scope.social.linkedin.available = false;

							$scope.doClose = function() {
								$scope.showError = false;
							};

							hello.on('auth.login', function (auth) {
								hello(auth.network).api('/me').then(function (r) {
									// Inject it into the container
									/*var label = document.getElementById('profile_' + auth.network);
									 if (!label) {
									 label = document.createElement('div');
									 label.id = 'profile_' + auth.network;
									 document.getElementById('profile').appendChild(label);
									 }
									 label.innerHTML = '<img src="' + r.thumbnail + '" /> Hey ' + r.name;*/
								});
							});

							hello.init({
								google: '519393406522-9ehstqc1vuddj5fhkof2dnkmv58118o4.apps.googleusercontent.com'
							}, {redirect_uri: document.URL});

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
														$location.path('/admin/statistics');
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