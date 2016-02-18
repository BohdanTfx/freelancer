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
						"Notification",
						'$translate',
						function($scope, $rootScope, $location, $cookieStore,
								 AuthenticationService,Notification,$translate) {
							$scope.user = {};
							$scope.social = {};
							$scope.social.linkedin = {};
							$scope.social.linkedin.available = false;


							$scope.confirmCode = $location.search().confirmCode;
							$scope.uuid = $location.search().uuid;
							if($scope.confirmCode!= undefined && $scope.uuid!= undefined){
								AuthenticationService.confirmEmail($scope.confirmCode, $scope.uuid).success(function(data){
										if(data==true){
											Notification.success({
												title:$translate.instant("auth.notification-success-title"),
												message:$translate.instant("auth.notification-success-body")
											});
										}
								});

							}

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
												function(response,status) {
													$scope.showError = true;
													$scope.errorTitle = $translate.instant("auth.error-title");
													$scope.errorDescription = $translate.instant("auth.invalid-cred");
													$scope.user.email = "";
													$scope.user.password = "";
													if(status==406){
														$scope.errorDescription = $translate.instant("auth.error-confirmed-email");
													}
												})
							};

							AuthenticationService.initSocial($scope);
						} ]);