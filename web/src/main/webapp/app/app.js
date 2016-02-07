;
(function() {
	angular.module(
			'FreelancerApp',
			[ 'ngRoute', 'ui.router', 'ngCookies', 'ngMaterial', 'ngAnimate',
					'ngAria', 'ngMessages', 'isteven-multi-select', 'rzModule',
					'ui.bootstrap', 'ui-notification','googlechart' ]).config(
			function($stateProvider, $urlRouterProvider, $locationProvider,
					NotificationProvider ) {
				$urlRouterProvider.otherwise('/home');

				NotificationProvider.setOptions({
					delay : 5000,
					startTop : 20,
					startRight : 10,
					verticalSpacing : 20,
					horizontalSpacing : 20,
					positionX : 'right',
					positionY : 'bottom'
				});
				// routes
				$stateProvider.state('orders', {
					url : '/orders',
					templateUrl : 'app/components/jobs/jobs.html',
					controller : 'jobsCtrl'
				}).state('developers', {
					url : '/developers',
					templateUrl : 'app/components/developers/developers.html',
					controller : 'developersCtrl'
				}).state('createOrder', {
					url : '/order/create',
					templateUrl : 'app/components/order/order.html',
					controller : 'orderCtrl'
				}).state('order', {
					url : '/orders/:orderId',
					templateUrl : 'app/components/jobinfo/jobinfo.html',
					controller : 'jobinfoCtrl'
				}).state('signup', {
					url : '/signup',
					templateUrl : 'app/components/signup/signup.html',
					controller : 'signupCtrl'
				}).state('personal', {
					url : '/personal',
					templateUrl : 'app/components/personal/personal.html',
					controller : 'personalCtrl'
				}).state('tests', {
					url : '/tests',
					templateUrl : 'app/components/tests/tests.html',
					controller : 'testsCtrl'
				}).state('myworks', {
					url : '/myworks',
					templateUrl : 'app/components/myworks/myworks.html',
					controller : 'myworksCtrl'
				}).state('test', {
					url : '/tests/:testId',
					templateUrl : 'app/components/test/test.html',
					controller : 'testCtrl'
				}).state('home', {
					url : '/home',
					templateUrl : 'app/components/home/home.html',
					controller : 'homeCtrl'
				}).state('auth', {
					url : '/auth',
					templateUrl : 'app/components/authentication/auth.html',
					controller : 'authCtrl'
				}).state('pubdev', {
                    url: '/public/developer/:devName/:devId',
					templateUrl : 'app/components/pubdev/pubdev.html',
					controller : 'pubdevCtrl'
                }).state('custpub', {
                    url: '/public/customer/:custName/:custId',
                    templateUrl: 'app/components/custpub/custpub.html',
                    controller: 'custpubCtrl'
				}).state('admin', {
					url: '/admin/statistics',
					templateUrl: 'app/components/admin/adminStatistics.html',
					controller: 'adminCtrl'
				}).state('signupadmin', {
					url : '/signup/:uuid',
					templateUrl : 'app/components/signup/signup.html',
					controller : 'signupCtrl'
				});

				$locationProvider.html5Mode(false);
			}).run(
			[
					'$rootScope',
					'$location',
					'$cookieStore',
					'$http',
					'AuthenticationService',
					function($rootScope, $location, $cookieStore, $http,
							AuthenticationService) {
						$rootScope.logout = function() {
							AuthenticationService.ClearCredentials();
						};
                        $rootScope.checking = function (data) {
                            var path = $location.path();
                            if (typeof data.id != 'undefined') {
                                switch (path) {
                                    case '/auth':
                                        $location.path('/');
                                        break;
                                    case '/signup':
                                        $location.path('/');
                                        break;
                                }
                            } else {
                                if (path.indexOf('/orders') > -1) {
                                    $location.path('/');
                                }
                                if (path.indexOf('/personal') > -1) {
                                    $location.path('/');
                                }
                                if (path.indexOf('/myworks') > -1) {
                                    $location.path('/');
                                }
                                if (path.indexOf('/tests') > -1) {
                                    $location.path('/');
                                }
                                if (path.indexOf('/public') > -1) {
                                    $location.path('/');
                                }
                            }
                        };
                        $rootScope.$on('$locationChangeStart', function (event, next, current, $scope) {
							$rootScope.globals = {};
							$rootScope.logged = false;
							$http.post('/user/isAuth').success(function(data) {
								$rootScope.id = data.id;
								$rootScope.name = data.fname;
								$rootScope.lastName = data.lname;
								$rootScope.role = data.role;
								$rootScope.logged = true;

                                $rootScope.checking($rootScope);
							}).error(function() {
                                $rootScope.checking($rootScope);
							});
						});
					} ]);

})();
