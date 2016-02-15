angular.module('FreelancerApp').controller(
		'signupCtrl',
		function($scope, signupAPI, $log, $http, $location, createadminAPI) {
			$scope.page = {};
			$scope.user = {};
			$scope.signup = false;
			$scope.signupForm = {};
			$scope.social = {};
			$scope.social.linkedin = {};
			$scope.social.linkedin.available = false;

			$scope.roleAdmin = false;
			$scope.adminUUID = $location.search().uuid;

			if ($scope.adminUUID != undefined) {
				createadminAPI.checkAvailableUUID($scope.adminUUID).success(
						function(data) {
							$scope.roleAdmin = data;
							$scope.signup = data;
						});
			}

			$scope.roles = [ "developer", "customer" ];

			$scope.chooseRole = function(signup, role) {
				$scope.role = role;
				$scope.signup = signup;
				localStorage.setItem("role", role);
			}

			$scope.getCurrentZone = function() {
				var offset = new Date().getTimezoneOffset(), o = Math
						.abs(offset);
				return Math.floor(o / 60);
			}

			$scope.createUser = function() {
				signupAPI.createUser($http, $scope.user, $scope.adminUUID,
						$scope.roleAdmin, createadminAPI);
			}

			$scope.resetInputs = function() {
				$scope.user.first_name = "";
				$scope.user.last_name = "";
				$scope.user.email = "";
				$scope.user.password = "";
				$scope.user.passwordconfirm = "";
				$scope.user.zone = $scope.getCurrentZone();
			}

			signupAPI.initSocial($http, $scope);

			$scope.timeZones = getTimeZones();
		}).directive(
		'emailAvailable',
		[
				'signupAPI',
				'$http',
				function(signupAPI, $http) {
					return {
						restrict : "A",
						require : "ngModel",
						link : function($scope, element, attrs, ngModel) {
							element.bind("keyup", function() {
								clearInterval($scope.emailValidationTimeout);
								var email = $scope.signupForm.email.$viewValue;
								if (email.match(/^.+@.+\..+$/)) {
									$scope.emailValidationTimeout = setTimeout(
											function() {
												signupAPI.checkEmail($http,
														ngModel, email);
											}, 1000);
								}
							});

							$scope.emailInputNgModel = ngModel;
						}
					}
				} ]).directive(
		'match',
		function($parse) {
			return {
				require : '?ngModel',
				restrict : 'A',
				link : function(scope, elem, attrs, ctrl) {
					if (!ctrl) {
						return;
					}

					var matchGetter = $parse(attrs.match);

					scope.$watch(getMatchValue, function() {
						ctrl.$$parseAndValidate();
					});

					ctrl.$validators.match = function() {
						var match = getMatchValue();
						var value = ctrl.$viewValue === match;
						return value;
					};

					function getMatchValue() {
						var match = matchGetter(scope);
						if (angular.isObject(match)
								&& match.hasOwnProperty('$viewValue')) {
							match = match.$viewValue;
						}
						return match;
					}
				}
			};
		});
;