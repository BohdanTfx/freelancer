angular
		.module('FreelancerApp')
		.controller(
				'signupCtrl',
				function($scope, signupAPI, $log, $http,$location,createadminAPI) {
					$scope.page = {};
					$scope.user = {};
					$scope.signup = false;
					$scope.signupForm = {};

					$scope.roleAdmin = false;
					$scope.adminUUID = $location.search().uuid;

					if($scope.adminUUID!=undefined){
						createadminAPI.checkAvailableUUID($scope.adminUUID).success(function(data){
							$scope.roleAdmin = data;
							$scope.signup = data;
							}
						);
					}





					$scope.roles = [ "developer", "customer" ];



					$scope.chooseRole = function(signup, role) {
						$scope.role = role;
						$scope.signup = signup;
						localStorage.setItem("role", role);
					};

					$scope.getCurrentZone = function() {
						var offset = new Date().getTimezoneOffset(), o = Math
								.abs(offset);
						return Math.floor(o / 60);
					};

					$scope.createUser = function() {
						signupAPI.createUser($http, $scope.user,$scope.adminUUID,$scope.roleAdmin,createadminAPI);
					};

					$scope.resetInputs = function() {
						$scope.user.first_name = "";
						$scope.user.last_name = "";
						$scope.user.email = "";
						$scope.user.password = "";
						$scope.user.passwordconfirm = "";
						$scope.user.zone = $scope.getCurrentZone();
					};

					signupAPI.initSocial($http, $scope);

					$scope.timeZones = [
							{
								zone : "-12",
								title : "-12 Baker island, Howland island",
								ticked : false
							},
							{
								zone : "-11",
								title : "-11 American Samoa, Niue",
								ticked : false
							},
							{
								zone : "-10",
								title : "-10 Hawaii",
								ticked : false
							},
							{
								zone : "-9",
								title : "-9 Marquesas Islands, Gamblie Islands",
								ticked : false
							},
							{
								zone : "-8",
								title : "-8 British Columbia, Mexico, California",
								ticked : false
							},
							{
								zone : "-7",
								title : "-7 British Columbia, US Arizona",
								ticked : false
							},
							{
								zone : "-6",
								title : "-6 Canada Saskatchewan, Costa Rica, Guatemala, Honduras",
								ticked : false
							},
							{
								zone : "-5",
								title : "-5 Colombia, Cuba, Ecuador, Peru",
								ticked : false
							},
							{
								zone : "-4",
								title : "-4 Venezuela, Bolivia, Brazil,	Barbados",
								ticked : false
							},
							{
								zone : "-3",
								title : "-3 Newfoundland, Argentina, Chile",
								ticked : false
							},
							{
								zone : "-2",
								title : "-2 South Georgia",
								ticked : false
							},
							{
								zone : "-1",
								title : "-1 Capa Verde",
								ticked : false
							},
							{
								zone : "0",
								title : "0 Ghana, Iceland, Senegal",
								ticked : false
							},
							{
								zone : "1",
								title : "+1 Algeria, Nigeria, Tunisia",
								ticked : false
							},
							{
								zone : "2",
								title : "+2 Ukraine, Zambia, Egypt",
								ticked : false
							},
							{
								zone : "3",
								title : "+3 Belarus, Iraq, Iran",
								ticked : false
							},
							{
								zone : "4",
								title : "+4 Armenia, Georgia, Oman",
								ticked : false
							},
							{
								zone : "5",
								title : "+5 Kazakhstan, Pakistan, India",
								ticked : false
							},
							{
								zone : "6",
								title : "+6 Ural, Bangladesh",
								ticked : false
							},
							{
								zone : "7",
								title : "+7 Western Indonesai, Thailand",
								ticked : false
							},
							{
								zone : "8",
								title : "+8 Hong Kong, China, Taiwan, Australia",
								ticked : false
							}, {
								zone : "9",
								title : "+9 Timor,Japan",
								ticked : false
							}, {
								zone : "10",
								title : "+10 New Guinea, Australia",
								ticked : false
							}, {
								zone : "11",
								title : "+11 Solomon Islands, Vanuatu",
								ticked : false
							}, {
								zone : "12",
								title : "+12 New zealand, Kamchatka, Kiribati",
								ticked : false
							} ];
				})
		.directive(
				'emailAvailable',
				[
						'signupAPI',
						'$http',
						function(signupAPI, $http) {
							return {
								restrict : "A",
								require : "ngModel",
								link : function($scope, element, attrs, ngModel) {
									element
											.bind(
													"keyup",
													function() {
														clearInterval($scope.emailValidationTimeout);
														var email = $scope.signupForm.email.$viewValue;
														if (email
																.match(/^.+@.+\..+$/)) {
															$scope.emailValidationTimeout = setTimeout(
																	function() {
																		signupAPI
																				.checkEmail(
																						$http,
																						ngModel,
																						email);
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
