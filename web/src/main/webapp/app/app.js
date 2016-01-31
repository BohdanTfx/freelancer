;
(function() {
	angular
			.module(
					'FreelancerApp',
					[ 'ngRoute', 'ui.router', 'ngCookies', 'ngMaterial',
							'ngAnimate', 'ngAria', 'ngMessages',
							'isteven-multi-select', 'rzModule' ])
			.config(
					function($stateProvider, $urlRouterProvider,
							$locationProvider) {
						$urlRouterProvider.otherwise('/orders');

						// routes
						$stateProvider
								.state(
										'orders',
										{
											url : '/orders',
											templateUrl : 'app/components/jobs/jobs.html',
											controller : 'jobsCtrl'
										})
								.state(
										'signup',
										{
											url : '/signup',
											templateUrl : 'app/components/signup/signup.html',
											controller : 'signupCtrl'
										})
								.state(
										'personal',
										{
											url : '/personal',
											templateUrl : 'app/components/personal/personal.html',
											controller : 'personalCtrl'
										})
								.state(
										'tests',
										{
											url : '/tests',
											templateUrl : 'app/components/tests/tests.html',
											controller : 'testsCtrl'
										})
								.state(
										'test',
										{
											url : '/tests/:testId',
											templateUrl : 'app/components/test/test.html',
											controller : 'testCtrl'
										})
								.state(
										'home',
										{
											url : '/home',
											templateUrl : 'app/components/home/home.html',
										})
								.state(
										'auth',
										{
											url : '/auth',
											templateUrl : 'app/components/authentication/auth.html',
											controller : 'authCtrl'
										});

						$locationProvider.html5Mode(false);
					})
			.run(
					[
							'$rootScope',
							'$location',
							'$cookieStore',
							'$http',
							function($rootScope, $location, $cookieStore, $http) {
								// keep user logged in after page refresh
								/*
								 * $rootScope.globals =
								 * $cookieStore.get('freelancerRememberMeCookieAng') ||
								 * {}; if ($rootScope.globals.currentUser) {
								 * $http.defaults.headers.common['Authorization'] =
								 * 'Basic ' +
								 * $rootScope.globals.currentUser.username; }
								 */
								$rootScope
										.$on(
												'$locationChangeStart',
												function(event, next, current) {
													// redirect to login page if
													// not logged in
													/*
													 * if (typeof
													 * $rootScope.globals.currentUser ==
													 * 'undefined') if
													 * ($location.path() !==
													 * '/auth') {
													 * $location.path('/auth'); }
													 */

													$rootScope.globals = $cookieStore
															.get('freelancerRememberMeCookieAng')
															|| {};
													if ($rootScope.globals.currentUser) {
														$rootScope.name = $rootScope.globals.currentUser.fname;
														$rootScope.lastName = $rootScope.globals.currentUser.lname;
														$rootScope.role = $rootScope.globals.currentUser.role;

														$rootScope.logged = true;
													} else {
														$rootScope.logged = false;
													}
												});
							} ]);

})();
