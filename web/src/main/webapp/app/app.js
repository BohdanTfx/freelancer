;
(function() {
	angular.module(
			'FreelancerApp',
			[ 'ngRoute', 'ui.router', 'ngCookies', 'ngMaterial', 'ngAnimate',
					'ngAria', 'ngMessages', 'isteven-multi-select', 'rzModule',
					'ui.bootstrap', 'ui-notification' ]).config(
			function($stateProvider, $urlRouterProvider, $locationProvider,
					NotificationProvider) {
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
					templateUrl : 'app/components/home/home.html'
				}).state('auth', {
					url : '/auth',
					templateUrl : 'app/components/authentication/auth.html',
					controller : 'authCtrl'
				}).state('pubdev', {
					url : '/public/:devName/:devId',
					templateUrl : 'app/components/pubdev/pubdev.html',
					controller : 'pubdevCtrl'
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
						$rootScope.$on('$locationChangeStart', function(event,
								next, current, $scope) {
							$rootScope.globals = {};
							$rootScope.logged = false;
							$http.post('/user/isAuth').success(function(data) {
								console.log(data);
								$rootScope.name = data.fname;
								$rootScope.lastName = data.lname;
								$rootScope.role = data.role;
								$rootScope.logged = true;
							}).error(function() {
							});
							console.log('rootScope ' + $rootScope.globals);
						});
					} ]);

})();
