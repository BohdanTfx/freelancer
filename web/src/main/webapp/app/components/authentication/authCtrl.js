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
					'$http',
						function($scope, $rootScope, $location, $cookieStore,
								 AuthenticationService, Notification, $translate, $http) {
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


							$scope.signinGoogle = function (auth) {
								AuthenticationService.signinGoogle(auth, $scope);

							};

							hello.init({
								google: '344510194886-fcto0du17jj39h2oil732hu2cmuq7p67.apps.googleusercontent.com'
							}, {
								redirect_uri: 'http://localhost:8081/index.html',
								scope: 'email'
							});

							$scope.login = function() {
								AuthenticationService
										.Login($scope.user.email,
												$scope.user.password,
												$scope.user.remember)
										.success(
												function(response) {
													AuthenticationService
															.proceedSuccessAuthentication(response);
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